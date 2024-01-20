package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.typings.Info;
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

    @Info(value = "Returns the maximum distance from the center block of collapsing blocks, or 0 if the collapse is fake")
    public double getRadiusSquared() {
        return radiusSquared;
    }

    @Info(value = "Returns a list of `BlockPos`es where a block collapses or, if the collapse is fake, particles spawn")
    public List<BlockPos> getSecondaryPositions() {
        return secondaries;
    }

    public boolean isFake() {
        return isFake;
    }
}
