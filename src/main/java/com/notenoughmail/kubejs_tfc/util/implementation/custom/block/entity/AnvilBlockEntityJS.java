package com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity;

import com.notenoughmail.kubejs_tfc.block.AnvilBlockBuilder;
import com.notenoughmail.kubejs_tfc.menu.AnvilMenuBuilder;
import com.notenoughmail.kubejs_tfc.menu.AnvilPlanMenuBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.AnvilBlockJS;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AnvilBlockEntityJS extends AnvilBlockEntity {

    private static Component getDefaultName(BlockState state) {
        return state.getBlock() instanceof AnvilBlockJS js ? js.getDefaultName() : AnvilBlockBuilder.DEFAULT_NAME;
    }

    public AnvilBlockEntityJS(BlockPos pos, BlockState state) {
        super(RegistryUtils.getAnvil().get(), pos, state, AnvilInventory::new, getDefaultName(state));
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return AnvilMenuBuilder.create(this, inventory, containerId);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createPlanContainer(int containerId, Inventory inventory, Player player) {
        return AnvilPlanMenuBuilder.create(this, inventory, containerId);
    }
}
