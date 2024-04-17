package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface SewingSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<Integer[]> STITCHES = NumberComponent.intRange(0, 1).asArray().key("stitches");
    RecipeKey<Integer[]> SQUARES = NumberComponent.intRange(-1, 1).asArray().key("squares");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, STITCHES, SQUARES);
}
