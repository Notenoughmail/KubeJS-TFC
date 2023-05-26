package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.ingredient.*;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = IngredientJS.class, remap = false)
public interface IngredientJSMixin {

    default IngredientJS asHeatable() {
        return new HeatableIngredientJS((IngredientJS) this);
    }

    default IngredientJS asHeatable(int min, int max) {
        return new HeatableIngredientJS((IngredientJS) this, min, max);
    }

    default IngredientJS asNotRotten() {
        return new NotRottenIngredientJS((IngredientJS) this);
    }

    default IngredientJS asTFCNot() {
        return new TFCNotIngredientJS((IngredientJS) this);
    }

    default IngredientJS asFluidItem(Object o) {
        return new FluidItemIngredientJS((IngredientJS) this, FluidStackIngredientJS.of(o));
    }

    default IngredientJS asHasTrait(String s) {
        return new TraitIngredientJS((IngredientJS) this, s, true);
    }

    default IngredientJS asLacksTrait(String s) {
        return new TraitIngredientJS((IngredientJS) this, s, false);
    }
}
