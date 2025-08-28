package com.notenoughmail.kubejs_tfc.recipe.js;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.recipes.BloomeryRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BloomeryRecipeJS extends TFCProviderRecipeJS {

    @Override
    public JsonElement writeInputItem(InputItem value) {
        return IngredientHelpers.inputItemToItemStackIngredient(value);
    }

    @Override
    public List<Ingredient> getOriginalRecipeIngredients() {
        if (getOriginalRecipe() instanceof BloomeryRecipe b) {
            return List.of(b.getCatalyst().ingredient());
        } else if (getOriginalRecipe() == null) {
            ConsoleJS.SERVER.warn("Original bloomery recipe is null - could not get ingredients");
            return List.of();
        } else {
            throw new IllegalStateException("Original recipe was not a bloomery recipe?");
        }
    }
}
