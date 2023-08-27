package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.server.ServerEventJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class CollapseEventJS extends EventJS {

    private final BlockContainerJS centerBlock;
    private final LevelJS level;
    private final double radiusSquared;
    private final List<BlockPos> secondaries;
    private final boolean isFake;

    public CollapseEventJS(BlockPos center, List<BlockPos> secondaries, double radiusSquared, Level level, boolean isFake) {
        this.level = KubeJS.PROXY.getLevel(level);
        centerBlock = this.level.getBlock(center);
        this.radiusSquared = radiusSquared;
        this.secondaries = secondaries;
        this.isFake = isFake;
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

    public boolean isFake() {
        return isFake;
    }
}
