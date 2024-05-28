package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.KnappingSchema;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.typings.Info;

@SuppressWarnings("unused")
public class KnappingRecipeJS extends TFCRecipeJS {

    @Info(value = "Sets the recipe's ingredient, must match the item clicked, use to restrict recipes beyond the knapping type's ingredient")
    public KnappingRecipeJS ingredient(InputItem ingredient) {
        setValue(KnappingSchema.INGREDIENT, ingredient);
        return this;
    }

    @Info(value = "Determines if slots outside the grid of defined pattern should be required to be filled or not")
    public KnappingRecipeJS outsideSlotRequired(boolean required) {
        setValue(KnappingSchema.OUTSIDE_REQUIRED, required);
        return this;
    }
}
