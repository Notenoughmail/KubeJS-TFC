package com.notenoughmail.kubejs_tfc.addons.afc.recipe;

import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;

public interface TreeTapSchema {

    RecipeKey<OutputFluid> RESULT_FLUID = FluidComponents.OUTPUT.key("result_fluid").optional(EmptyFluidStackJS.INSTANCE).preferred("resultFluid");
    RecipeKey<Float> MIN_TEMP = NumberComponent.ANY_FLOAT.key("minimum_temperature").optional(-50.0F).preferred("minTemp");
    RecipeKey<Float> MAX_TEMP = NumberComponent.ANY_FLOAT.key("maximum_temperature").optional(50.0F).preferred("maxTemp");
    RecipeKey<Boolean> REQUIRES_NATURAL_LOG = BooleanComponent.BOOLEAN.key("requires_natural_log").optional(true).preferred("requiresNaturalLog");
    RecipeKey<Boolean> SPRING_ONLY = BooleanComponent.BOOLEAN.key("spring_only").optional(false).preferred("springOnly");
    RecipeKey<BlockIngredient> INPUT_BLOCK = BlockIngredientComponent.INGREDIENT.key("input_block");

    RecipeSchema SCHEMA = new RecipeSchema(TreeTapRecipeJS.class, TreeTapRecipeJS::new, INPUT_BLOCK, RESULT_FLUID, MIN_TEMP, MAX_TEMP, REQUIRES_NATURAL_LOG, SPRING_ONLY)
            .constructor(INPUT_BLOCK);
}
