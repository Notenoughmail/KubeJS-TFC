package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.KnappingRecipeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface KnappingSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
    RecipeKey<String> TYPE = StringComponent.ID.key("knapping_type").preferred("knappingType");
    RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient").optional(InputItem.EMPTY);
    RecipeKey<Boolean> OUTSIDE_REQUIRED = BooleanComponent.BOOLEAN.key("outside_slot_required").optional(true).preferred("outsideSlotRequired");

    RecipeSchema SCHEMA = new RecipeSchema(KnappingRecipeJS.class, KnappingRecipeJS::new, RESULT, TYPE, PATTERN, INGREDIENT, OUTSIDE_REQUIRED)
            .constructor(RESULT, TYPE, PATTERN);
}
