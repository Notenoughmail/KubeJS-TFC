package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AlloyRecipeJS extends TFCRecipeJS {

    private JsonArray contents = new JsonArray();
    private String resultMetal;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and contents");
        }

        resultMetal = listJS.get(0).toString();

        contents = ListJS.of(listJS.get(1)).toJson().getAsJsonArray();

        if (contents.size() < 2) {
            throw new RecipeExceptionJS("Contents must have at least two members");
        }
    }


    @Override
    public void deserialize() {
        resultMetal = json.get("result").toString();
        contents.add(json.get("contents"));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", resultMetal);
        }

        if (serializeInputs) {
            json.add("contents", contents);
        }
    }
}
