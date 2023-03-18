package com.notenoughmail.kubejs_tfc.recipe;

import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class InstantBarrelRecipeJS extends TFCRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result(s), ingredient item, and ingredient fluid");
        }

        // If the user wishes to output only a ISP it will have to be wrapped in [] (i.e. [['6x whatever', [json]]]; I really should make a wrapper for ISPs and FSIs but that stuff makes my brain hurt
        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                var item = ListJS.orSelf(result);
                if (item.size() < 2) {
                    itemStackProvider = itemStackToISProvider(parseResultItem(item.get(0)).toResultJson().getAsJsonObject());
                } else {
                    itemStackProvider = parseItemStackProvider(item);
                }
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
            itemStackProvider = json.get("output_item").getAsJsonObject();
        }
        if (json.has("output_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid").getAsJsonObject()));
        }
        if (json.has("sound")) {
            sound = json.get("sound").getAsString();
        }
    }

    public InstantBarrelRecipeJS sound(Object o) {
        var sound = ListJS.orSelf(o);
        this.sound = sound.get(0).toString();
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (itemStackProvider != null) {
                json.add("output_item", itemStackProvider);
            }
            if (!outputFluids.isEmpty()) {
                json.add("output_fluid", outputFluids.get(0).toJson());
            }
            json.addProperty("sound", sound);
        }

        if (serializeInputs) {
            if (!inputItems.isEmpty()) {
                json.add("input_item", inputItems.get(0).toJson());
            }
            if (!inputFluids.isEmpty()) {
                json.add("input_fluid", inputFluids.get(0).toJson());
            }
        }
    }
}
