package com.notenoughmail.kubejs_tfc.util.implementation.custom.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IChunkGenWrapper;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.KubeChunkDataGenerator;
import net.dries007.tfc.mixin.accessor.ChunkMapAccessor;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.RandomStateExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
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
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.UnaryOperator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WrappedChunkGenerator extends ChunkGenerator implements ChunkGeneratorExtension {

    public static final Codec<WrappedChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChunkGenerator.CODEC.fieldOf("generator").forGetter(c -> c.wrapped),
            Codec.STRING.fieldOf("event_key").forGetter(c -> c.key),
            Settings.CODEC.fieldOf("settings").forGetter(c -> c.settings)
    ).apply(instance, WrappedChunkGenerator::new));

    public static ChunkGenerator getWrapper(ChunkGenerator gen) {
        final WrappedChunkGenerator wrapper = ((IChunkGenWrapper) gen).kubejs_tfc$getWrapper();
        return wrapper == null ? gen : wrapper;
    }

    private final ChunkGenerator wrapped;
    private final String key;
    private Settings settings;
    private KubeChunkDataGenerator chunkDataGenerator;

    public WrappedChunkGenerator(ChunkGenerator wrapped, String key, Settings settings) {
        super(wrapped.getBiomeSource());
        this.wrapped = wrapped;
        this.key = key;
        this.settings = settings;
        ((IChunkGenWrapper) wrapped).kubejs_tfc$SetWrapper(this);
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
    public ChunkDataProvider chunkDataProvider() {
        return chunkDataGenerator.provider();
    }

    @Override
    public Aquifer getOrCreateAquifer(ChunkAccess chunk) {
        return chunkDataGenerator.makeAquifer(chunk);
    }

    @Override
    public void initRandomState(ChunkMap chunkMap, ServerLevel level) {
        if (chunkDataGenerator != null) {
            final WrappedChunkGenerator copy = new WrappedChunkGenerator(wrapped, key, settings);
            ((ChunkMapAccessor) chunkMap).accessor$setGenerator(copy);
            copy.initRandomState(chunkMap, level);
            return;
        }

        chunkDataGenerator = KubeChunkDataGenerator.create(key, settings.rockLayerSettings());

        ((RandomStateExtension) (Object) ((ChunkMapAccessor) chunkMap).accessor$getRandomState()).tfc$setChunkGeneratorExtension(this);
    }

    @Override
    protected Codec<WrappedChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {
        wrapped.applyCarvers(pLevel, pSeed, pRandom, pBiomeManager, pStructureManager, pChunk, pStep);
    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
        wrapped.buildSurface(pLevel, pStructureManager, pRandom, pChunk);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {
        wrapped.spawnOriginalMobs(pLevel);
    }

    @Override
    public int getGenDepth() {
        return wrapped.getGenDepth();
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
        final ChunkData data = chunkDataGenerator.generate(pChunk);
        return wrapped.fillFromNoise(pExecutor, pBlender, pRandom, pStructureManager, pChunk)
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
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState pRandom) {
        return wrapped.getBaseHeight(pX, pZ, pType, pLevel, pRandom);
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        return wrapped.getBaseColumn(pX, pZ, pHeight, pRandom);
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {
        wrapped.addDebugScreenInfo(pInfo, pRandom, pPos);
    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> pStructureSetLookup, RandomState pRandomState, long pSeed) {
        return wrapped.createState(pStructureSetLookup, pRandomState, pSeed);
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Executor pExecutor, RandomState pRandomState, Blender pBlender, StructureManager pStructureManager, ChunkAccess pChunk) {
        return wrapped.createBiomes(pExecutor, pRandomState, pBlender, pStructureManager, pChunk)
                .thenApplyAsync(chunk -> {
                    chunkDataGenerator.generate(chunk);
                    return chunk;
                }, Util.backgroundExecutor());
    }

    @Override
    @Nullable
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel pLevel, HolderSet<Structure> pStructure, BlockPos pPos, int pSearchRadius, boolean pSkipKnownStructures) {
        return wrapped.findNearestMapStructure(pLevel, pStructure, pPos, pSearchRadius, pSkipKnownStructures);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel pLevel, ChunkAccess pChunk, StructureManager pStructureManager) {
        wrapped.applyBiomeDecoration(pLevel, pChunk, pStructureManager);
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor pLevel) {
        return wrapped.getSpawnHeight(pLevel);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return wrapped.getBiomeSource();
    }

    @Override
    public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> pBiome, StructureManager pStructureManager, MobCategory pCategory, BlockPos pPos) {
        return wrapped.getMobsAt(pBiome, pStructureManager, pCategory, pPos);
    }

    @Override
    public void createStructures(RegistryAccess pRegistryAccess, ChunkGeneratorStructureState pStructureState, StructureManager pStructureManager, ChunkAccess pChunk, StructureTemplateManager pStructureTemplateManager) {
        final ChunkData data = chunkDataGenerator.generate(pChunk);
        chunkDataGenerator.saveForStructureUse(data);
        wrapped.createStructures(pRegistryAccess, pStructureState, pStructureManager, pChunk, pStructureTemplateManager);
    }

    @Override
    public void createReferences(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkAccess pChunk) {
        wrapped.createReferences(pLevel, pStructureManager, pChunk);
    }

    @Override
    public int getFirstFreeHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState pRandom) {
        return wrapped.getFirstFreeHeight(pX, pZ, pType, pLevel, pRandom);
    }

    @Override
    public int getFirstOccupiedHeight(int pX, int pZ, Heightmap.Types pTypes, LevelHeightAccessor pLevel, RandomState pRandom) {
        return wrapped.getFirstOccupiedHeight(pX, pZ, pTypes, pLevel, pRandom);
    }

    @Override
    public BiomeGenerationSettings getBiomeGenerationSettings(Holder<Biome> pBiome) {
        return wrapped.getBiomeGenerationSettings(pBiome);
    }
}
