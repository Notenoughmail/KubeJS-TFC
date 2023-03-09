package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ChiselRecipeJS extends TFCRecipeJS {

    private String mode;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least 2 arguments - result and ingredient");
        }

        result = listJS.get(0).toString();

        ingredient = listJS.get(1).toString();

        if (listJS.size() > 2) {
            mode = listJS.get(2).toString();
        } else {
            mode = "smooth";
        }

        if (listJS.size() > 3) {
            outputItems.add(parseResultItem(listJS.get(3)));
        }

        if (listJS.size() > 4) {
            inputItems.addAll(parseIngredientItemList(listJS.get(4)));
        }
    }

    @Override
    public void deserialize() {
        if (json.has("extra_drop")) {
            outputItems.add(parseResultItem(json.get("extra_drop")));
        }
        result = json.get("result").getAsString();
        ingredient = json.get("ingredient").getAsString();
        mode = json.get("mode").getAsString();
        if (json.has("item_ingredient")) {
            var list = json.get("item_ingredient").getAsJsonArray();
            for (int i = 0 ; i < list.size() ; i++) {
                inputItems.add(parseIngredientItem(list.get(i)));
            }
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", result);
            if (!outputItems.isEmpty()) {
                json.add("extra_drop", outputItems.get(0).toResultJson());
            }
        }

        if (serializeInputs) {
            json.addProperty("ingredient", ingredient);
            json.addProperty("mode", mode);
            if (!inputItems.isEmpty()) {
                JsonArray array = new JsonArray();
                for (IngredientJS inputItem : inputItems) {
                    var item = inputItem.toJson().getAsJsonObject();
                    item.remove("nbt");
                    item.remove("type"); // Bad; this is an ingredient which means it should be able to have type as an option, this is only here b/c Kube likes to add a damage condition by default
                    array.add(item); // Strip the ingredient of Kube's dumb stuff
                }
                json.add("item_ingredient", array);
            }
        }
    }
}
