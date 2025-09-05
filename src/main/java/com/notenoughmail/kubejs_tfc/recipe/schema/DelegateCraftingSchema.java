package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.recipe.component.NestedRecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.UtilsJS;

public interface DelegateCraftingSchema {

    // Kube does not implement these in quite a few of its default components
    NestedRecipeComponent RECIPE_COMPONENT = new NestedRecipeComponent() {

        @Override
        public boolean isInput(RecipeJS recipe, RecipeJS value, ReplacementMatch match) {
            for (RecipeKey<?> key : value.type.schemaType.schema.keys) {
                if (key.component.role().isInput() && key.component.isInput(recipe, UtilsJS.cast(value.getValue(key)), match)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public RecipeJS replaceInput(RecipeJS recipe, RecipeJS original, ReplacementMatch match, InputReplacement with) {
            if (isInput(recipe, original, match)) {
                original.replaceInput(match, with);
            }
            return original;
        }

        @Override
        public boolean isOutput(RecipeJS recipe, RecipeJS value, ReplacementMatch match) {
            for (RecipeKey<?> key : value.type.schemaType.schema.keys) {
                if (key.component.role().isInput() && key.component.isInput(recipe, UtilsJS.cast(value.getValue(key)), match)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public RecipeJS replaceOutput(RecipeJS recipe, RecipeJS original, ReplacementMatch match, OutputReplacement with) {
            if (isOutput(recipe, original, match)) {
                original.replaceOutput(match, with);
            }
            return original;
        }
    };

    RecipeKey<RecipeJS> RECIPE = RECIPE_COMPONENT.key("recipe");

    static RecipeSchema schema(String type) {
        return new RecipeSchema(TFCRecipeJS.class, () -> new TFCRecipeJS() {

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
                return getValue(RECIPE).hasOutput(match);
            }

            @Override
            public boolean replaceOutput(ReplacementMatch match, OutputReplacement with) {
                return getValue(RECIPE).replaceOutput(match, with);
            }
        }, RECIPE).uniqueId(recipe -> type + "/" + recipe.getValue(RECIPE).getPath());
    }
}
