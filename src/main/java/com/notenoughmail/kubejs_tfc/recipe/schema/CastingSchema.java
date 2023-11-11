package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface CastingSchema {

    RecipeKey<InputItem> MOLD = ItemComponents.INPUT.key("mold");
    RecipeKey<FluidStackIngredientJS> FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("fluid");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<Double> BREAK_CHANCE = NumberComponent.doubleRange(0, 1).key("break_chance");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, MOLD, FLUID, BREAK_CHANCE);
}
