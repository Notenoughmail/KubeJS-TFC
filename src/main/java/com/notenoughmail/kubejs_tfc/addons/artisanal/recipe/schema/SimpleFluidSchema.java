package com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.mrhitech.artisanal.common.recipes.SimpleFluidRecipe;

import java.util.List;

public interface SimpleFluidSchema {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<OutputFluid> RESULT = FluidComponents.OUTPUT.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, () -> TFCRecipeJS.of(r -> {
        if (r instanceof SimpleFluidRecipe s) {
            return List.of(s.getIngredient());
        } else if (r == null) {
            ConsoleJS.SERVER.warn("Original simple fluid recipe is null - could not get ingredeints");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a simple fluid recipe?");
        }
    }), RESULT, INGREDIENT);
}
