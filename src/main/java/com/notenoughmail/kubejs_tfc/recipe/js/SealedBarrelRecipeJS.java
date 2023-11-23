package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.schema.BarrelSealedSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class SealedBarrelRecipeJS extends RecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    public SealedBarrelRecipeJS outputItem(ItemStackProviderJS outputItem) {
        setValue(BarrelSealedSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    public SealedBarrelRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(BarrelSealedSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public SealedBarrelRecipeJS outputs(ItemStackProviderJS outputItem, OutputFluid outputFluid) {
        setValue(BarrelSealedSchema.OUTPUT_ITEM, outputItem);
        setValue(BarrelSealedSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public SealedBarrelRecipeJS inputItem(InputItem inputItem) {
        setValue(BarrelSealedSchema.INPUT_ITEM, inputItem);
        return this;
    }

    public SealedBarrelRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(BarrelSealedSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public SealedBarrelRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(BarrelSealedSchema.INPUT_ITEM, inputItem);
        setValue(BarrelSealedSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    public SealedBarrelRecipeJS sound(String sound) {
        setValue(BarrelSealedSchema.SOUND, sound);
        return this;
    }

    public SealedBarrelRecipeJS onSeal(ItemStackProviderJS onSeal) {
        setValue(BarrelSealedSchema.ON_SEAL, onSeal);
        return this;
    }

    public SealedBarrelRecipeJS onUnseal(ItemStackProviderJS onUnseal) {
        setValue(BarrelSealedSchema.ON_UNSEAL, onUnseal);
         return this;
    }

    public SealedBarrelRecipeJS seal(ItemStackProviderJS onSeal, ItemStackProviderJS onUnseal) {
        setValue(BarrelSealedSchema.ON_SEAL, onSeal);
        setValue(BarrelSealedSchema.ON_UNSEAL, onUnseal);
        return this;
    }
}
