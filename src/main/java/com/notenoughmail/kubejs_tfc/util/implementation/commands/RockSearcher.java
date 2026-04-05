package com.notenoughmail.kubejs_tfc.util.implementation.commands;

import net.dries007.tfc.world.chunkdata.ChunkDataGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public record RockSearcher(
        int radius,
        int sampleSpacing,
        int elevation,
        ChunkDataGenerator generator,
        Block raw,
        BlockPos origin
) {

    private boolean test(int x, int z) {
        return generator.generateRock(x, elevation, z, 72, null).raw() == raw;
    }

    private BlockPos pos(int x, int z) {
        return new BlockPos(x, elevation, z);
    }

    @Nullable
    public BlockPos find() {
        for (int r = sampleSpacing; r < radius; r += sampleSpacing) {
            for (int x = origin.getX() - r; x < origin.getX() + r; x += sampleSpacing) {
                int z = origin.getZ() - r;
                if (test(x, z)) {
                    return pos(x, z);
                }
                z = origin.getZ() + r;
                if (test(x, z)) {
                    return pos(x, z);
                }
            }
            for (int z = origin.getZ() - r; z < origin.getZ() + r; z += sampleSpacing) {
                int x = origin.getX() - r;
                if (test(x, z)) {
                    return pos(x, z);
                }
                x = origin.getX() + r;
                if (test(x, z)) {
                    return pos(x, z);
                }
            }
        }
        return null;
    }
}
