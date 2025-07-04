package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js;

import com.eerussianguy.firmalife.common.recipes.MixingBowlRecipe;
import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema.MixingBowlSchema;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

@SuppressWarnings("unused")
public class MixingBowlRecipeJS extends TFCRecipeJS {

    public MixingBowlRecipeJS outputItem(OutputItem outputItem) {
        setValue(MixingBowlSchema.OUTPUT_ITEM, outputItem);
        return this;
    }

    public MixingBowlRecipeJS outputFluid(OutputFluid outputFluid) {
        setValue(MixingBowlSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public MixingBowlRecipeJS outputs(OutputItem outputItem, OutputFluid outputFluid) {
        setValue(MixingBowlSchema.OUTPUT_ITEM, outputItem);
        setValue(MixingBowlSchema.OUTPUT_FLUID, outputFluid);
        return this;
    }

    public MixingBowlRecipeJS itemIngredients(InputItem[] ingredients) {
        setValue(MixingBowlSchema.INGREDIENTS, ingredients);
        return this;
    }

    public MixingBowlRecipeJS fluidIngredient(FluidStackIngredient fluidIngredient) {
        setValue(MixingBowlSchema.FLUID_INGREDIENT, fluidIngredient);
        return this;
    }

    public MixingBowlRecipeJS ingredients(InputItem[] ingredients, FluidStackIngredient fluidIngredient) {
        setValue(MixingBowlSchema.INGREDIENTS, ingredients);
        setValue(MixingBowlSchema.FLUID_INGREDIENT, fluidIngredient);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof MixingBowlRecipe m) {
            return List.copyOf(m.getItemIngredients());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a mixing bowl recipe?");
        }
    }
}
