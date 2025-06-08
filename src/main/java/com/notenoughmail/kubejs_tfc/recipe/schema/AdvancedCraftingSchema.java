package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCProviderRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;

public interface AdvancedCraftingSchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
    RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");
    RecipeKey<Integer> ROW = NumberComponent.INT.key("input_row").preferred("inputRow");
    RecipeKey<Integer> COLUMN = NumberComponent.INT.key("input_column").preferred("inputColumn");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.UNWRAPPED_INPUT_ARRAY.key("ingredients");
    RecipeKey<InputItem> PRIMARY_INGREDIENT = ItemComponents.INPUT.key("primary_ingredient").optional(InputItem.EMPTY).preferred("primaryIngredient");

    RecipeSchema SHAPED = new RecipeSchema(AdvancedCraftingRecipeJS.class, () -> new AdvancedCraftingRecipeJS(true), RESULT, PATTERN, KEY, ROW, COLUMN);
    RecipeSchema SHAPELESS = new RecipeSchema(AdvancedCraftingRecipeJS.class, () -> new AdvancedCraftingRecipeJS(false), RESULT, INGREDIENTS, PRIMARY_INGREDIENT);

    class AdvancedCraftingRecipeJS extends TFCProviderRecipeJS {

        private static RecipeTypeFunction advShaped, advShapeless, kubeAdvShaped, kubeAdvShapeless;

        private final boolean shaped;

        public AdvancedCraftingRecipeJS(boolean shaped) {
            this.shaped = shaped;
        }

        @Override
        public RecipeTypeFunction getSerializationTypeFunction() {
            if (!json.has("kubejs:actions") && !json.has("kubejs:modify_result") && !json.has("kubejs:stage")) {
                if (shaped) {
                    if (advShaped == null) advShaped = type.event.getRecipeFunction(TFCRecipeSerializers.ADVANCED_SHAPED_CRAFTING.getId().toString());
                    return advShaped;
                } else {
                    if (advShapeless == null) advShapeless = type.event.getRecipeFunction(TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId().toString());
                    return advShapeless;
                }
            } else {
                if (shaped) {
                    if (kubeAdvShaped == null) kubeAdvShaped = type.event.getRecipeFunction(KubeJSTFCRecipeSerializers.SHAPED.getId().toString());
                    return kubeAdvShaped;
                } else {
                    if (kubeAdvShapeless == null) kubeAdvShapeless = type.event.getRecipeFunction(KubeJSTFCRecipeSerializers.SHAPELESS.getId().toString());
                    return kubeAdvShapeless;
                }
            }
        }
    }
}
