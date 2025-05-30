package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dries007.tfc.common.items.TorchItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Currently, in {@link TorchItem#onEntityItemUpdate(ItemStack, ItemEntity)}, the block is hardcoded to TFC's torch block.
 * This makes it use the item's floor block to avoid having to reimplement the entirety of that method to use custom blocks
 */
@Mixin(value = TorchItem.class, remap = false)
public abstract class TorchItemMixin {

    @WrapOperation(method = "onEntityItemUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/RegistryObject;get()Ljava/lang/Object;"), remap = false)
    private <T> T kubejs_tfc$UseFloorBlock(RegistryObject<Block> instance, Operation<Block> original) {
        return (T) ((TorchItem) (Object) this).getBlock();
    }
}
