package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.recipe.InputReplacement;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = IngredientType.class, remap = false)
public interface IngredientTypeMixin extends InputReplacement {
}
