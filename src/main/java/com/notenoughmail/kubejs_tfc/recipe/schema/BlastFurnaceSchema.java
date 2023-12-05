package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface BlastFurnaceSchema {

    RecipeKey<FluidStackIngredient> FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("fluid");
    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst");
    RecipeKey<OutputFluid> RESULT = FluidComponents.OUTPUT.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, CATALYST, FLUID);
}
