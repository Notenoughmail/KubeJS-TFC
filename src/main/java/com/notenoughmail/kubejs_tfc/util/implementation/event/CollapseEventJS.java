package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.dries007.tfc.util.events.CollapseEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class CollapseEventJS extends LevelEventJS {

    private final BlockContainerJS centerBlock;
    private final Level level;
    private final double radiusSquared;
    private final List<BlockPos> secondaries;
    private final boolean isFake;

    public CollapseEventJS(CollapseEvent event) {
        level = event.getLevel();
        centerBlock = new BlockContainerJS(level, event.getCenterPos());
        radiusSquared = event.getRadiusSquared();
        secondaries = event.getNextPositions();
        isFake = event.isFake();
    }

    public BlockContainerJS getCenterBlock() {
        return centerBlock;
    }

    @Override
    public Level getLevel() {
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
