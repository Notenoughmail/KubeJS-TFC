package com.notenoughmail.kubejs_tfc.recipe.firmalife;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;
import org.jetbrains.annotations.Nullable;

public class VatRecipeJS extends TFCRecipeJS {

    public int length = 600;
    public float temperature = 300f;
    @Nullable
    public ItemStackJS jarItem;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("requires four arguments - results, ingredients, length, and temperature");
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

        if (listJS.size() > 2) {
            length = (int) Float.parseFloat(listJS.get(2).toString());
        }

        if (listJS.size() > 3) {
            temperature = Float.parseFloat(listJS.get(3).toString());
        }
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
            outputFluids.add(FluidStackJS.fromJson(json.get("output_fluid")));
        }
        if (json.has("length")) {
            length = json.get("length").getAsInt();
        }
        if (json.has("temperature")) {
            temperature = json.get("temperature").getAsFloat();
        }
        if (json.has("jar")) {
            jarItem = ItemStackJS.of(json.get("jar"));
        }
    }

    public VatRecipeJS jar(Object jar) {
        jarItem = ItemStackJS.of(jar);
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
            if (jarItem != null) {
                json.add("jar", jarItem.toResultJson());
            }
        }

        if (serializeInputs) {
            if (!inputItems.isEmpty()) {
                json.add("input_item", fixBrokenKubeIngredientStack(inputItems.get(0)));
            }
            if (!inputFluids.isEmpty()) {
                json.add("input_fluid", inputFluids.get(0).toJson());
            }
            json.addProperty("length", length);
            json.addProperty("temperature", temperature);
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
        if (jarItem != null) {
            builder.append(jarItem);
            builder.append(" + ");
        }
        builder.append(outputFluids);
        return builder.toString();
    }
}