package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class CastingRecipeJS extends TFCRecipeJS {

    private float breakChance = 1f;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result, mold, fluid ingredient, and break chance");
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));

        boolean other = true;
        for (var ingredientFluid : ListJS.orSelf(listJS.get(2))) {
            if (ingredientFluid instanceof FluidStackJS fluid) {
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
                other = false;
            }
        }
        if (other) {
            inputFluids.add(parseFluidStackIngredient(ListJS.of(listJS.get(2))));
        }

        outputItems.add(parseResultItem(listJS.get(0)));

        breakChance = ListJS.orSelf(listJS.get(3)).toJson().getAsFloat();
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("mold")));
        inputFluids.add(json.get("fluid").getAsJsonObject());
        if (json.has("break_chance")) {
            breakChance = json.get("break_chance").getAsFloat();
        }

    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        if (serializeInputs) {
            json.add("mold", inputItems.get(0).toJson());
            json.add("fluid", inputFluids.get(0));
            json.addProperty("break_chance", breakChance);
        }
    }
}