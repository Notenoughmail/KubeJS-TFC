package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import dev.latvian.mods.kubejs.item.InputItem;

public class LoomRecipeJS extends TFCProviderRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }
}
