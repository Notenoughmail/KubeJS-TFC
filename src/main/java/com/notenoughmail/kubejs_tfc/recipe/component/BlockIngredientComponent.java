package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;

public class BlockIngredientComponent implements RecipeComponent<BlockIngredient> {

    public static final BlockIngredientComponent INGREDIENT = new BlockIngredientComponent();

    @Override
    public String componentType() {
        return "BlockIngredient";
    }

    @Override
    public Class<?> componentClass() {
        return BlockIngredient.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, BlockIngredient value) {
        return value.toJson();
    }

    @Override
    public BlockIngredient read(RecipeJS recipe, Object from) {
        return IngredientHelpers.ofBlockIngredient(from);
    }
}
