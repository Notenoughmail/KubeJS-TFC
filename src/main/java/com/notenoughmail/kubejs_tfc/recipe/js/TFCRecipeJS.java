package com.notenoughmail.kubejs_tfc.recipe.js;

import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.function.Function;

public class TFCRecipeJS extends RecipeJS {

    public static TFCRecipeJS of(Function<Recipe<?>, List<Ingredient>> originalIngredientGetter) {
        return new TFCRecipeJS() {
            @Override
            public List<Ingredient> getOriginalRecipeIngredients() {
                return originalIngredientGetter.apply(getOriginalRecipe());
            }
        };
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        final Recipe<?> r = getOriginalRecipe();
        if (r instanceof SimpleItemRecipe s) {
            return List.of(s.getIngredient());
        } else if (r instanceof BlastFurnaceRecipe b) {
            return List.of(b.getCatalyst());
        } else if (r instanceof CastingRecipe c) {
            return List.of(c.getIngredient());
        } else if (r instanceof IRecipeDelegate<?> d) {
            return List.copyOf(d.getIngredients());
        } else if (r instanceof GlassworkingRecipe g) {
            return List.of(g.getBatchItem());
        } else if (r instanceof WeldingRecipe w) {
            return List.of(w.getFirstInput(), w.getSecondInput());
        } else if (r == null) {
            ConsoleJS.SERVER.warn("Original TFC recipe (%s) is null - could not get ingredients".formatted(getType()));
            return List.of();
        }
        return super.getOriginalRecipeIngredients();
    }
}
