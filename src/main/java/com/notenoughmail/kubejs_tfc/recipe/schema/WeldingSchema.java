package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface WeldingSchema {

    RecipeKey<InputItem> FIRST_INPUT = ItemComponents.INPUT.key("first_input").preferred("firstInput");
    RecipeKey<InputItem> SECOND_INPUT = ItemComponents.INPUT.key("second_input").preferred("secondInput");
    RecipeKey<Integer> TIER = NumberComponent.INT.key("tier").optional(-1);
    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, FIRST_INPUT, SECOND_INPUT, TIER)
            .constructor(RESULT, FIRST_INPUT, SECOND_INPUT);
}
