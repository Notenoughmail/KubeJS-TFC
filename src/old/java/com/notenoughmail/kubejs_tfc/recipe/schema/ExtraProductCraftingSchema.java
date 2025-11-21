package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ExtraProductCraftingSchema {

    RecipeKey<RecipeJS> RECIPE = DelegateCraftingSchema.RECIPE_COMPONENT.key("recipe");
    RecipeKey<OutputItem[]> EXTRAS = ItemComponents.OUTPUT_ARRAY.key("extra_products").preferred("extraProducts");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, () -> new TFCRecipeJS() {
        @Override
        public boolean hasInput(ReplacementMatch match) {
            return getValue(RECIPE).hasInput(match);
        }

        @Override
        public boolean replaceInput(ReplacementMatch match, InputReplacement with) {
            return getValue(RECIPE).replaceInput(match, with);
        }

        @Override
        public boolean hasOutput(ReplacementMatch match) {
            return outputValues()[0].isOutput(this, match) || getValue(RECIPE).hasOutput(match);
        }

        @Override
        public boolean replaceOutput(ReplacementMatch match, OutputReplacement with) {
            boolean replaced = outputValues()[0].replaceOutput(this, match, with);
            replaced |= getValue(RECIPE).replaceOutput(match, with); // Separate from above so JVM doesn't skip it
            return replaced;
        }
    }, EXTRAS, RECIPE);
}
