package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.TorchItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Currently, in {@link TorchItem#onEntityItemUpdate(ItemStack, ItemEntity)}, the block is hardcoded to TFC's torch block.
 * This makes it use the item's floor block to avoid having to reimplement the entirety of that method to use custom blocks
 */
@Mixin(TorchItem.class)
public abstract class TorchItemMixin {

    @WrapOperation(method = "onEntityItemUpdate", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/common/blocks/TFCBlocks$Id;get()Ljava/lang/Object;"))
    private <T extends Block> Object kubejs_tfc$UseFloorBlock(TFCBlocks.Id<T> instance, Operation<T> original) {
        return ((TorchItem) (Object) this).getBlock();
    }
}
