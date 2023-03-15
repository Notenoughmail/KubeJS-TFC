package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class SealedBarrelRecipeJS extends TFCRecipeJS {

    private int duration;
    private JsonObject onSealISP;
    private JsonObject onUnsealISP;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result(s), ingredient(s), and duration");
        }

        // If the user wishes to output only a ISP it will have to be wrapped in [] (i.e. [['6x whatever', []]]; I really should make a wrapper for ISPs and FSIs but that stuff makes my brain hurt
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

        for (var ingredient : ListJS.orSelf(listJS.get(1))) {
            if (ingredient instanceof FluidStackJS fluid) {
                inputFluids.add(fluidStackToFSIngredient(fluid.toJson()));
            } else if (ingredient.toString().matches("\\[\\[.+\\]")) {
                inputFluids.add(parseFluidStackIngredient(ListJS.of(ingredient)));
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }

        duration = ListJS.orSelf(listJS.get(2)).toJson().getAsInt();
    }

    @Override
    public void deserialize() {
        if (json.has("input_item")) {
            inputItems.add(parseIngredientItem(json.get("input_item")));
        }
        if (json.has("input_fluid")) {
            inputFluids.add(json.get("input_fluid").getAsJsonObject());
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
        duration = json.get("duration").getAsInt();
        if (json.has("on_seal")) {
            onSealISP = json.get("on_seal").getAsJsonObject();
        }
        if (json.has("on_unseal")) {
            onUnsealISP = json.get("on_unseal").getAsJsonObject();
        }
    }

    public SealedBarrelRecipeJS sound(Object o) {
        var sound = ListJS.orSelf(o);
        this.sound = sound.get(0).toString();
        save();
        return this;
    }

    public SealedBarrelRecipeJS onSeal(Object o) {
        if (o instanceof JsonObject json) {
            onSealISP = json;
        } else {
            var isp = ListJS.orSelf(o);
            if (isp.size() < 2) {
                onSealISP = itemStackToISProvider(parseResultItem(isp.get(0)).toResultJson().getAsJsonObject());
                KubeJSTFC.LOGGER.warn("The recipe {} of type {} does not specify any sealing modifiers", currentRecipe.getId(), currentRecipe.getType());
            } else {
                onSealISP = parseItemStackProvider(isp);
            }
        }
        save();
        return this;
    }

    public SealedBarrelRecipeJS onUnseal(Object o) {
        if (o instanceof JsonObject json) {
            onUnsealISP = json;
        } else {
            var isp = ListJS.orSelf(o);
            if (isp.size() < 2) {
                onUnsealISP = itemStackToISProvider(parseResultItem(isp.get(0)).toResultJson().getAsJsonObject());
                KubeJSTFC.LOGGER.warn("The recipe {} of type {} does not specify any unsealing modifiers", currentRecipe.getId(), currentRecipe.getType());
            } else {
                onUnsealISP = parseItemStackProvider(isp);
            }
        }
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            if (!itemStackProvider.isJsonNull()) {
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
                json.add("input_fluid", inputFluids.get(0));
            }
            json.addProperty("duration", duration);
            if (!onUnsealISP.isJsonNull()) {
                json.add("on_unseal", onUnsealISP);
            }
            if (!onSealISP.isJsonNull()) {
                json.add("on_seal", onSealISP);
            }
        }
    }
}
