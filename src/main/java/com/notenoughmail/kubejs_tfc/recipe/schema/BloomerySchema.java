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

public interface BloomerySchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst");
    RecipeKey<FluidStackIngredient> FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("fluid");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");

    RecipeSchema SCHEMA = new RecipeSchema(TFCProviderRecipeJS.class, TFCProviderRecipeJS::new, RESULT, CATALYST, FLUID, DURATION);
}
