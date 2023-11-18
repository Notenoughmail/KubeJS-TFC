package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This
 */
public class TFCBlockStateComponent implements RecipeComponent<BlockState> {
    @Override
    public Class<?> componentClass() {
        return BlockState.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, BlockState value) {
        return null;
    }

    @Override
    public BlockState read(RecipeJS recipe, Object from) {
        return null;
    }
}
