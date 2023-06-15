package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeFilter.class, remap = false)
public interface RecipeFilterMixin {

    @Inject(method = "of(Ljava/lang/Object;)Ldev/latvian/mods/kubejs/recipe/filter/RecipeFilter;", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void kubejs_tfc$recipeFilterOf(Object o, CallbackInfoReturnable<RecipeFilter> cir) {
        if (o instanceof RecipeFilter filter) {
            cir.setReturnValue(filter); // Why Kube doesn't have this natively is beyond me
        }
    }
}