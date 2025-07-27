package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkRockDataCache;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class CreateChunkDataProviderEventJS extends EventJS {

    public transient RocksGetter generateRock;
    public transient BiConsumer<ChunkData, ChunkAccess> generatePartial, generateFull;
    public transient Function<ChunkAccess, Aquifer> createAquifer;

    private final long seed;
    private final RandomState rs;

    public CreateChunkDataProviderEventJS(long seed, RandomState rs) {
        this.seed = seed;
        this.rs = rs;
    }

    @Info("Returns the seed for the world the chunk data provider is being applied to")
    public long getWorldSeed() {
        return seed;
    }

    @Info("Returns the normal noise defined by the noise parameters with the given id")
    public NormalNoise getNormalNoise(ResourceLocation id) {
        return rs.getOrCreateNoise(ResourceKey.create(Registries.NOISE, id));
    }

    @Info("""
            Sets the partial calculation for a chunk's `ChunkData`.
            
            `ChunkData#generatePartial` should be called here.
            
            `ChunkData#generateFull` may be called here, but heightmap access is not guaranteed during this callback.
            
            Defaults to filling the chunk with 0s.
            
            For a full explanation, see the wiki.
            """)
    @Generics({ ChunkData.class, ChunkAccess.class })
    public void partial(BiConsumer<ChunkData, ChunkAccess> gen) {
        generatePartial = gen;
    }

    @Info("""
            Sets the full calculation for a chunk's `ChunkData`.
            
            `ChunkData#generateFull` should be called here.
            
            Heightmap access is available here.
            
            Defaults to filling `surfaceHeight` with the `OCEAN_FLOOR_WG` heightmap and `aquiferSurfaceHeight` with values 20 less than the average height of the quart.
            
            For a full explanation, see the wiki.
            """)
    @Generics({ ChunkData.class, ChunkAccess.class })
    public void full(BiConsumer<ChunkData, ChunkAccess> gen) {
        generateFull = gen;
    }

    @Info("""
            Sets the aquifer used in the chunk generator. Does not affect world generation without intervention, but some TFC features (erosion) may use it.
            
            Defaults to creating an aquifer filled with air at -2^31
            """)
    public void erosionalAquifer(AquiferMaker maker) {
        createAquifer = maker;
    }

    @Info("""
            Sets the rock settings generator. Does not affect world generation without intervention, but some TFC features (boulders, erosion, fissure) may use this for selecting blocks.
            
            Defaults to returning an empty `RockSettings` made entirely of air
            """)
    public void rocks(RocksGetter rocksGetter) {
        generateRock = rocksGetter;
    }

    @FunctionalInterface
    public interface RocksGetter {
        @Info(params = {
                @Param(name = "x", value = "The x coordinate"),
                @Param(name = "y", value = "The y coordinate"),
                @Param(name = "z", value = "The z coordinate"),
                @Param(name = "surfaceY", value = "The y value of the surface"),
                @Param(name = "cache", value = "A nullable cache of rock layer elevations"),
                @Param(name = "rockLayers", value = "The rock settings defined in the generator")
        })
        @Nullable
        RockSettings generate(int x, int y, int z, int surfaceY, @Nullable ChunkRockDataCache cache, RockLayerSettings rockLayers);
    }

    @FunctionalInterface
    public interface AquiferMaker extends Function<ChunkAccess, Aquifer> {
        Aquifer apply(ChunkAccess access);
    }
}
