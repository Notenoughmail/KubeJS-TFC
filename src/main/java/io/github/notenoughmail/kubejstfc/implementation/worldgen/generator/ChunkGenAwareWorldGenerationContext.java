package io.github.notenoughmail.kubejstfc.implementation.worldgen.generator;

import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

public class ChunkGenAwareWorldGenerationContext extends WorldGenerationContext {

    public final ChunkGenerator chunkGenerator;

    public ChunkGenAwareWorldGenerationContext(ChunkGenerator pGenerator, LevelHeightAccessor pLevel) {
        super(pGenerator, pLevel);
        chunkGenerator = pGenerator;
    }
}
