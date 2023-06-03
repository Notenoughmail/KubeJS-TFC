package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.level.BlockContainerJS;

@FunctionalInterface
public interface MossGrowingCallback {
    boolean convertToMossy(BlockContainerJS container, boolean needsWater);
}
