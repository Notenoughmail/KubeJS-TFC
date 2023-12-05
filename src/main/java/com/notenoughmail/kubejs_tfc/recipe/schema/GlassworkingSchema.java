package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;

public interface GlassworkingSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<InputItem> BATCH = ItemComponents.INPUT.key("batch");
    RecipeKey<GlassOperation[]> OPERATIONS = new EnumComponent<>(GlassOperation.class).asArray().key("operations");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, RESULT, BATCH, OPERATIONS);
}
