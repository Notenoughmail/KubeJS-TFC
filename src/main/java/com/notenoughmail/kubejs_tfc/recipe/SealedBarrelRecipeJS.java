package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class SealedBarrelRecipeJS extends TFCRecipeJS {

    private int duration;
    private ItemStackProviderJS onSealISP;
    private ItemStackProviderJS onUnsealISP;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result(s), ingredient(s), and duration");
        }

        for (var result : ListJS.orSelf(listJS.get(0))) {
            if (result instanceof FluidStackJS fluid) {
                outputFluids.add(fluid);
            } else {
                itemProviderResult = ItemStackProviderJS.of(result);
            }
        }

        for (var ingredient : ListJS.orSelf(listJS.get(1))) {
            if (ingredient instanceof FluidStackJS fluid) {
                inputFluids.add(FluidStackIngredientJS.of(fluid));
            } else if (ingredient instanceof FluidStackIngredientJS fluid) {
                inputFluids.add(fluid);
            } else {
                inputItems.add(parseIngredientItem(ingredient));
            }
        }

        // Blame js, toString does not return an int-parsable string here
        duration = (int) Float.parseFloat(listJS.get(2).toString());
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
        duration = json.get("duration").getAsInt();
        if (json.has("on_seal")) {
            onSealISP = ItemStackProviderJS.fromJson(json.get("on_seal").getAsJsonObject());
        }
        if (json.has("on_unseal")) {
            onUnsealISP = ItemStackProviderJS.fromJson(json.get("on_unseal").getAsJsonObject());
        }
    }

    public SealedBarrelRecipeJS sound(String s) {
        sound = s;
        save();
        return this;
    }

    public SealedBarrelRecipeJS onSeal(Object o) {
        if (o instanceof JsonObject json) {
            onSealISP = ItemStackProviderJS.fromJson(json);
        } else {
            onSealISP = ItemStackProviderJS.of(o);
        }
        save();
        return this;
    }

    public SealedBarrelRecipeJS onUnseal(Object o) {
        if (o instanceof JsonObject json) {
            onUnsealISP = ItemStackProviderJS.fromJson(json);
        } else {
            onUnsealISP = ItemStackProviderJS.of(o);
        }
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
            json.addProperty("duration", duration);
            if (onUnsealISP != null) {
                json.add("on_unseal", onUnsealISP.toJson());
            }
            if (onSealISP != null) {
                json.add("on_seal", onSealISP.toJson());
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
        if (onSealISP != null && onUnsealISP != null) {
            builder.append(onSealISP);
            builder.append(" + ");
            builder.append(onUnsealISP);
            builder.append(" -> ");
        } else if (onSealISP != null && onUnsealISP == null) {
            builder.append(onSealISP);
            builder.append(" + [] -> ");
        } else if (onSealISP == null && onUnsealISP != null) {
            builder.append("[] + ");
            builder.append(onUnsealISP);
            builder.append(" -> ");
        } else {
            builder.append("[] -> ");
        }
        if (itemProviderResult != null) {
            builder.append(itemProviderResult);
            builder.append(" + ");
        }
        builder.append(outputFluids );
        return builder.toString();
    }
}
