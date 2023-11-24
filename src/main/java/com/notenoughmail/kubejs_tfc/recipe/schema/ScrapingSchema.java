package com.notenoughmail.kubejs_tfc.recipe.schema;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ScrapingSchema {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<String> INPUT_TEXTURE = StringComponent.ID.key("input_texture").preferred("inputTexture");
    RecipeKey<String> OUTPUT_TEXTURE = StringComponent.ID.key("output_texture").preferred("outputTexture");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, OUTPUT_TEXTURE, INPUT_TEXTURE);
}
