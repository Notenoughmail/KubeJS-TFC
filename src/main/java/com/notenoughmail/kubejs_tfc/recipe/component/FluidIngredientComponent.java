package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class FluidIngredientComponent implements RecipeComponent<FluidIngredient> {

    public static final FluidIngredientComponent INGREDIENT = new FluidIngredientComponent();
    public static final FluidStackIngredientComponent STACK_INGREDIENT = new FluidStackIngredientComponent();

    @Override
    public ComponentRole role() {
        return ComponentRole.INPUT;
    }

    @Override
    public String componentType() {
        return "FluidIngredient";
    }

    @Override
    public Class<?> componentClass() {
        return FluidIngredient.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, FluidIngredient value) {
        return value.toJson();
    }

    @Override
    public FluidIngredient read(RecipeJS recipe, Object from) {
        return IngredientHelpers.ofFluidIngredient(from);
    }

    @Override
    public boolean isInput(RecipeJS recipe, FluidIngredient value, ReplacementMatch match) {
        final FluidIngredient matchIng = IngredientHelpers.ofFluidIngredient(match);
        if (!matchIng.entries().isEmpty()) {
            return matchIng.entries().stream().anyMatch(e -> value.all().anyMatch(e));
        }
        return false;
    }

    public static class FluidStackIngredientComponent implements RecipeComponent<FluidStackIngredient> {

        @Override
        public ComponentRole role() {
            return ComponentRole.INPUT;
        }

        @Override
        public String componentType() {
            return "FluidStackIngredient";
        }

        @Override
        public Class<?> componentClass() {
            return FluidStackIngredient.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, FluidStackIngredient value) {
            return value.toJson();
        }

        @Override
        public FluidStackIngredient read(RecipeJS recipe, Object from) {
            return IngredientHelpers.ofFluidStackIngredient(from);
        }

        @Override
        public boolean isInput(RecipeJS recipe, FluidStackIngredient value, ReplacementMatch match) {
            return INGREDIENT.isInput(recipe, value.ingredient(), match);
        }
    }
}
