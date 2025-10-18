package io.github.notenoughmail.kubejstfc.util.mixin;

import dev.latvian.mods.kubejs.gui.KubeJSGUI;
import dev.latvian.mods.kubejs.gui.KubeJSMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <b>Purpose:</b><p>
 * Prevents shift clicking from being able to add/remove items from {@link io.github.notenoughmail.kubejstfc.implementation.attachments.SealableInventoryAttachment sealed inventories}
 */
@Mixin(KubeJSMenu.class)
public abstract class KubeJSMenuMixin {

    @Shadow(remap = false)
    @Final
    public KubeJSGUI guiData;

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void kubejs_tfc$RespectInventoryMutability(Player player, int i, CallbackInfoReturnable<ItemStack> cir) {
        if (!guiData.inventory.kjs$isMutable()) cir.setReturnValue(ItemStack.EMPTY);
    }
}
