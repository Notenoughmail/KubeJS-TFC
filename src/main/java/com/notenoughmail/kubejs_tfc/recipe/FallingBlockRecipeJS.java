package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class FallingBlockRecipeJS extends TFCRecipeJS {

    public boolean copy = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and block ingredient");
        }

        if (listJS.get(0) instanceof Boolean bool) {
            copy = bool;
        } else {
            result = listJS.get(0).toString();
        }

        blockIngredient = BlockIngredientJS.of(listJS.get(1));
    }

    @Override
    public void deserialize() {
        blockIngredient = BlockIngredientJS.fromJson(json.get("ingredient"));
        if (json.has("result")) {
            result = json.get("result").getAsString();
        }
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
            json.add("ingredient", blockIngredient.toJson());
            json.addProperty("copy_input", copy);
        }
    }

    @Override
    public String getFromToString() {
        if (copy) {
            return blockIngredient + " -> itself";
        }
        return blockIngredient + " -> " + result;
    }
}
