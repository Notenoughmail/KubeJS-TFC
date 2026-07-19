package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.notenoughmail.kubejstfc.implementation.bindings.FoodBindings;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodComponent;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodComponent.class)
public abstract class FoodComponentMixin {

    @Shadow
    private long creationDate;

    @Inject(method = "capture", at = @At("TAIL"))
    private void kubejs_tfc$RecipeNonDecayCapture(ItemStack stack, CallbackInfo ci) {
        if (creationDate == FoodBindings.RECIPE_TRANSIENT_NON_DECAY_FLAG) {
            FoodCapability.setTransientNonDecaying(stack);
        }
    }

    @Mixin(FoodCapability.class)
    public static abstract class FoodCapabilityMixin {

        @Expression("? == -1")
        @ModifyExpressionValue(
                method = "roundCreationDate",
                at = @At("MIXINEXTRAS:EXPRESSION")
        )
        private static boolean kubejs_tfc$RecipeNonDecayRoundCreationDate(
                boolean original,
                @Local(type = FoodComponent.class, name = "food") FoodComponent food
        ) {
            return original || food.getCreationDate() == FoodBindings.RECIPE_TRANSIENT_NON_DECAY_FLAG;
        }

        @Inject(method = "isRotten(JF)Z", at = @At("HEAD"), cancellable = true)
        private static void kubejs_tfc$RecipeNonDecayIsRotten(
                long creationDate,
                float decayDateModifier,
                CallbackInfoReturnable<Boolean> cir
        ) {
            if (creationDate == FoodBindings.RECIPE_TRANSIENT_NON_DECAY_FLAG) {
                cir.setReturnValue(false);
            }
        }
    }

    @Mixin(IFood.class)
    public interface IFoodMixin {

        @Definition(
                id = "creationDate",
                local = @Local(type = long.class, name = "creationDate")
        )
        @Expression("creationDate != -1")
        @ModifyExpressionValue(
                method = "addTooltipInfo",
                at = @At("MIXINEXTRAS:EXPRESSION")
        )
        private boolean kubejs_tfc$RecipeNonDecayAddTooltipInfo(
                boolean original,
                @Local(type = long.class, name = "creationDate") long creationDate
        ) {

            return original && creationDate != FoodBindings.RECIPE_TRANSIENT_NON_DECAY_FLAG;
        }
    }
}
