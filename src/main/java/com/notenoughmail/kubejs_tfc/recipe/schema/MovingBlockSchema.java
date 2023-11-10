package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockStateComponent;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface MovingBlockSchema {

    RecipeKey<BlockState> RESULT = BlockStateComponent.OUTPUT.key("result").optional(Blocks.AIR.defaultBlockState()).alwaysWrite();
    RecipeKey<BlockIngredient> INGREDIENT = BlockIngredientComponent.INGREDIENT.key("ingredient");
    RecipeKey<Boolean> COPY_INPUT = BooleanComponent.BOOLEAN.key("copy_input").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, COPY_INPUT)
            .constructor(RESULT, INGREDIENT)
            .constructor(INGREDIENT, COPY_INPUT);
}
