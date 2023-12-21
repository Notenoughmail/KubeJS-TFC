package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.block.LampBlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.items.LampBlockItem;
import net.dries007.tfc.util.loot.CopyFluidFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LampBlockItemBuilder extends BlockItemBuilder {

    public transient LampBlockBuilder blockBuilder;

    public LampBlockItemBuilder(ResourceLocation i, LampBlockBuilder blockBuilder) {
        super(i);
        this.blockBuilder = blockBuilder;
    }

    @Override
    public Item createObject() {
        return new LampBlockItem(blockBuilder.get(), createItemProperties()) {
            @Override
            protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
                CopyFluidFunction.copyFromItem(stack, level.getBlockEntity(pos, LampBlockBuilder.be.get()).orElse(null));
                return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
            }
        };
    }
}
