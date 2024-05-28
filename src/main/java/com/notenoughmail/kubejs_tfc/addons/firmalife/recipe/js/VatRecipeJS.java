package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema.VatSchema;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

@SuppressWarnings("unused")
public class VatRecipeJS extends TFCProviderRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    public VatRecipeJS outputItem(ItemStackProviderJS outputProvider) {
        setValue(VatSchema.OUTPUT_ITEM, outputProvider);
        return this;
    }

    public VatRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(VatSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public VatRecipeJS outputs(ItemStackProviderJS itemOutput, OutputFluid outputFluid) {
        setValue(VatSchema.OUTPUT_ITEM, itemOutput);
        setValue(VatSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public VatRecipeJS inputItem(InputItem inputItem) {
        setValue(VatSchema.INPUT_ITEM, inputItem);
        return this;
    }

    public VatRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(VatSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public VatRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(VatSchema.INPUT_ITEM, inputItem);
        setValue(VatSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public VatRecipeJS length(int length) {
        setValue(VatSchema.LENGTH, length);
        return this;
    }

    public VatRecipeJS temperature(float temperature) {
        setValue(VatSchema.TEMPERATURE, temperature);
        return this;
    }

    public VatRecipeJS jar(OutputItem outputItem) {
        setValue(VatSchema.JAR, outputItem);
        return this;
    }
}
