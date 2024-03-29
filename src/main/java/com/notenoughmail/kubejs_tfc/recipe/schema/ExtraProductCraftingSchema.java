package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NestedRecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ExtraProductCraftingSchema {

    RecipeKey<RecipeJS> RECIPE = new NestedRecipeComponent().key("recipe");
    RecipeKey<OutputItem[]> EXTRAS = ItemComponents.OUTPUT_ARRAY.key("extra_products").preferred("extraProducts");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, EXTRAS, RECIPE);
}
