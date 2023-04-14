package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class DamageInputsShapelessJS extends ShapelessRecipeJS {

    private JsonObject recipeJson;

    @Override
    public void create(ListJS listJS) {
        if (listJS.isEmpty()) {
            throw new RecipeExceptionJS("No arguments provided! Requires either a shapeless recipe or an output and ingredients");
        }

        if (listJS.get(0) instanceof ShapelessRecipeJS recipe) {
            recipe.serializeJson();
            recipeJson = recipe.json;
            recipe.dontAdd();

            // Here purely so #getFromToString() works
            outputItems.addAll(recipe.outputItems);
            inputItems.addAll(recipe.inputItems);
        } else {
            /*
             * Purely so the user doesn't have to write something like
             * event.recipes.tfc.damage_inputs_shapeless_crafting(event.shapeless('3x minecraft:dark_oak_log', ['minecraft:bucket', Item.of('minecraft:diamond_sword').ignoreNBT()]));
             */
            outputItems.add(parseResultItem(listJS.get(0)));
            inputItems.addAll(parseIngredientItemList(listJS.get(1)));

            recipeJson = new JsonObject();
            recipeJson.addProperty("type", "minecraft:crafting_shapeless");

            if (serializeOutputs) {
                recipeJson.add("result", outputItems.get(0).toResultJson());
            }

            if (serializeInputs) {
                var ingredients = new JsonArray();
                for (var item : inputItems) {
                    for (var item1 : item.unwrapStackIngredient()) {
                        ingredients.add(item1.toJson());
                    }
                }
                recipeJson.add("ingredients", ingredients);
            }
        }
    }

    @Override
    public void deserialize() {
        recipeJson = json.get("recipe").getAsJsonObject();
    }

    @Override
    public void serialize() {
        json.add("recipe", recipeJson);
    }
}
