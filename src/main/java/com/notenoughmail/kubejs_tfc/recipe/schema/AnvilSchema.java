package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.capabilities.forge.ForgeRule;

public interface AnvilSchema {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<ForgeRule[]> RULES = new EnumComponent<>(ForgeRule.class).asArray().key("rules");
    RecipeKey<Integer> TIER = NumberComponent.INT.key("tier").optional(-1);
    RecipeKey<Boolean> BONUS = BooleanComponent.BOOLEAN.key("apply_forging_bonus").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, RULES, TIER, BONUS)
            .constructor(RESULT, INGREDIENT, RULES, TIER)
            .constructor(RESULT, INGREDIENT, RULES, BONUS)
            .constructor(RESULT, INGREDIENT, RULES);
}
