package com.notenoughmail.kubejs_tfc.recipe.js;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.KnappingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.notenoughmail.kubejs_tfc.recipe.schema.KnappingSchema.*;

@SuppressWarnings("unused")
public class KnappingRecipeJS extends TFCRecipeJS {

    @Info(value = "Sets the recipe's ingredient, must match the item clicked, use to restrict recipes beyond the knapping type's ingredient")
    public KnappingRecipeJS ingredient(InputItem ingredient) {
        setValue(INGREDIENT, ingredient);
        return this;
    }

    @Info(value = "Determines if slots outside the grid of defined pattern should be required to be filled or not")
    public KnappingRecipeJS outsideSlotRequired(boolean required) {
        setValue(OUTSIDE_REQUIRED, required);
        return this;
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof KnappingRecipe k) {
            return k.getIngredient() == null ? List.of() : List.of(k.getIngredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original knapping recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a knapping recipe?");
        }
    }
}
