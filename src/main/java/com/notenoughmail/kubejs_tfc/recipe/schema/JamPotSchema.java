package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface JamPotSchema {

    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<FluidStackIngredient> FLUID_INGREDIENT = FluidIngredientComponent.STACK_INGREDIENT.key("fluid_ingredient").preferred("fluidIngredient");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");
    RecipeKey<Float> TEMPERATURE = NumberComponent.FLOAT.key("temperature");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<String> TEXTURE = StringComponent.ID.key("texture");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, INGREDIENTS, FLUID_INGREDIENT, DURATION, TEMPERATURE, TEXTURE);
}
