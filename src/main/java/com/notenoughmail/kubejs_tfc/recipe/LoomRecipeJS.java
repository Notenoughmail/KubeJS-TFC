package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class LoomRecipeJS extends TFCRecipeJS {

    private String tex = "minecraft:block/white_wool";

    private int count;
    private int steps;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and ingredient");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        inputItems.add(parseIngredientItem(listJS.get(1)));

        count = inputItems.get(0).getCount();

        steps = count;
        if (listJS.size() > 2) {
            steps = (int) Float.parseFloat(listJS.get(2).toString());
        }
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(parseIngredientItem(json.get("ingredient"))));
        count = json.get("input_count").getAsInt();
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
            json.add("ingredient", inputItems.get(0).toJson());
            json.addProperty("input_count", count);
            json.addProperty("steps_required", steps);
            json.addProperty("in_progress_texture", tex);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}
