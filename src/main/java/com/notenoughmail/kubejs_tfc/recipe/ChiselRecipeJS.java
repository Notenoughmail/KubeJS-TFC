package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ChiselRecipeJS extends TFCRecipeJS {

    private String mode = "smooth";

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least 2 arguments - result and ingredient");
        }

        result = listJS.get(0).toString();

        ingredient = listJS.get(1).toString();

        if (listJS.size() > 2) {
            mode = listJS.get(2).toString();
        }
    }

    @Override
    public void deserialize() {
        if (json.has("extra_drop")) {
            itemStackProvider = json.get("extra_drop").getAsJsonObject();
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

    public ChiselRecipeJS extraDrop(Object o) {
        var drop = ListJS.orSelf(o);
        if (drop.size() < 2) {
            itemStackProvider = itemStackToISProvider(parseResultItem(drop.get(0)).toResultJson().getAsJsonObject());
        } else {
            itemStackProvider = parseItemStackProvider(drop);
        }
        json.add("extra_drop", itemStackProvider);
        save();
        return this;
    }

    public ChiselRecipeJS itemIngredients(Object... o) {
        var list = ListJS.ofArray(o);
        inputItems.addAll(parseIngredientItemList(list));
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", result);
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
