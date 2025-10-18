package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.LayeredArea;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.layer.UniformLayer;
import net.dries007.tfc.world.layer.framework.SourceLayer;
import net.dries007.tfc.world.noise.Noise2D;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.function.DoubleToIntFunction;

public enum WorldBindings {
    INSTANCE;

    @Info("Creates a new LayeredArea from the SourceLayer")
    public LayeredArea layeredArea(SourceLayer source, long seed) {
        return new LayeredArea(source, seed);
    }

    @Info("Creates a new LayeredArea from a Noise2D object")
    public LayeredArea layeredAreaFromNoise(Noise2D noise, DoubleToIntFunction rounder, long seed) {
        return layeredArea((ctx, x, z) -> rounder.applyAsInt(noise.noise(x, z)), seed);
    }

    @Info("Creates a new LayeredArea from a Noise2D object")
    public LayeredArea layeredAreaFromNoise(Noise2D noise, long seed) {
        return layeredAreaFromNoise(noise, d -> (int) Math.round(d), seed);
    }

    @Info("Creates a new LayeredArea with values uniformly distributed across the 32-bit sign integer range")
    public LayeredArea uniformLayeredArea(long seed) {
        return layeredArea(UniformLayer.INSTANCE, seed);
    }

    @Info(value = "Creates a LerpFloatLayer, an interpolatable square of numbers at the corners of a square", params = {
            @Param(name = "value00", value = "The value at the [low x, low z] corner"),
            @Param(name = "value01", value = "The value at the [low x, high z] corner"),
            @Param(name = "value10", value = "The value at the [high x, low z] corner"),
            @Param(name = "value11", value = "The value at the [high x, high z] corner")
    })
    public LerpFloatLayer lerpFloatLayer(float value00, float value01, float value10, float value11) {
        return new LerpFloatLayer(value00, value01, value10, value11);
    }

    @Info("Gets TFC's ChunkData at the given position")
    public ChunkData getChunkData(LevelReader level, BlockPos pos) {
        return ChunkData.get(level, pos);
    }

    @Info("Gets TFC's ChunkData fro the given chunk")
    public ChunkData getChunkData(ChunkAccess chunk) {
        return ChunkData.get(chunk);
    }
}
