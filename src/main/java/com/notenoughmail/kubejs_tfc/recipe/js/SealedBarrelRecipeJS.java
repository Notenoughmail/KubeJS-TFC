package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.recipe.schema.BarrelSealedSchema;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class SealedBarrelRecipeJS extends RecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    @Info(value = "Sets the recipe's output ItemStackProvider")
    public SealedBarrelRecipeJS outputItem(ItemStackProviderJS outputItem) {
        setValue(BarrelSealedSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    @Info(value = "Sets the recipe's output fluid")
    public SealedBarrelRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(BarrelSealedSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's output ItemStackProvider and fluid")
    public SealedBarrelRecipeJS outputs(ItemStackProviderJS outputItem, OutputFluid outputFluid) {
        setValue(BarrelSealedSchema.OUTPUT_ITEM, outputItem);
        setValue(BarrelSealedSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input ingredient")
    public SealedBarrelRecipeJS inputItem(InputItem inputItem) {
        setValue(BarrelSealedSchema.INPUT_ITEM, inputItem);
        return this;
    }

    @Info(value = "Sets the recipe's input FluidStackIngredient")
    public SealedBarrelRecipeJS inputFluid(FluidStackIngredient inputFluid) {
        setValue(BarrelSealedSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = "Sets the recipe's input ingredient and FluidStackIngredient")
    public SealedBarrelRecipeJS inputs(InputItem inputItem, FluidStackIngredient inputFluid) {
        setValue(BarrelSealedSchema.INPUT_ITEM, inputItem);
        setValue(BarrelSealedSchema.INPUT_FLUID, inputFluid);
        return this;
    }

    @Info(value = """
            Sets the sound event the barrel will play when the recipe finishes
            
            Use '/kubejs dump_registry minecraft:sound_event' in-game to get a full list of possible values
            """)
    public SealedBarrelRecipeJS sound(String sound) {
        setValue(BarrelSealedSchema.SOUND, sound);
        return this;
    }

    @Info(value = """
            Sets the ItemStackProvider which will be applied whenever the barrel is sealed
            
            Can be used to apply special effects to the item in the barrel while sealed
            """)
    public SealedBarrelRecipeJS onSeal(ItemStackProviderJS onSeal) {
        setValue(BarrelSealedSchema.ON_SEAL, onSeal);
        return this;
    }

    @Info(value = """
            Sets the ItemStackProvider which will be applied whenever the barrel is unsealed
            
            Can be used to remove special effects from the item in the barrel while sealed
            """)
    public SealedBarrelRecipeJS onUnseal(ItemStackProviderJS onUnseal) {
        setValue(BarrelSealedSchema.ON_UNSEAL, onUnseal);
         return this;
    }

    @Info(value = "Sets the recipe's onSeal and onUnseal ItemStackProviders")
    public SealedBarrelRecipeJS seal(ItemStackProviderJS onSeal, ItemStackProviderJS onUnseal) {
        setValue(BarrelSealedSchema.ON_SEAL, onSeal);
        setValue(BarrelSealedSchema.ON_UNSEAL, onUnseal);
        return this;
    }
}
