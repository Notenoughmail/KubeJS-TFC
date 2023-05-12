package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class CastingRecipeJS extends TFCRecipeJS {

    private float breakChance = 1f;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires four arguments - result, mold, fluid ingredient, and break chance");
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(2)));

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        if (listJS.size() > 3) {
            breakChance = Float.parseFloat(listJS.get(3).toString());
        }
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        inputItems.add(parseIngredientItem(json.get("mold")));
        inputFluids.add(FluidStackIngredientJS.fromJson(json.get("fluid")));
        if (json.has("break_chance")) {
            breakChance = json.get("break_chance").getAsFloat();
        }

    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            json.add("mold", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0).toJson());
            json.addProperty("break_chance", breakChance);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " + " + inputFluids + " -> " + itemProviderResult;
    }
}