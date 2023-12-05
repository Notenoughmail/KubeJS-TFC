package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.NestedRecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface DelegateCraftingSchema {

    RecipeKey<RecipeJS> RECIPE = NestedRecipeComponent.RECIPE.key("recipe");

    static RecipeSchema schema(String type) {
        return new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RECIPE).uniqueId(recipe -> type + "/" + recipe.getPath());
    }
}
