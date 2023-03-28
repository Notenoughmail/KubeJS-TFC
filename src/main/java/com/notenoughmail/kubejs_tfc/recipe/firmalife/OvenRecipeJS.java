package com.notenoughmail.kubejs_tfc.recipe.firmalife;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class OvenRecipeJS extends TFCRecipeJS {

    private int duration;
    private float temperature;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result, ingredient, duration, and temperature");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.add(parseIngredientItem(listJS.get(1)));

        duration = (int) Float.parseFloat(listJS.get(2).toString());

        temperature = Float.parseFloat(listJS.get(3).toString());
    }

    @Override
    public void deserialize() {
        inputItems.add(parseIngredientItem(json.get("ingredient")));
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result_item").getAsJsonObject());
        temperature = json.get("temperature").getAsFloat();
        duration = json.get("duration").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result_item", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
            json.addProperty("temperature", temperature);
            json.addProperty("duration", duration);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems  + " -> " + itemProviderResult;
    }
}
