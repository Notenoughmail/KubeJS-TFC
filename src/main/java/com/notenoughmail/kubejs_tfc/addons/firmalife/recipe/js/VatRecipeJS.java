package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js;

import com.eerussianguy.firmalife.common.recipes.VatRecipe;
import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.common.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema.VatSchema.*;

@SuppressWarnings("unused")
public class VatRecipeJS extends TFCProviderRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    public VatRecipeJS outputItem(ItemStackProviderJS outputProvider) {
        setValue(OUTPUT_ITEM, outputProvider);
        return this;
    }

    public VatRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(OUTPUT_FLUID, outputFluid);
        return this;
    }

    public VatRecipeJS outputs(ItemStackProviderJS itemOutput, OutputFluid outputFluid) {
        setValue(OUTPUT_ITEM, itemOutput);
        setValue(OUTPUT_FLUID, outputFluid);
        return this;
    }

    public VatRecipeJS inputItem(InputItem inputItem) {
        setValue(INPUT_ITEM, inputItem);
        return this;
    }

    public VatRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(INPUT_FLUID, inputFluid);
        return this;
    }

    public VatRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(INPUT_ITEM, inputItem);
        setValue(INPUT_FLUID, inputFluid);
        return this;
    }

    public VatRecipeJS length(int length) {
        setValue(LENGTH, length);
        return this;
    }

    public VatRecipeJS temperature(float temperature) {
        setValue(TEMPERATURE, temperature);
        return this;
    }

    public VatRecipeJS jar(OutputItem outputItem) {
        setValue(JAR, outputItem);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof VatRecipe v) {
            return v.getInputItem() == ItemStackIngredient.EMPTY ? List.of() : List.of(v.getInputItem().ingredient());
        } else if (getOriginalRecipeResult() == null) {
            ConsoleJS.SERVER.warn("Original vat recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a vat recipe?");
        }
    }
}
