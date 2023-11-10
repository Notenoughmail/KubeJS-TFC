package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartsComponent;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.dries007.tfc.common.recipes.AlloyRecipe;

public interface AlloySchema {

    RecipeKey<TinyMap<String, AlloyRecipe.Range>> CONTENTS = AlloyPartsComponent.ALLOY.key("contents");
    RecipeKey<String> RESULT = StringComponent.ID.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, CONTENTS);
}
