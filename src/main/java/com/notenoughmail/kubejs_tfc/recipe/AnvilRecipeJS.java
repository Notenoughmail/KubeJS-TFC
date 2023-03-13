package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AnvilRecipeJS extends TFCRecipeJS {

    private JsonArray rules;
    private JsonObject input;
    private int tier = -1;
    private boolean bonus = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires at least 3 arguments - result, ingredient, and rules");
        }

        var result = ListJS.orSelf(listJS.get(0));
        if (result.size() < 2) {
            itemStackProvider = itemStackToISProvider(parseResultItem(result.get(0)).toResultJson().getAsJsonObject());
        } else {
            itemStackProvider = parseItemStackProvider(result);
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));
        input = inputItems.get(0).toJson().getAsJsonObject();

        rules = ListJS.orSelf(listJS.get(2)).toJson().getAsJsonArray();
    }

    @Override
    public void deserialize() {
        itemStackProvider = json.get("result").getAsJsonObject();
        input = json.get("input").getAsJsonObject();
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
            json.add("result", itemStackProvider);
        }

        if (serializeInputs) {
            json.add("input", input);
            json.add("rules", rules);
            json.addProperty("tier", tier);
            json.addProperty("apply_forging_bonus", bonus);
        }
    }
}
