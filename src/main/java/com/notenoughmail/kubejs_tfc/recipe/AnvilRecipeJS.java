package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AnvilRecipeJS extends TFCRecipeJS {

    private JsonArray rules;
    private int tier = -1;
    private boolean bonus = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires arguments - result, ingredient, and rules");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.add(parseIngredientItem(listJS.get(1)));

        rules = ListJS.orSelf(listJS.get(2)).toJson().getAsJsonArray();
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("input")));
        rules = json.get("rules").getAsJsonArray();
        if (json.has("tier")) {
            tier = json.get("tier").getAsInt();
        }
        if (json.has("apply_forging_bonus")) {
            bonus = json.get("apply_forging_bonus").getAsBoolean();
        }
    }

    public AnvilRecipeJS tier(int i) {
        tier = i;
        save();
        return this;
    }

    public AnvilRecipeJS applyBonus() {
        bonus = true;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("input", inputItems.get(0).toJson());
            json.add("rules", rules);
            json.addProperty("tier", tier);
            json.addProperty("apply_forging_bonus", bonus);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}
