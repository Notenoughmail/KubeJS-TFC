package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockStateComponent;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.world.level.block.state.BlockState;

public interface ChiselSchema {

    RecipeKey<BlockState> RESULT = BlockStateComponent.OUTPUT.key("result");
    RecipeKey<BlockIngredient> INGREDIENT = BlockIngredientComponent.INGREDIENT.key("ingredient");
    RecipeKey<ChiselRecipe.Mode> MODE = new EnumComponent<>(ChiselRecipe.Mode.class).key("mode");
    RecipeKey<InputItem> ITEM_INGREDIENT = ItemComponents.INPUT.key("item_ingredient").optional(InputItem.of("#" + TFCTags.Items.CHISELS.location()));
    RecipeKey<ItemStackProviderJS> EXTRA_DROP = ItemProviderComponent.PROVIDER.key("extra_drop").optional(ItemStackProviderJS.EMPTY).preferred("extraDrop");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, MODE, ITEM_INGREDIENT, EXTRA_DROP)
            .constructor(RESULT, INGREDIENT, MODE, EXTRA_DROP)
            .constructor(RESULT, INGREDIENT, MODE);
}
