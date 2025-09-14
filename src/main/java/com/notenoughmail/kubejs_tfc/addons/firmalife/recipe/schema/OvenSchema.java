package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema;

import com.eerussianguy.firmalife.common.recipes.OvenRecipe;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.ConsoleJS;

import java.util.List;

public interface OvenSchema {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<ItemStackProviderJS> RESULT_ITEM = ItemProviderComponent.PROVIDER.key("result_item").preferred("resultItem").optional(ItemStackProviderJS.EMPTY);
    RecipeKey<Float> TEMPERATURE = NumberComponent.FLOAT.key("temperature");
    RecipeKey<Integer> DURATION = NumberComponent.INT.key("duration");

    RecipeSchema SCHEMA = new RecipeSchema(TFCRecipeJS.class, () -> TFCRecipeJS.of(r -> {
        if (r instanceof OvenRecipe o) {
            return List.of(o.getIngredient());
        } else if (r == null) {
            ConsoleJS.SERVER.warn("Original oven recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not an oven recipe?");
        }
    }), INGREDIENT, TEMPERATURE, DURATION, RESULT_ITEM);
}
