package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.ingredient.*;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;

public interface IIngredientWrapperMixin {

    static IngredientJS heatable() {
        return new HeatableIngredientJS(null);
    }

    static IngredientJS heatable(IngredientJS o) {
        return new HeatableIngredientJS(o);
    }

    static IngredientJS heatable(IngredientJS o, int min, int max) {
        return new HeatableIngredientJS(o, min, max);
    }

    static IngredientJS fluidItem(FluidStackIngredientJS o) {
        return new FluidItemIngredientJS(null, o);
    }

    static IngredientJS fluidItem(FluidStackIngredientJS fluid, IngredientJS item) {
        return new FluidItemIngredientJS(item, fluid);
    }

    static IngredientJS notRotten() {
        return new NotRottenIngredientJS(null);
    }

    static IngredientJS notRotten(IngredientJS o) {
        return new NotRottenIngredientJS(o);
    }

    static IngredientJS hasTrait(String trait) {
        return new TraitIngredientJS(null, trait, true);
    }

    static IngredientJS hasTrait(String trait, IngredientJS o) {
        return new TraitIngredientJS(o, trait, true);
    }

    static IngredientJS lacksTrait(String trait) {
        return new TraitIngredientJS(null, trait, false);
    }

    static IngredientJS lacksTrait(String trait, IngredientJS o) {
        return new TraitIngredientJS(o, trait, false);
    }

    static IngredientJS tfcNot() {
        return new TFCNotIngredientJS(null);
    }

    static IngredientJS tfcNot(IngredientJS o) {
        return new TFCNotIngredientJS(o);
    }
}
