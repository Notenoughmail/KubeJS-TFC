package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class CastingRecipeJS extends TFCRecipeJS {

    private float breakChance;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 5) {
            throw new RecipeExceptionJS("Requires five arguments - result, mold, fluid, amount, and break chance");
        }

        inputItems.addAll(parseIngredientItemList(listJS.get(1)));
        json.add("mold", inputItems.get(0).toJson());

        // A tad awful
        fluidIngredient = buildFluidIngredient(listJS.get(2).toString(), ListJS.orSelf(listJS.get(3)).toJson().getAsInt());
        json.add("fluid", fluidIngredient);

        outputItems.add(parseResultItem(listJS.get(0)));

        breakChance = ListJS.orSelf(listJS.get(4)).toJson().getAsFloat();
        json.addProperty("break_chance", breakChance);
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("mold")));
        fluidIngredient = json.get("fluid").getAsJsonObject();
        breakChance = json.get("break_chance").getAsFloat();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("mold", inputItems.get(0).toJson());
            json.add("fluid", fluidIngredient);
            json.addProperty("break_chance", breakChance);
        }
    }
}