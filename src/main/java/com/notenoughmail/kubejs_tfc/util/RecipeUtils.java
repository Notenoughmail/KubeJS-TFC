package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeUtils {

    /**
     * Do <b>not</b> use this for actual handling of a recipe, especially if that recipe has the potential
     * to have an ItemStackProvider output. The sole purpose of this is to populate an extended JS crafting
     * recipe's {@link RecipeJS#getFromToString()}, other uses are bad and will remove critical data.
     */
    public static void populateIOFromJson(JsonObject recipeJson, List<ItemStackJS> outputs, List<IngredientJS> inputs) {
        if (recipeJson.has("recipe")) {
            populateIOFromJson(recipeJson.getAsJsonObject("recipe"), outputs, inputs);
        } else {
            if (recipeJson.has("result")) {
                outputs.add(staticResultParse(recipeJson.get("result")));
            }
            if (recipeJson.has("ingredients")) {
                inputs.addAll(staticIngredientListParse(recipeJson.get("ingredients")));
            }
            if (recipeJson.has("key")) {
                for (Map.Entry<String, JsonElement> entry : recipeJson.get("key").getAsJsonObject().entrySet()) {
                    inputs.add(staticIngredientParse(entry.getValue(), entry.getKey()));
                }
            }
        }
    }

    // Static versions of those in RecipeJS, lacking the fabric check, and simplifying ItemStackProviders
    public static ItemStackJS staticResultParse(JsonElement element) {
        if (element.isJsonObject()) {
            var obj = element.getAsJsonObject();
            if (obj.has("stack") || obj.has("modifiers")) {
                return ItemStackProviderJS.fromJson(obj).getStackJS();
            }
        }
        ItemStackJS result = ItemStackJS.of(element);
        if (result.isInvalidRecipeIngredient()) {
            throw new RecipeExceptionJS(element + " is not a valid result!");
        }
        return result;
    }

    public static List<IngredientJS> staticIngredientListParse(@Nullable Object o) {
        List<IngredientJS> list = new ArrayList<>();
        if (o instanceof JsonElement elem) {
            JsonArray jsonArray;
            if (elem instanceof JsonArray arr) {
                jsonArray = arr;
            } else {
                jsonArray = Util.make(new JsonArray(), (arrx) -> {
                    arrx.add(elem);
                });
            }

            JsonArray array = jsonArray;

            for (JsonElement e : array) {
                list.add(staticIngredientParse(e));
            }
        } else {
            for (Object o1 : ListJS.orSelf(o)) {
                list.add(staticIngredientParse(o1));
            }
        }

        return list;
    }

    public static IngredientJS staticIngredientParse(@Nullable Object o, String key) {
        IngredientJS ingredient = IngredientJS.of(o);
        if (ingredient.isInvalidRecipeIngredient()) {
            if (key.isEmpty()) {
                throw new RecipeExceptionJS(o + " is not a valid ingredient!");
            } else {
                throw new RecipeExceptionJS(o + " with key '" + key + "' is not a valid ingredient!");
            }
        } else {
            return ingredient;
        }
    }

    public static IngredientJS staticIngredientParse(@Nullable Object o) {
        return staticIngredientParse(o, "");
    }
}
