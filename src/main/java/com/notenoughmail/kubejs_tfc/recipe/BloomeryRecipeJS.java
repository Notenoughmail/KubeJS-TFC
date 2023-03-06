package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class BloomeryRecipeJS extends TFCRecipeJS {

    private int duration;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result, fluid ingredient, catalyst, and duration");
        }

        outputItems.add(parseResultItem(listJS.get(0)));

        fluidStackIngredient = buildFluidStackIngredient(ListJS.of(listJS.get(1)));

        inputItems.add(parseIngredientItem(listJS.get(2)).asIngredientStack().ingredient);

        duration = ListJS.orSelf(listJS.get(3)).toJson().getAsInt();
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("catalyst")));
        fluidStackIngredient = json.get("fluid").getAsJsonObject();
        duration = json.get("duration").getAsInt();

        System.out.println(json);
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("catalyst", inputItems.get(0).toJson());
            json.add("fluid", fluidStackIngredient);
            json.addProperty("duration", duration);
        }

        System.out.println(json);
    }
}
