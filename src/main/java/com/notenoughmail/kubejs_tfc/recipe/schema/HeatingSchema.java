package com.notenoughmail.kubejs_tfc.recipe.schema;

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

public interface HeatingSchema {

    RecipeKey<ItemStackProviderJS> ITEM_RESULT = ItemProviderComponent.PROVIDER.key("result_item").optional(ItemStackProviderJS.EMPTY);
    RecipeKey<OutputFluid> FLUID_RESULT = FluidComponents.OUTPUT.key("result_fluid").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<Double> TEMP = NumberComponent.DOUBLE.key("temperature");

    RecipeSchema SCHEMA = new RecipeSchema(ITEM_RESULT, FLUID_RESULT, INGREDIENT, TEMP)
            .constructor(ITEM_RESULT, INGREDIENT, TEMP)
            .constructor(FLUID_RESULT, INGREDIENT, TEMP)
            .constructor(INGREDIENT, TEMP);
}
