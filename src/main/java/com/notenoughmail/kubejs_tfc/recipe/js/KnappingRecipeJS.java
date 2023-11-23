package com.notenoughmail.kubejs_tfc.recipe.js;

import com.notenoughmail.kubejs_tfc.recipe.schema.KnappingSchema;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public class KnappingRecipeJS extends RecipeJS {

    public KnappingRecipeJS ingredient(InputItem ingredient) {
        setValue(KnappingSchema.INGREDIENT, ingredient);
        return this;
    }

    public KnappingRecipeJS outsideSlotRequired(boolean required) {
        setValue(KnappingSchema.OUTSIDE_REQUIRED, required);
        return this;
    }
}
