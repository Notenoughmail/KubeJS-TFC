package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;


public interface AdvancedCraftingSchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
    RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");
    RecipeKey<Integer> ROW = NumberComponent.INT.key("input_row").preferred("inputRow");
    RecipeKey<Integer> COLUMN = NumberComponent.INT.key("input_column").preferred("inputColumn");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.UNWRAPPED_INPUT_ARRAY.key("ingredients");
    RecipeKey<InputItem> PRIMARY_INGREDIENT = ItemComponents.INPUT.key("primary_ingredient").optional(InputItem.EMPTY).preferred("primaryIngredient");

    RecipeSchema SHAPED = new RecipeSchema(RESULT, PATTERN, KEY, ROW, COLUMN);
    RecipeSchema SHAPELESS = new RecipeSchema(RESULT, INGREDIENTS, PRIMARY_INGREDIENT);
}
