package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ChiselRecipeJS extends TFCRecipeJS {

    public String mode = "smooth";

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and block ingredient");
        }

        result = listJS.get(0).toString();

        blockIngredient = BlockIngredientJS.of(listJS.get(1));

        if (listJS.size() > 2) {
            mode = listJS.get(2).toString();
        }
    }

    @Override
    public void deserialize() {
        if (json.has("extra_drop")) {
            itemProviderResult = ItemStackProviderJS.fromJson(json.get("extra_drop").getAsJsonObject());
        }
        result = json.get("result").getAsString();
        blockIngredient = BlockIngredientJS.fromJson(json.get("ingredient"));
        mode = json.get("mode").getAsString();
        if (json.has("item_ingredient")) {
            var list = json.get("item_ingredient").getAsJsonArray();
            for (int i = 0 ; i < list.size() ; i++) {
                inputItems.add(parseIngredientItem(list.get(i)));
            }
        }
    }

    public ChiselRecipeJS extraDrop(Object o) {
        itemProviderResult = ItemStackProviderJS.of(o);
        save();
        return this;
    }

    public ChiselRecipeJS itemIngredients(Object... o) {
        inputItems.addAll(parseIngredientItemList(o));
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", result);
            if (itemProviderResult != null) {
                json.add("extra_drop", itemProviderResult.toJson());
            }
        }

        if (serializeInputs) {
            json.add("ingredient", blockIngredient.toJson());
            json.addProperty("mode", mode);
            if (!inputItems.isEmpty()) {
                if (inputItems.size() == 1) {
                    json.add("item_ingredient", inputItems.get(0).toJson().getAsJsonObject());
                } else {
                    JsonArray array = new JsonArray();
                    for (IngredientJS ingredient : inputItems) {
                        array.add(ingredient.toJson().getAsJsonObject());
                    }
                    json.add("item_ingredient", array);
                }
            }
        }
    }

    @Override
    public String getFromToString() {
        var builder = new StringBuilder();
        builder.append(inputItems);
        builder.append(" + ");
        builder.append(blockIngredient);
        builder.append(" -> ");
        builder.append(result);
        if (itemProviderResult != null) {
            builder.append(" + ");
            builder.append(itemProviderResult);
        }
        return builder.toString();
    }
}