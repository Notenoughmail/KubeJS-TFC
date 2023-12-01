package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.schema.BarrelInstantSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class InstantBarrelRecipeJS extends RecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    @Info(value = "Sets the recipe's output ItemStackProvider")
    public InstantBarrelRecipeJS outputItem(ItemStackProviderJS outputItem) {
        setValue(BarrelInstantSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public InstantBarrelRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(BarrelInstantSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's output ItemStackProvider and fluid")
    public InstantBarrelRecipeJS outputs(ItemStackProviderJS outputItem, OutputFluid outputFluid) {
        setValue(BarrelInstantSchema.OUTPUT_ITEM, outputItem);
        setValue(BarrelInstantSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input item ingredient ngredient")
    public InstantBarrelRecipeJS inputItem(InputItem inputItem) {
        setValue(BarrelInstantSchema.INPUT_ITEM, inputItem);
        return this;
    }

    @Info(value = "Sets the recipe's input FluidStackIngredient")
    public InstantBarrelRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(BarrelInstantSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input item ingredient and FluidStackIngredient")
    public InstantBarrelRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(BarrelInstantSchema.INPUT_ITEM, inputItem);
        setValue(BarrelInstantSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = """
            Sets the sound event the barrel will play when the recipe finishes
            
            Use '/kubejs dump_registry minecraft:sound_event' in-game to get a full list of possible values
            """)
    public InstantBarrelRecipeJS sound(String sound) {
        setValue(BarrelInstantSchema.SOUND, sound);
        return this;
    }
}
