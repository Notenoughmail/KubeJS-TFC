package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public class FluidIngredientComponent implements RecipeComponent<FluidIngredient> {

    public static final FluidIngredientComponent INGREDIENT = new FluidIngredientComponent();
    public static final FluidStackIngredientComponent STACK_INGREDIENT = new FluidStackIngredientComponent();

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

    public static class FluidStackIngredientComponent implements RecipeComponent<FluidStackIngredient> {

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
    }
}
