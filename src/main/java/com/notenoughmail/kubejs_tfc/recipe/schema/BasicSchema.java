package com.notenoughmail.kubejs_tfc.recipe.schema;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface BasicSchema {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT);
}
