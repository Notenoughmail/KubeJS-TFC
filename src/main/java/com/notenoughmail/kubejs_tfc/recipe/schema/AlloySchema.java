package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface AlloySchema {

    RecipeKey<AlloyPartComponent.AlloyPart[]> CONTENTS = AlloyPartComponent.ALLOY.key("contents");
    RecipeKey<String> RESULT = StringComponent.ID.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, CONTENTS);
}
