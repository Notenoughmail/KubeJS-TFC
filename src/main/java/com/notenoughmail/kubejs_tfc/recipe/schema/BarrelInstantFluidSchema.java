package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface BarrelInstantFluidSchema {

    RecipeKey<FluidStackIngredient> PRIMARY_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("primary_fluid").preferred("primaryFluid");
    RecipeKey<FluidStackIngredient> ADDED_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("added_fluid").preferred("addedFluid");
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("output_fluid").preferred("outputFluid");
    RecipeKey<String> SOUND = StringComponent.ID.key("sound").optional("minecraft:block.brewing_stand.brew");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT_FLUID, PRIMARY_FLUID, ADDED_FLUID, SOUND);
}
