package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface SewingSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<Integer[]> STITCHES = NumberComponent.intRange(0, 1).asArray().key("stitches"); // 0: No stitch, 1: Stitch
    RecipeKey<Integer[]> SQUARES = NumberComponent.intRange(-1, 1).asArray().key("squares"); // -1: No cloth, 0: Dark cloth, 1: Light cloth

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, STITCHES, SQUARES).uniqueOutputId(RESULT);
}
