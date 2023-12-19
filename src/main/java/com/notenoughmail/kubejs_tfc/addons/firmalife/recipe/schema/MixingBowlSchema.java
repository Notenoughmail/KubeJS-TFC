package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema;

import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js.MixingBowlRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface MixingBowlSchema {

    RecipeKey<OutputItem> OUTPUT_ITEM = ItemComponents.OUTPUT.key("output_item").preferred("outputItem").optional(OutputItem.EMPTY);
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("output_fluid").preferred("outputFluid").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<FluidStackIngredient> FLUID_INGREDIENT = FluidIngredientComponent.STACK_INGREDIENT.key("fluid_ingredient").preferred("fluidIngredient").optional(FluidStackIngredient.EMPTY);
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT_ARRAY.key("ingredients").preferred("itemIngredients").optional(new InputItem[0]);

    RecipeSchema SCHEMA = new RecipeSchema(MixingBowlRecipeJS.class, MixingBowlRecipeJS::new, OUTPUT_ITEM, OUTPUT_FLUID, INGREDIENTS, FLUID_INGREDIENT)
            .constructor();
}
