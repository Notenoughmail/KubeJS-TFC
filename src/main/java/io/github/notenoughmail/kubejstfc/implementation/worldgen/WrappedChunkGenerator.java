package io.github.notenoughmail.kubejstfc.implementation.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.ChunkGenAwareWorldGenerationContext;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.Seed;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataGenerator;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class WrappedChunkGenerator extends ChunkGenerator implements ChunkGeneratorExtension {

    public static final MapCodec<WrappedChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ChunkGenerator.CODEC.fieldOf("generator").forGetter(c -> c.wrapped),
            Codec.STRING.fieldOf("event_key").forGetter(c -> c.key),
            Settings.CODEC.fieldOf("settings").forGetter(c -> c.settings)
    ).apply(instance, WrappedChunkGenerator::new));

    public static ChunkGenerator getWrapper(ChunkGenerator gen) {
        final WrappedChunkGenerator wrapper = gen.kubejs_tfc$getWrapper();
        return wrapper == null ? gen : wrapper;
    }

    public static ChunkGenerator copy(ChunkGenerator generator) {
        return ChunkGenerator.CODEC.decode(
                NbtOps.INSTANCE,
                ChunkGenerator.CODEC.encodeStart(
                        NbtOps.INSTANCE,
                        generator
                ).getOrThrow()
        ).getOrThrow().getFirst();
    }

    private final ChunkGenerator wrapped;
    private final String key;
    private Settings settings;

    private KubeChunkDataGenerator chunkDataGenerator;
    private Climate.Sampler climateSampler;

    public WrappedChunkGenerator(ChunkGenerator wrapped, String key, Settings settings) {
        super(wrapped.getBiomeSource());
        this.wrapped = wrapped;
        this.key = key;
        this.settings = settings;
        wrapped.kubejs_tfc$SetWrapper(this);
    }

    public ChunkGenerator getWrapped() {
        return wrapped;
    }

    @Override
    public Settings settings() {
        return settings;
    }

    @Override
    public void applySettings(UnaryOperator<Settings> settings) {
        this.settings = settings.apply(this.settings);
    }

    @Override
    public ChunkDataGenerator chunkDataGenerator() {
        return chunkDataGenerator;
    }

    @Override
    public Aquifer getOrCreateAquifer(ChunkAccess chunk) {
        return chunkDataGenerator.makeAquifer(chunk);
    }

    @Override
    public BlockPos findSpawnBiome(RandomSource random) {
        final int step = Math.max(4, settings.spawnDistance() / 256); // Check every 4 quarts / 1 chunk at a minimum
        final int centerX = QuartPos.fromBlock(settings.spawnCenterX());
        final int centerZ = QuartPos.fromBlock(settings.spawnCenterZ());
        final int maxRadius = QuartPos.fromBlock(settings.spawnDistance());

        int count = 0;

        // This *should* be called after #initRandomState, see MinecraftServer#createLevels
        Climate.SpawnFinder.Result result = Climate.SpawnFinder.getSpawnPositionAndFitness(climateSampler.spawnTarget(), climateSampler, settings.spawnCenterX(), settings.spawnCenterZ());
        for (int quartX = centerX - maxRadius ; quartX < centerX + maxRadius ; quartX += step) {
            for (int quartZ = centerZ - maxRadius ; quartZ < centerZ + maxRadius ; quartZ += step) {
                if (random.nextInt(count + 1) == 0) {
                    final Climate.SpawnFinder.Result atQuart = Climate.SpawnFinder.getSpawnPositionAndFitness(climateSampler.spawnTarget(), climateSampler, QuartPos.toBlock(quartX), QuartPos.toBlock(quartZ));
                    if (atQuart.fitness() < result.fitness()) {
                        result = atQuart;
                        count++;
                    }
                }
            }
        }

        return result.location();
    }

    @Override
    public void initRandomState(ChunkMap chunkMap, ServerLevel level) {
        if (chunkDataGenerator != null) {
            final WrappedChunkGenerator copy = new WrappedChunkGenerator(copy(wrapped), key, settings);
            chunkMap.tfc$updateGenerator(copy);
            copy.initRandomState(chunkMap, level);
            return;
        }

        final RandomState rs = chunkMap.accessor$getRandomState();

        chunkDataGenerator = KubeChunkDataGenerator.create(key, settings, Seed.of(level.getSeed()), rs);
        climateSampler = rs.sampler();

        rs.tfc$setChunkGeneratorExtension(this);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
        wrapped.applyCarvers(level, seed, random, biomeManager, structureManager, chunk, step);
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
        if (wrapped instanceof NoiseBasedChunkGenerator noise) {
            // Noise based generators use a WorldGenerationContext for depth information
            // Replicate that but with access to the generator and thus the ChunkDataProvider
            // for use with RockSurfaceRuleSources
            //
            // This will not work with subclasses of NoiseBasedChunkGenerator that do not make use of the extended #buildSurface
            final WorldGenerationContext ctx = new ChunkGenAwareWorldGenerationContext(this, level);
            noise.buildSurface(chunk, ctx, random, structureManager, level.getBiomeManager(), level.registryAccess().registryOrThrow(Registries.BIOME), Blender.of(level));
        } else {
            wrapped.buildSurface(level, structureManager, random, chunk);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion level) {
        wrapped.spawnOriginalMobs(level);
    }

    @Override
    public int getGenDepth() {
        return wrapped.getGenDepth();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        final ChunkData data = chunkDataGenerator.generate(chunk);
        return wrapped.fillFromNoise(blender, randomState, structureManager, chunk)
                .thenApplyAsync(chunkAccess -> {
                    chunkDataGenerator.generateFullIfNot(data, chunkAccess);
                    data.getRockData().useCache(chunkAccess.getPos());
                    return chunkAccess;
                }, Util.backgroundExecutor());
    }

    @Override
    public int getSeaLevel() {
        return wrapped.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return wrapped.getMinY();
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return wrapped.getBaseHeight(x, z, type, level, random);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
        return wrapped.getBaseColumn(x, z, height, random);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
        wrapped.addDebugScreenInfo(info, random, pos);
    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetLookup, RandomState randomState, long seed) {
        return wrapped.createState(structureSetLookup, randomState, seed);
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunk) {
        // Create the biomes *after* generating the data to match TFC order
        return CompletableFuture.supplyAsync(() -> chunkDataGenerator.generate(chunk), Util.backgroundExecutor())
                .thenComposeAsync(
                        d -> wrapped.createBiomes(randomState, blender, structureManager, chunk),
                        Util.backgroundExecutor()
                );
    }

    @Override
    @Nullable
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> structure, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
        return wrapped.findNearestMapStructure(level, structure, pos, searchRadius, skipKnownStructures);
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory category, BlockPos pos) {
        return wrapped.getMobsAt(biome, structureManager, category, pos);
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunk, StructureTemplateManager structureTemplateManager) {
        chunkDataGenerator.generate(chunk);
        wrapped.createStructures(registryAccess, structureState, structureManager, chunk, structureTemplateManager);
    }

    @Override
    public void createReferences(WorldGenLevel level, StructureManager structureManager, ChunkAccess chunk) {
        wrapped.createReferences(level, structureManager, chunk);
    }

    @Override
    public int getFirstFreeHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return wrapped.getFirstFreeHeight(x, z, type, level, random);
    }

    @Override
    public int getFirstOccupiedHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor level, RandomState random) {
        return wrapped.getFirstOccupiedHeight(x, z, types, level, random);
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor level) {
        return wrapped.getSpawnHeight(level);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        wrapped.applyBiomeDecoration(level, chunk, structureManager);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return wrapped.getBiomeSource();
    }

    @Override
    public BiomeGenerationSettings getBiomeGenerationSettings(Holder<Biome> biome) {
        return wrapped.getBiomeGenerationSettings(biome);
    }
}
