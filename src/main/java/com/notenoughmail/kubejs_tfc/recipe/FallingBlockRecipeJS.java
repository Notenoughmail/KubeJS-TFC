package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class FallingBlockRecipeJS extends TFCRecipeJS {

    private boolean copy = true; // TFC's default is false, but this makes more sense for this implementation

    // The cardinal sin, not putting the result first
    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 1) {
            throw new RecipeExceptionJS("Requires at least 1 argument - ingredient");
        }

        // Block ingredient implementation, should probably be a wrapper
        var ingredients = ListJS.of(listJS.get(0));
        if (ingredients.size() < 2) {
            blockIngredient.add(ingredients.get(0).toString());
        } else {
            for (var ingred : ingredients) {
                if (ingred instanceof JsonObject json) {
                    blockIngredient.add(json);
                } else if (ingred.toString().matches("#.+")) {
                    var tag = new JsonObject();
                    tag.addProperty("tag", ingred.toString().replaceFirst("#", ""));
                    blockIngredient.add(tag);
                } else {
                    blockIngredient.add(ingred.toString());
                }
            }
        }

        if (listJS.size() > 1) {
            if (listJS.get(1) instanceof Boolean bool) {
                copy = bool;
            } else {
                result = listJS.get(1).toString();
                copy = false;
            }
        }
    }

    @Override
    public void deserialize() {
        blockIngredient = json.get("ingredient").getAsJsonArray();
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
            json.add("ingredient", blockIngredient);
            json.addProperty("copy_input", copy);
        }

        System.out.println(json);
    }
}
