package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.HeatingRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface HeatingSchema {

    RecipeKey<ItemStackProviderJS> ITEM_RESULT = ItemProviderComponent.PROVIDER.key("result_item").optional(ItemStackProviderJS.EMPTY).preferred("resultItem");
    RecipeKey<OutputFluid> FLUID_RESULT = FluidComponents.OUTPUT.key("result_fluid").optional(EmptyFluidStackJS.INSTANCE).preferred("resultFluid");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<Float> TEMP = NumberComponent.FLOAT.key("temperature");
    RecipeKey<Boolean> USE_DURABILITY = BooleanComponent.BOOLEAN.key("use_durability").preferred("useDurability").optional(false);
    RecipeKey<Float> CHANCE = NumberComponent.floatRange(0F, 1F).key("chance").optional(1F);

    RecipeSchema SCHEMA = new RecipeSchema(HeatingRecipeJS.class, HeatingRecipeJS::new, INGREDIENT, TEMP, ITEM_RESULT, FLUID_RESULT, USE_DURABILITY, CHANCE)
            .constructor(INGREDIENT, TEMP);
}
