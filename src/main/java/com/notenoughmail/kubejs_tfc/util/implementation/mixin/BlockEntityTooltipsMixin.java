package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomTorchBlock;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltips;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

/**
 * <b>Purpose:</b><p>
 * Allows Jade to show the correct duration for custom torch blocks
 */
@Mixin(value = BlockEntityTooltips.class, remap = false)
public abstract class BlockEntityTooltipsMixin {

    @WrapOperation(method = "lambda$tickCounter$31", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"), remap = false)
    private static <T> T kubejs_tfc$CustomTorchDuration(Supplier<T> instance, Operation<T> original, @Local(argsOnly = true) BlockState state) {
        if (state.getBlock() instanceof ICustomTorchBlock c) {
            return (T) (Object) c.getTotalTicks();
        }
        return original.call(instance);
    }
}
