package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockStateComponent;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface MovingBlockSchema {

    RecipeKey<BlockState> RESULT = BlockStateComponent.OUTPUT.key("result").optional(Blocks.AIR.defaultBlockState()).allowEmpty();
    RecipeKey<BlockIngredient> INGREDIENT = BlockIngredientComponent.INGREDIENT.key("ingredient");
    RecipeKey<Boolean> COPY_INPUT = BooleanComponent.BOOLEAN.key("copy_input").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, TFCRecipeJS::new, INGREDIENT, RESULT, COPY_INPUT)
            .constructor(RESULT, INGREDIENT)
            // Weird hack, but it seems to work
            .constructor((recipe, schemaType, keys, from) -> {
                recipe.setValue(INGREDIENT, from.getValue(recipe, INGREDIENT));
                recipe.setValue(RESULT, Blocks.AIR.defaultBlockState());
                recipe.setValue(COPY_INPUT, true);
            }, INGREDIENT);
}
