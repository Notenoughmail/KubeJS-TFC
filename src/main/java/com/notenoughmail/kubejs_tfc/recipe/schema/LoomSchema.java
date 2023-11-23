package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.LoomRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface LoomSchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<Integer> REQUIRED_STEPS = NumberComponent.INT.key("steps_required").preferred("requiredSteps");
    RecipeKey<String> TEXTURE = StringComponent.ID.key("in_progress_texture").preferred("inProgressTexture");

    RecipeSchema SCHEMA = new RecipeSchema(LoomRecipeJS.class, LoomRecipeJS::new, RESULT, INGREDIENT, REQUIRED_STEPS, TEXTURE);
}
