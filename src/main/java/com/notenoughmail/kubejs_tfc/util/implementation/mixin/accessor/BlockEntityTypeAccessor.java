package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeAccessor {

    @Accessor("validBlocks")
    Set<Block> kubejs_tfc$GetBlocks();

    @Accessor("validBlocks")
    @Mutable
    void kubejs_tfc$SetBlocks(Set<Block> blocks);
}
