package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AnvilRecipeJS extends TFCRecipeJS {

    private JsonArray rules;
    private JsonObject input;
    private int tier;
    private boolean bonus;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires at least 3 arguments - result, ingredient, and rules");
        }

        outputItems.add(parseResultItem(listJS.get(0)));

        inputItems.add(parseIngredientItem(listJS.get(1)));
        input = inputItems.get(0).toJson().getAsJsonObject();
        json.add("input", input);

        rules = ListJS.orSelf(listJS.get(2)).toJson().getAsJsonArray();
        json.add("rules", rules);

        if (listJS.size() > 3) {
            bonus = ListJS.orSelf(listJS.get(3)).toJson().getAsBoolean();
        } else {
            bonus = false;
        }

        if (listJS.size() > 4) {
            tier = ListJS.orSelf(listJS.get(4)).toJson().getAsInt();
        } else {
            tier = -1;
        }

        json.addProperty("apply_forging_bonus", bonus);
        json.addProperty("tier", tier);

        System.out.println(json);
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        input = json.get("input").getAsJsonObject();
        rules = json.get("rules").getAsJsonArray();
        tier = getOptionalIntMemeber(json, "tier", -1);
        bonus = getOptionalBoolMember(json, "apply_forging_bonus", false);
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("input", input);
            json.add("rules", rules);
            json.addProperty("tier", tier);
            json.addProperty("apply_forging_bonus", bonus);
        }

        System.out.println(json);
    }
}
