package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class WeldingRecipeJS extends TFCRecipeJS {

    private int weldingTier = -1; // TFC's default
    private boolean combineForgingBonus = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2 ) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and inputs");
        }

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("first_input")));
        inputItems.add(parseIngredientItem(json.get("second_input")));
        weldingTier = json.get("tier").getAsInt();
        if (json.has("combine_forging_bonus")) {
            combineForgingBonus = json.get("combine_forging_bonus").getAsBoolean();
        }
    }

    public WeldingRecipeJS tier(int i) {
        weldingTier = i;
        save();
        return this;
    }

    public WeldingRecipeJS combineForgingBonus() {
        combineForgingBonus = true;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("first_input", inputItems.get(0).toJson());
            json.add("second_input", inputItems.get(1).toJson());
            json.addProperty("tier", weldingTier);
            json.addProperty("combine_forging_bonus", combineForgingBonus);
        }
    }

    @Override
    public String toString() {
        return inputItems + " -> " + itemProviderResult;
    }
}
