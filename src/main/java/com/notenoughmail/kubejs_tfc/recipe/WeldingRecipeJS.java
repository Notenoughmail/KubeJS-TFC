package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class WeldingRecipeJS extends TFCRecipeJS {

    private int weldingTier = -1; // TFC's default

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2 ) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and inputs");
        }

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));

        var result = ListJS.orSelf(listJS.get(0));
        if (result.size() < 2) {
            itemStackProvider = itemStackToISProvider(parseResultItem(result.get(0)).toResultJson().getAsJsonObject());
        } else {
            itemStackProvider = parseItemStackProvider(result);
        }
    }

    @Override
    public void deserialize() {
        itemStackProvider = json.get("result").getAsJsonObject();
        inputItems.add(parseIngredientItem(json.get("first_input")));
        inputItems.add(parseIngredientItem(json.get("second_input")));
        weldingTier = json.get("tier").getAsInt();
    }

    public WeldingRecipeJS tier(int i) {
        weldingTier = i;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemStackProvider);
        }

        if (serializeInputs) {
            json.add("first_input", inputItems.get(0).toJson());
            json.add("second_input", inputItems.get(1).toJson());
            json.addProperty("tier", weldingTier);
        }
    }
}
