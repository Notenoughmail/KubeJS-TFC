package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface CastingSchema {

    RecipeKey<InputItem> MOLD = ItemComponents.INPUT.key("mold");
    RecipeKey<FluidStackIngredient> FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("fluid");
    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<Double> BREAK_CHANCE = NumberComponent.doubleRange(0, 1).key("break_chance").preferred("breakChance");

    RecipeSchema SCHEMA = new RecipeSchema(TFCProviderRecipeJS.class, TFCProviderRecipeJS::new, RESULT, MOLD, FLUID, BREAK_CHANCE);
}
