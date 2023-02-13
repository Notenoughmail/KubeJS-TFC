package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class FallingBlockRecipeJS extends TFCRecipeJS {

    private String result;
    private String ingredient;
    private boolean copy;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 1) {
            throw new RecipeExceptionJS("Requires at least 1 argument - result");
        }

        result = listJS.get(0).toString();

        if (listJS.size() < 2) {
            copy = !result.matches(".+\\[.+\\]");
            ingredient = result.replaceAll("\\[.+\\]", "");
        } else {
            copy = false;
            ingredient = listJS.get(1).toString();
        }

        json.addProperty("ingredient", ingredient);
        json.addProperty("copy_input", copy);
        json.addProperty("result", result);
    }

    @Override
    public void deserialize() {
        ingredient = json.get("ingredient").getAsString();
        result = json.get("result").getAsString();
        copy = getOptionalBoolMember(json, "copy_input", false);
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", result);
        }

        if (serializeInputs) {
            json.addProperty("ingredient", ingredient);
        }
    }
}
