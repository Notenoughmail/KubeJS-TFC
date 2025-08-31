package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface ILeafBuilder extends Supplier<Block> {

    @HideFromJS
    boolean seasonalColors();

    @HideFromJS
    int autumnIndex();

    @HideFromJS
    default boolean isFallen() { return false; }

    default ILeafBuilder noDynamicTinting() {
        BuilderRefs.leafColors.remove(this);
        return this;
    }
}
