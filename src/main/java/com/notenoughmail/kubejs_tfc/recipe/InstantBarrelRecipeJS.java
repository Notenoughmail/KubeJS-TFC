package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantBarrelRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result(s), ingredient item, and ingredient fluid");
        }

        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                itemProviderResult = ItemStackProviderJS.of(result);
            }
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));

        inputFluids.add(FluidStackIngredientJS.of(listJS.get(2)));
    }

    @Override
    public void deserialize() {
        if (json.has("input_item")) {
            inputItems.add(parseIngredientItem(json.get("input_item")));
        }
        if (json.has("input_fluid")) {
            inputFluids.add(FluidStackIngredientJS.fromJson(json.get("input_fluid")));
        }
        if (json.has("output_item")) {
            itemProviderResult = ItemStackProviderJS.fromJson(json.get("output_item").getAsJsonObject());
        }
        if (json.has("output_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        }
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    public InstantBarrelRecipeJS sound(String s) {
        sound = s;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (itemProviderResult != null) {
                json.add("output_item", itemProviderResult.toJson());
            }
            if (!outputFluids.isEmpty()) {
                json.add("output_fluid", outputFluids.get(0).toJson());
            }
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            if (!inputItems.isEmpty()) {
                json.add("input_item", fixBrokenKubeIngredientStack(inputItems.get(0)));
            }
            if (!inputFluids.isEmpty()) {
                json.add("input_fluid", inputFluids.get(0).toJson());
            }
        }
    }

    @Override
    public String getFromToString() {
        var builder = new StringBuilder();
        builder.append(inputItems);
        builder.append(" + ");
        builder.append(inputFluids);
        builder.append(" -> ");
        if (itemProviderResult != null) {
            builder.append(itemProviderResult);
            builder.append(" + ");
        }
        builder.append(outputFluids);
        return builder.toString();
    }
}
