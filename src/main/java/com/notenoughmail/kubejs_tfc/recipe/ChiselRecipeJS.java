package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ChiselRecipeJS extends TFCRecipeJS {

    private String mode = "smooth";

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least 2 arguments - result and block ingredient");
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
        var list = ListJS.ofArray(o);
        inputItems.addAll(parseIngredientItemList(list));
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
