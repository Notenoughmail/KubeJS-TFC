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
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.dries007.tfc.common.recipes.AdvancedShapedRecipe;
import net.dries007.tfc.common.recipes.AdvancedShapelessRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface AdvancedCraftingSchema {

    RecipeKey<ItemStackProviderJS> RESULT = ItemProviderComponent.PROVIDER.key("result");
    RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
    RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");
    RecipeKey<Integer> ROW = NumberComponent.INT.key("input_row").preferred("inputRow");
    RecipeKey<Integer> COLUMN = NumberComponent.INT.key("input_column").preferred("inputColumn");
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.UNWRAPPED_INPUT_ARRAY.key("ingredients");
    RecipeKey<InputItem> PRIMARY_INGREDIENT = ItemComponents.INPUT.key("primary_ingredient").optional(InputItem.EMPTY).preferred("primaryIngredient");

    private static RecipeSchema shaped(boolean base) {
        return new RecipeSchema(AdvancedCraftingRecipeJS.class, () -> new AdvancedCraftingRecipeJS(true, base), RESULT, PATTERN, KEY, ROW, COLUMN);
    }
    private static RecipeSchema shapeless(boolean base) {
        return new RecipeSchema(AdvancedCraftingRecipeJS.class, () -> new AdvancedCraftingRecipeJS(false, base), RESULT, INGREDIENTS, PRIMARY_INGREDIENT);
    }

    RecipeSchema SHAPED = shaped(true);
    RecipeSchema SHAPED_CUSTOM = shaped(false);
    RecipeSchema SHAPELESS = shapeless(true);
    RecipeSchema SHAPELESS_CUSTOM = shapeless(false);

    class AdvancedCraftingRecipeJS extends TFCProviderRecipeJS {

        private static RecipeTypeFunction advShaped, advShapeless, kubeAdvShaped, kubeAdvShapeless;

        private final boolean shaped, base;

        public AdvancedCraftingRecipeJS(boolean shaped, boolean base) {
            this.shaped = shaped;
            this.base = base;
        }

        @Override
        public RecipeTypeFunction getSerializationTypeFunction() {
            if (base) {
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
            } else {
                return type;
            }
        }

        @Override
        public List<Ingredient> getOriginalRecipeIngredients() {
            if (shaped && getOriginalRecipe() instanceof AdvancedShapedRecipe adv) {
                return adv.getIngredients();
            } else if (!shaped && getOriginalRecipe() instanceof AdvancedShapelessRecipe adv) {
                return adv.getIngredients();
            } else if (getOriginalRecipe() == null) {
                ConsoleJS.SERVER.warn("Original advanced crafting recipe (%s) is null - could not get ingredients".formatted(getType()));
                return List.of();
            } else {
                return super.getOriginalRecipeIngredients();
            }
        }
    }
}
