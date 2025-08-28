package com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.schema;

import com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.js.DistilleryRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface DistillerySchema {

    RecipeKey<InputItem> INPUT_ITEM = ItemComponents.INPUT.key("input_item").preferred("inputItem").optional(InputItem.EMPTY);
    RecipeKey<FluidStackIngredient> INPUT_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("input_fluid").preferred("inputFluid").optional(FluidStackIngredient.EMPTY);
    RecipeKey<ItemStackProviderJS> RESULT_ITEM = ItemProviderComponent.PROVIDER.key("result_item").preferred("resultItem").optional(ItemStackProviderJS.EMPTY);
    RecipeKey<OutputFluid> RESULT_FLUID = FluidComponents.OUTPUT.key("result_fluid").preferred("resultFluid").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<ItemStackProviderJS> LEFTOVER_ITEM = ItemProviderComponent.PROVIDER.key("leftover_item").preferred("leftoverItem").optional(ItemStackProviderJS.EMPTY);
    RecipeKey<OutputFluid> LEFTOVER_FLUID = FluidComponents.OUTPUT.key("leftover_fluid").preferred("leftoverFluid").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<Integer> MIN_TEMP = NumberComponent.INT.key("min_temp").preferred("minTemp");
    RecipeKey<Integer> DURATION_TICKS = NumberComponent.INT.key("duration");

    RecipeSchema SCHEMA = new RecipeSchema(DistilleryRecipeJS.class, DistilleryRecipeJS::new, MIN_TEMP, DURATION_TICKS, INPUT_ITEM, INPUT_FLUID, RESULT_ITEM, RESULT_FLUID, LEFTOVER_ITEM, LEFTOVER_FLUID)
            .constructor(MIN_TEMP, DURATION_TICKS);

}
