package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class KnappingRecipeJS extends TFCRecipeJS {

    private JsonArray knapPattern = new JsonArray();

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires 2 arguments - result and pattern");
        }

        knapPattern = ListJS.orSelf(listJS.get(1)).toJson().getAsJsonArray();
        if (knapPattern.isEmpty()) {
            throw new RecipeExceptionJS("Pattern is empty!");
        }

        outputItems.add(parseResultItem(listJS.get(0)));
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        knapPattern = json.get("pattern").getAsJsonArray();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        // I guess?
        if (serializeInputs) {
            json.add("pattern", knapPattern);
        }
    }

    public KnappingRecipeJS outsideSlotNotRequired() {
        json.addProperty("outside_slot_required", false);
        save();
        return this;
    }
}
