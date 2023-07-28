package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class HeatingRecipeJS extends TFCRecipeJS {

    public float temperature;
    public boolean useDurability = false;

    @Override
    public void create(ListJS listJS) {
        int index = 1; // This is how I'm choosing to handle the optional result
        if (listJS.size() < 3) {
            index--;
            if (listJS.size() < 2) {
                throw new RecipeExceptionJS("Requires three arguments - result, ingredient, and temperature");
            }
        }

        if (index == 1) {
            for (var result : ListJS.orSelf(listJS.get(0))) {
                if (result instanceof FluidStackJS fluid) {
                    outputFluids.add(fluid);
                } else {
                    itemProviderResult = ItemStackProviderJS.of(result);
                }
            }
        }

        inputItems.add(parseIngredientItem(listJS.get(index)));

        temperature = Float.parseFloat(listJS.get(index + 1).toString());
    }

    @Override
    public void deserialize() {
        temperature = json.get("temperature").getAsFloat();
        inputItems.add(parseIngredientItem(json.get("ingredient")));
        if (json.has("result_item")) {
            itemProviderResult = ItemStackProviderJS.fromJson(json.get("result_item").getAsJsonObject());
        }
        if (json.has("result_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("result_fluid").getAsJsonObject()));
        }
        if (json.has("use_durability")) {
            useDurability = json.get("use_durability").getAsBoolean();
        }
    }

    public HeatingRecipeJS useDurability() {
        return useDurability(true);
    }

    public HeatingRecipeJS useDurability(boolean use) {
        useDurability = use;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (itemProviderResult != null) {
                json.add("result_item", itemProviderResult.toJson());
            }
            if (!outputFluids.isEmpty()) {
                json.add("result_fluid", outputFluids.get(0).toJson());
            }
        }

        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
            json.addProperty("temperature", temperature);
            json.addProperty("use_durability", useDurability);
        }
    }

    @Override
    public String getFromToString() {
        var builder = new StringBuilder();
        builder.append(inputItems);
        builder.append(" -> ");
        if (itemProviderResult != null) {
            builder.append(itemProviderResult);
            builder.append(" + ");
        }
        builder.append(outputFluids);
        return builder.toString();
    }
}