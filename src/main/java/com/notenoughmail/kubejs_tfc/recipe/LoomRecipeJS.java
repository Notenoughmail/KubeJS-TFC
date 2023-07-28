package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class LoomRecipeJS extends TFCRecipeJS {

    public String tex = "minecraft:block/white_wool";

    public int steps;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredient");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.add(parseIngredientItem(listJS.get(1)));

        steps = inputItems.get(0).getCount();
        if (listJS.size() > 2) {
            steps = (int) Float.parseFloat(listJS.get(2).toString());
        }
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("ingredient")).withCount(json.get("input_count").getAsInt()));
        steps = json.get("steps_required").getAsInt();
    }

    public LoomRecipeJS inProgressTexture(String s) {
        tex = s;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).unwrapStackIngredient().get(0).toJson());
            json.addProperty("input_count", inputItems.get(0).getCount());
            json.addProperty("steps_required", steps);
            json.addProperty("in_progress_texture", tex);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}