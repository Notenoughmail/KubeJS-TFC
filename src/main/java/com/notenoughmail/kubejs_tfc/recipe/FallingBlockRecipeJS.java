package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class FallingBlockRecipeJS extends TFCRecipeJS {

    private boolean copy = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 1) {
            throw new RecipeExceptionJS("Requires at least 1 argument - result");
        }

        result = listJS.get(0).toString();

        /*
         * This makes it so the copy_input member is true when
         * only the result is provided, in addition to setting
         * the ingredient to be the input but stripped of
         * block properties.
         */
        if (listJS.size() < 2) {
            copy = !result.matches(".+\\[.+\\]");
            ingredient = result.replaceAll("\\[.+\\]", "");
        } else {
            ingredient = listJS.get(1).toString();
        }
    }

    @Override
    public void deserialize() {
        ingredient = json.get("ingredient").getAsString();
        result = json.get("result").getAsString();
        if (json.has("copy_input")) {
            copy = json.get("copy_input").getAsBoolean();
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", result);
        }

        if (serializeInputs) {
            json.addProperty("ingredient", ingredient);
            json.addProperty("copy_input", copy);
        }
    }
}
