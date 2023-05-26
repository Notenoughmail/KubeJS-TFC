package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.ingredient.*;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;

public interface IIngredientWrapperMixin {

    public static IngredientJS heatable() {
        return new HeatableIngredientJS(null);
    }

    public static IngredientJS heatable(Object o) {
        return new HeatableIngredientJS(IngredientJS.of(o));
    }

    public static IngredientJS heatable(Object o, int min, int max) {
        return new HeatableIngredientJS(IngredientJS.of(o), min, max);
    }

    public static IngredientJS fluidIngredient(Object o) {
        return new FluidItemIngredientJS(null, FluidStackIngredientJS.of(o));
    }

    public static IngredientJS fluidIngredient(Object fluid, Object item) {
        return new FluidItemIngredientJS(IngredientJS.of(item), FluidStackIngredientJS.of(fluid));
    }

    public static IngredientJS notRotten() {
        return new NotRottenIngredientJS(null);
    }

    public static IngredientJS notRotten(Object o) {
        return new NotRottenIngredientJS(IngredientJS.of(o));
    }

    public static IngredientJS hasTrait(String trait) {
        return new TraitIngredientJS(null, trait, true);
    }

    public static IngredientJS hasTrait(String trait, Object o) {
        return new TraitIngredientJS(IngredientJS.of(o), trait, true);
    }

    public static IngredientJS lacksTrait(String trait) {
        return new TraitIngredientJS(null, trait, false);
    }

    public static IngredientJS lacksTrait(String trait, Object o) {
        return new TraitIngredientJS(IngredientJS.of(o), trait, false);
    }

    public static IngredientJS tfcNot() {
        return new TFCNotIngredientJS(null);
    }

    public static IngredientJS tfcNot(Object o) {
        return new TFCNotIngredientJS(IngredientJS.of(o));
    }
}
