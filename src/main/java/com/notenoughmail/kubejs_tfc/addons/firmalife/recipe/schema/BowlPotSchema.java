package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema;

import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.component.FoodComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface BowlPotSchema {

    RecipeKey<OutputItem> ITEM_OUTPUT = ItemComponents.OUTPUT.key("item_output").preferred("itemOutput");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<FluidStackIngredient> FLUID_INGREDIENT = FluidIngredientComponent.STACK_INGREDIENT.key("fluid_ingredient").preferred("fluidIngredient");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");
    RecipeKey<Float> TEMPERATURE = NumberComponent.FLOAT.key("temperature");
    RecipeKey<FoodComponent.FoodData> FOOD = FoodComponent.COMPONENT.key("food");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, ITEM_OUTPUT, INGREDIENTS, FLUID_INGREDIENT, DURATION, TEMPERATURE, FOOD);
}
