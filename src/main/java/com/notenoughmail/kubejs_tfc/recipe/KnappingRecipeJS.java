package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class KnappingRecipeJS extends TFCRecipeJS {

    public JsonArray knapPattern = new JsonArray();
    public boolean outsideSlots = true;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and pattern");
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
        if (json.has("outside_slot_required")) {
            outsideSlots = json.get("outside_slot_required").getAsBoolean();
        }
    }

    public KnappingRecipeJS outsideSlotNotRequired() {
        return outSideSlotsRequired(false);
    }

    public KnappingRecipeJS outSideSlotsRequired(boolean required) {
        outsideSlots = required;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("pattern", knapPattern);
            json.addProperty("outside_slot_required", outsideSlots);
        }
    }
}