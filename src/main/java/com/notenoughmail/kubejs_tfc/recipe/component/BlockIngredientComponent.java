package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockIngredientComponent implements RecipeComponent<BlockIngredient> {

    public static final BlockIngredientComponent INGREDIENT = new BlockIngredientComponent();

    @Override
    public Class<?> componentClass() {
        return BlockIngredient.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, BlockIngredient value) {
        return IngredientType.toJson(value, IngredientHelpers.blockIngredientFactory);
    }

    @Override
    public BlockIngredient read(RecipeJS recipe, Object from) {
        if (from instanceof BlockIngredient block) {
            return block;
        } else if (from instanceof JsonElement json) {
            return BlockIngredient.fromJson(json);
        } else if (from instanceof Block block) {
            return IngredientHelpers.of(block);
        } else if (from instanceof BlockState state) {
            return IngredientHelpers.of(state.getBlock());
        } else {
            return IngredientHelpers.of(Blocks.AIR);
        }
    }
}
