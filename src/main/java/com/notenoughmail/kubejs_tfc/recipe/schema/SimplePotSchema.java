package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.SimplePotRecipeJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface SimplePotSchema {

    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients");
    RecipeKey<FluidStackIngredient> FLUID_INGREDIENT = FluidIngredientComponent.STACK_INGREDIENT.key("fluid_ingredient").preferred("fluidIngredient");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");
    RecipeKey<Float> TEMPERATURE = NumberComponent.FLOAT.key("temperature");
    RecipeKey<OutputFluid> FLUID_OUTPUT = FluidComponents.OUTPUT.key("fluid_output").preferred("fluidOutput").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<OutputItem[]> ITEM_OUTPUT = ItemComponents.OUTPUT_ARRAY.key("item_output").preferred("itemOutput").optional(new OutputItem[0]);

    RecipeSchema SCHEMA = new RecipeSchema(SimplePotRecipeJS.class, SimplePotRecipeJS::new, INGREDIENTS, FLUID_INGREDIENT, DURATION, TEMPERATURE, ITEM_OUTPUT, FLUID_OUTPUT)
            .constructor(INGREDIENTS, FLUID_INGREDIENT, DURATION, TEMPERATURE);
}
