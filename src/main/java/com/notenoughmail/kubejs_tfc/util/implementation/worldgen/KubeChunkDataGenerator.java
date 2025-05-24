package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.CreateChunkDataProviderEventJS;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import net.dries007.tfc.world.chunkdata.*;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class KubeChunkDataGenerator implements ChunkDataGenerator {

    private static final LerpFloatLayer EMPTY_LERP = new LerpFloatLayer(0F, 0F, 0F, 0F);
    private static final RockSettings EMPTY_ROCK = new RockSettings(Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Blocks.AIR, Optional.empty(), Optional.empty(), Optional.empty());

    private static final BiConsumer<ChunkData, ChunkAccess>
            GEN_PARTIAL = (chunkData, access) -> chunkData.generatePartial(
                    EMPTY_LERP,
                    EMPTY_LERP,
                    ForestType.NONE,
                    0.0F,
                    0.0F
            ),
            GEN_FULL = (chunkData, access) -> {
                final int[][][] data = new int[4][4][16];
                final int[] elevations = new int[16 * 16];
                final int[] aquifer = new int[4 * 4];
                for (int x = 0 ; x < 16 ; x++) {
                    for (int z = 0 ; z < 16 ; z++) {
                        final int height = access.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                        elevations[x + 16 * z] = height;
                        data[x / 4][z / 4][(x & 0b11) | ((z & 0b11) << 2)] = height;
                    }
                }
                for (int x = 0 ; x < 4 ; x++) {
                    for (int z = 0 ; z < 4 ; z++) {
                        aquifer[x + 4 * z] = avg(data[x][z]) - 20;
                    }
                }
                chunkData.generateFull(
                        elevations,
                        aquifer
                );
            };
    private static final Aquifer.FluidStatus DEFAULT_AQUIFER_STATUS = new Aquifer.FluidStatus(Integer.MIN_VALUE, Blocks.WATER.defaultBlockState());
    private static final Function<ChunkAccess, Aquifer> AQUIFER = access -> Aquifer.createDisabled((x, y, z) -> DEFAULT_AQUIFER_STATUS);
    private static final CreateChunkDataProviderEventJS.RocksGetter ROCK = (x, y, z, surfaceY, cache, rocks) -> EMPTY_ROCK;

    private static int avg(int[] a) {
        int sum = 0;
        for (int x : a) sum += x;
        return sum / a.length;
    }

    public static KubeChunkDataGenerator create(String levelKey, RockLayerSettings rockLayers) {
        BiConsumer<ChunkData, ChunkAccess> partial = GEN_PARTIAL, full = GEN_FULL;
        Function<ChunkAccess, Aquifer> aquifer = AQUIFER;
        CreateChunkDataProviderEventJS.RocksGetter rock = ROCK;
        if (EventHandlers.createChunkDataProvider.hasListeners(levelKey)) {
            final CreateChunkDataProviderEventJS event = new CreateChunkDataProviderEventJS();
            EventHandlers.createChunkDataProvider.post(event, levelKey);
            if (event.generatePartial != null) partial = event.generatePartial;
            if (event.generateFull != null) full = event.generateFull;
            if (event.createAquifer != null) aquifer = event.createAquifer;
            if (event.generateRock != null) rock = event.generateRock;
        }

        return new KubeChunkDataGenerator(levelKey, partial, full, aquifer, rock, rockLayers);
    }

    private final String key;
    private final ChunkDataProvider provider;
    private final BiConsumer<ChunkData, ChunkAccess> genPartial, genFull;
    private final Function<ChunkAccess, Aquifer> createAquifer;
    private final CreateChunkDataProviderEventJS.RocksGetter rock;
    private final RockLayerSettings rockLayers;

    private KubeChunkDataGenerator(
            String key,
            BiConsumer<ChunkData, ChunkAccess> genPartial,
            BiConsumer<ChunkData, ChunkAccess> genFull,
            Function<ChunkAccess, Aquifer> createAquifer,
            CreateChunkDataProviderEventJS.RocksGetter rock,
            RockLayerSettings rockLayers
    ) {
        this.key = key;
        provider = new ChunkDataProvider(this);
        this.genPartial = genPartial;
        this.genFull = genFull;
        this.createAquifer = createAquifer;
        this.rock = rock;
        this.rockLayers = rockLayers;
    }

    public ChunkDataProvider provider() {
        return provider;
    }

    public ChunkData get(ChunkAccess chunk) {
        return provider.get(chunk);
    }

    public Aquifer makeAquifer(ChunkAccess access) {
        return createAquifer.apply(access);
    }

    @Override
    public void generate(ChunkData data) {}

    public void generatePartialIfNot(ChunkData data, ChunkAccess access) {
        if (data.status() == ChunkData.Status.PARTIAL || data.status() == ChunkData.Status.FULL) return;
        try {
            genPartial.accept(data, access);
        } catch (Exception e) {
            KubeJSTFC.error("Error during partial chunk data creation for %s".formatted(key), e);
        }
        if (data.status() == ChunkData.Status.EMPTY) GEN_PARTIAL.accept(data, access);
    }

    public void generateFullIfNot(ChunkData data, ChunkAccess access) {
        if (data.status() != ChunkData.Status.PARTIAL) return;
        try {
            genFull.accept(data, access);
        } catch (Exception e) {
            KubeJSTFC.error("Error during full chunk data creation for %s".formatted(key), e);
        }
        if (data.status() == ChunkData.Status.PARTIAL) GEN_FULL.accept(data, access);
    }

    @Override
    public RockSettings generateRock(int x, int y, int z, int surfaceY, @Nullable ChunkRockDataCache cache) {
        final RockSettings r = rock.generate(x, y, z, surfaceY, cache, rockLayers);
        return r == null ? EMPTY_ROCK : r;
    }

    @Override
    public void displayDebugInfo(List<String> tooltip, BlockPos pos, int surfaceY) {
        tooltip.add(toString());
        tooltip.add("- [%d, %d, %d]".formatted(pos.getX(), pos.getY(), pos.getZ()));
        if (rock != ROCK) {
            tooltip.add("- %s".formatted(ForgeRegistries.BLOCKS.getKey(generateRock(pos.getX(), pos.getY(), pos.getZ(), surfaceY, null).raw())));
        }
    }

    @Override
    public String toString() {
        return "KubeChunkDataGenerator[" + key + "]";
    }
}
