package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomCropBlock;
import net.dries007.tfc.common.blocks.crop.CropHelpers;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Allow custom crops to specify their growth and expiry time
 */
@Mixin(value = CropHelpers.class, remap = false)
public abstract class CropHelpersMixin {

    @WrapOperation(method = "growthTickStep", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0), remap = false)
    private static float kubejs_tfc$ModifyGrowthRate(Double instance, Operation<Float> original, @Local(argsOnly = true) BlockState state) {
        float val = original.call(instance);
        if (state.getBlock() instanceof ICustomCropBlock custom) {
            val *= custom.growthModifier();
        }
        return val;
    }

    @WrapOperation(method = "growthTickStep", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1), remap = false)
    private static float kubejs_tfc$ModifyExpiryRate(Double instance, Operation<Float> original, @Local(argsOnly = true) BlockState state) {
        float val = original.call(instance);
        if (state.getBlock() instanceof ICustomCropBlock custom) {
            val *= custom.expiryModifier();
        }
        return val;
    }
}
