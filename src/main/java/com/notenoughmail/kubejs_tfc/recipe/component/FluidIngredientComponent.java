package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.level.material.Fluid;

public class FluidIngredientComponent implements RecipeComponent<FluidIngredient> {

    public static final FluidIngredientComponent INGREDIENT = new FluidIngredientComponent();
    public static final FluidStackIngredientComponent STACK_INGREDIENT = new FluidStackIngredientComponent();

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
        if (from instanceof FluidIngredient fluid) {
            return fluid;
        } else if (from instanceof JsonElement json ) {
            return FluidIngredient.fromJson(json);
        } else if (from instanceof Fluid fluid) {
            return IngredientHelpers.fluid(fluid);
        } else if (from instanceof FluidStackJS fluid) {
            return IngredientHelpers.fluid(fluid.getFluid());
        } else if (from instanceof FluidStack fluid) {
            return IngredientHelpers.fluid(fluid.getFluid());
        } else if (from instanceof net.minecraftforge.fluids.FluidStack fluid) {
            return IngredientHelpers.fluid(fluid.getFluid());
        } else {
            return FluidStackIngredient.EMPTY.ingredient();
        }
    }

    public static class FluidStackIngredientComponent implements RecipeComponent<FluidStackIngredientJS> {

        @Override
        public Class<?> componentClass() {
            return FluidStackIngredientJS.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, FluidStackIngredientJS value) {
            return value.toCannonClass().toJson();
        }

        @Override
        public FluidStackIngredientJS read(RecipeJS recipe, Object from) {
            if (from instanceof FluidStackIngredientJS fluid) {
                return fluid;
            } else if (from instanceof FluidIngredient fluid) {
                return new FluidStackIngredientJS(fluid, FluidStack.bucketAmount());
            } else if (from instanceof FluidStackJS js) {
                return new FluidStackIngredientJS(IngredientHelpers.fluid(js.getFluid()), js.getAmount());
            } else if (from instanceof Fluid fluid) {
                return new FluidStackIngredientJS(IngredientHelpers.fluid(fluid), FluidStack.bucketAmount());
            } else if (from instanceof JsonElement json && json.isJsonObject()) {
                return new FluidStackIngredientJS(FluidStackIngredient.fromJson(json.getAsJsonObject()));
            } else {
                return FluidStackIngredientJS.EMPTY;
            }
        }
    }
}
