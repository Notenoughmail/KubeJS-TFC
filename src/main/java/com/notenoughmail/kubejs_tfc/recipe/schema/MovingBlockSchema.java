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

    RecipeKey<BlockState> RESULT = BlockStateComponent.OUTPUT.key("result").defaultOptional(); // Oh how nice, this doesn't work. See: https://github.com/KubeJS-Mods/KubeJS/issues/743
    RecipeKey<BlockIngredient> INGREDIENT = BlockIngredientComponent.INGREDIENT.key("ingredient");
    RecipeKey<Boolean> COPY_INPUT = BooleanComponent.BOOLEAN.key("copy_input").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema()
            .constructor(RESULT, INGREDIENT)
            .constructor(RESULT, INGREDIENT, COPY_INPUT);
}
