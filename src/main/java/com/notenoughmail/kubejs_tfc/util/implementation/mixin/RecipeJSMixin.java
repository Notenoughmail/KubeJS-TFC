package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.IRecipeJSExtension;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Left empty because RecipeJS  only needs to implement IRecipeJSExtension, not override any behavior there
 */
@Mixin(value = RecipeJS.class, remap = false)
public abstract class RecipeJSMixin implements IRecipeJSExtension {
}