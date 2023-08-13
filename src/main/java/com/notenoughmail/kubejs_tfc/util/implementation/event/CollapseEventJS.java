package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class CollapseEventJS extends StartupEventJS {

    private final BlockContainerJS centerBlock;
    private final LevelJS level;
    private final double radiusSquared;
    private final List<BlockPos> secondaries;

    public CollapseEventJS(BlockPos center, List<BlockPos> secondaries, double radiusSquared, Level level) {
        this.level = KubeJS.PROXY.getLevel(level);
        centerBlock = this.level.getBlock(center);
        this.radiusSquared = radiusSquared;
        this.secondaries = secondaries;
    }

    public BlockContainerJS getCenterBlock() {
        return centerBlock;
    }

    public LevelJS getLevel() {
        return level;
    }

    public double getRadiusSquared() {
        return radiusSquared;
    }

    public List<BlockPos> getSecondaryPositions() {
        return secondaries;
    }
}
