package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantBarrelRecipeJS extends TFCRecipeJS {

    // Bad, but it works (maybe)
    // Dangerous/broken when only one type of input
    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires four arguments - result type, result, ingredient item, and ingredient fluid");
        }

        if (listJS.get(0).toString().matches("^item$")) {
            outputItems.add(parseResultItem(listJS.get(1)));
        } else if (listJS.get(0).toString().matches("^fluid$")) {
            fluidResult = parseFluidStack(ListJS.of(listJS.get(1)));
        } else {
            outputItems.add(parseResultItem(listJS.get(0)));
            fluidResult = parseFluidStack(ListJS.of(listJS.get(1)));
        }

        /*
        if (listJS.get(2).toString().matches("^item$")) {
            inputItems.add(parseIngredientItem(listJS.get(3)));
        } else if (listJS.get(2).toString().matches("^fluid$")) {
            fluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(3)));
        } else {
            inputItems.add(parseIngredientItem(listJS.get(2)));
            fluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(3)));
        }*/

        inputItems.add(parseIngredientItem(listJS.get(2)));
        fluidStackIngredient = parseFluidStackIngredient(ListJS.of(listJS.get(3)));

        if (listJS.size() > 4) {
            sound = listJS.get(4).toString();
        }
    }

    @Override
    public void deserialize() {
        if (json.has("input_item")) {
            inputItems.add(parseIngredientItem(json.get("input_item")));
        }
        if (json.has("input_fluid")) {
            fluidStackIngredient = json.get("input_fluid").getAsJsonObject();
        }
        if (json.has("output_item")) {
            outputItems.add(parseResultItem(json.get("output_item")));
        }
        if (json.has("output_fluid")) {
            fluidResult = json.get("output_fluid").getAsJsonObject();
        }
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (!outputItems.isEmpty()) {
                json.add("output_item", outputItems.get(0).toResultJson());
            }
            if (fluidResult != null) {
                json.add("output_fluid", fluidResult);
            }
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            if (!inputItems.isEmpty()) {
                json.add("input_item", inputItems.get(0).toJson());
            }
            if (fluidStackIngredient != null) {
                json.add("input_fluid", fluidStackIngredient);
            }
        }
    }
}
