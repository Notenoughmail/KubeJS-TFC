package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

import static com.notenoughmail.kubejs_tfc.recipe.schema.BarrelInstantSchema.*;

@SuppressWarnings("unused")
public class InstantBarrelRecipeJS extends BarrelRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    @Info(value = "Sets the recipe's output ItemStackProvider")
    public InstantBarrelRecipeJS outputItem(ItemStackProviderJS outputItem) {
        setValue(OUTPUT_ITEM, outputItem);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public InstantBarrelRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's output ItemStackProvider and fluid")
    public InstantBarrelRecipeJS outputs(ItemStackProviderJS outputItem, OutputFluid outputFluid) {
        setValue(OUTPUT_ITEM, outputItem);
        setValue(OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input item ingredient ingredient")
    public InstantBarrelRecipeJS inputItem(InputItem inputItem) {
        setValue(INPUT_ITEM, inputItem);
        return this;
    }

    @Info(value = "Sets the recipe's input FluidStackIngredient")
    public InstantBarrelRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input item ingredient and FluidStackIngredient")
    public InstantBarrelRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(INPUT_ITEM, inputItem);
        setValue(INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = """
            Sets the sound event the barrel will play when the recipe finishes
            
            Use '/kubejs dump_registry minecraft:sound_event' in-game to get a full list of possible values
            """)
    public InstantBarrelRecipeJS sound(String sound) {
        setValue(SOUND, sound);
        return this;
    }
}
