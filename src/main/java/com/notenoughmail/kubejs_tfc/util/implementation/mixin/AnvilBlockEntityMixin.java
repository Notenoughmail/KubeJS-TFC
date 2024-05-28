package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.block.AnvilBlockBuilder;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilBlockEntity.class, remap = false)
public class AnvilBlockEntityMixin extends InventoryBlockEntity<AnvilBlockEntity.AnvilInventory> {

    @SuppressWarnings("unused")
    private AnvilBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<AnvilBlockEntity.AnvilInventory> inventoryFactory, Component defaultName) {
        super(type, pos, state, inventoryFactory, defaultName);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/dries007/tfc/common/blockentities/InventoryBlockEntity$InventoryFactory;Lnet/minecraft/network/chat/Component;)V", remap = false, at = @At("TAIL"))
    private void kubejs_tfc$CustomTitle(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<AnvilBlockEntity.AnvilInventory> inventoryFactory, Component defaultName, CallbackInfo ci) {
        if (state.getBlock() instanceof AnvilBlockBuilder.AnvilBlockJS js && js.getDefaultName() != null) {
            this.defaultName = js.getDefaultName();
        }
    }
}
