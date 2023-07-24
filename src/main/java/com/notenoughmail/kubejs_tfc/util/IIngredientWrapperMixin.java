package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.ingredient.*;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.rhino.util.HideFromJS;

@HideFromJS
public interface IIngredientWrapperMixin {

    public static IngredientJS heatable() {
        return new HeatableIngredientJS(null);
    }

    public static IngredientJS heatable(IngredientJS o) {
        return new HeatableIngredientJS(o);
    }

    public static IngredientJS heatable(IngredientJS o, int min, int max) {
        return new HeatableIngredientJS(o, min, max);
    }

    public static IngredientJS fluidItem(FluidStackIngredientJS o) {
        return new FluidItemIngredientJS(null, o);
    }

    public static IngredientJS fluidItem(FluidStackIngredientJS fluid, IngredientJS item) {
        return new FluidItemIngredientJS(item, fluid);
    }

    public static IngredientJS notRotten() {
        return new NotRottenIngredientJS(null);
    }

    public static IngredientJS notRotten(IngredientJS o) {
        return new NotRottenIngredientJS(o);
    }

    public static IngredientJS hasTrait(String trait) {
        return new TraitIngredientJS(null, trait, true);
    }

    public static IngredientJS hasTrait(String trait, IngredientJS o) {
        return new TraitIngredientJS(o, trait, true);
    }

    public static IngredientJS lacksTrait(String trait) {
        return new TraitIngredientJS(null, trait, false);
    }

    public static IngredientJS lacksTrait(String trait, IngredientJS o) {
        return new TraitIngredientJS(o, trait, false);
    }

    public static IngredientJS tfcNot() {
        return new TFCNotIngredientJS(null);
    }

    public static IngredientJS tfcNot(IngredientJS o) {
        return new TFCNotIngredientJS(o);
    }
}
