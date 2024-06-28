package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface StompingSchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<String> PRE_TEXTURE = StringComponent.ID.key("input_texture").preferred("inputTexture");
    RecipeKey<String> POST_TEXTURE = StringComponent.ID.key("output_texture").preferred("outputTexture");
    RecipeKey<String> SOUND = StringComponent.ID.key("sound");

    RecipeSchema SCHEMA = new RecipeSchema(TFCProviderRecipeJS.class, TFCProviderRecipeJS::new, RESULT, INGREDIENT, PRE_TEXTURE, POST_TEXTURE, SOUND);
}
