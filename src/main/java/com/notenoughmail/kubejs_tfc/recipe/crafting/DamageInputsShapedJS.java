package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.RecipeUtils;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapedRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DamageInputsShapedJS extends ShapedRecipeJS {

    public JsonObject recipeJson = new JsonObject();

    @Override
    public void create(ListJS listJS) {
        if (listJS.isEmpty()) {
            throw new RecipeExceptionJS("No arguments provided! Requires either a shaped recipe or an output, pattern, and key");
        }

        if (listJS.get(0) instanceof ShapedRecipeJS recipe) {
            recipe.serializeJson();
            recipeJson = recipe.json;
            recipe.dontAdd();

            // Here purely so #getFromToString() works
            RecipeUtils.populateIOFromJson(recipeJson, outputItems, inputItems);
        } else {
            /*
             * Purely so the user doesn't have to write something like
             * event.recipes.tfc.damage_inputs_shaped_crafting(event.shaped('3x minecraft:dark_oak_log', ['minecraft:bucket', Item.of('minecraft:diamond_sword').ignoreNBT()]));
             */
            List<String> pattern = new ArrayList<>();
            List<String> key = new ArrayList<>();

            if (listJS.size() < 3) {
                var vertical = ListJS.orSelf(listJS.get(1));
                if (vertical.isEmpty()) {
                    throw new RecipeExceptionJS("Pattern is empty!");
                }

                var id = 0;
                for (var o : vertical) {
                    var horizontalPattern = new StringBuilder();
                    var horizontal = ListJS.orSelf(o);

                    for (var item : horizontal) {
                        var ingredient = IngredientJS.of(item);

                        if (!ingredient.isEmpty()) {
                            var currentID = String.valueOf((char) ('A' + (id++)));
                            horizontalPattern.append(currentID);
                            inputItems.add(ingredient);
                            key.add(currentID);
                        } else {
                            horizontalPattern.append(" ");
                        }
                    }

                    pattern.add(horizontalPattern.toString());
                }

                var maxLength = pattern.stream().mapToInt(String::length).max().getAsInt();
                var iterator = pattern.listIterator();

                while (iterator.hasNext()) {
                    iterator.set(StringUtils.rightPad(iterator.next(), maxLength));
                }
            } else {
                var pattern1 = ListJS.orSelf(listJS.get(1));
                if (pattern1.isEmpty()) {
                    throw new RecipeExceptionJS("Pattern is empty!");
                }

                List<String> airs = new ArrayList<>();

                var key1 = MapJS.of(listJS.get(2));

                if (key1 == null || key1.isEmpty()) {
                    throw new RecipeExceptionJS("Key map is empty!");
                }

                for (var k : key1.keySet()) {
                    var o = key1.get(k);

                    if (o == ItemStackJS.EMPTY || o.equals("minecraft:air")) {
                        airs.add(k);
                    } else {
                        inputItems.add(parseIngredientItem(o, k));
                        key.add(k);
                    }
                }

                for (var p : pattern1) {
                    var s = String.valueOf(p);

                    for (var s1 : airs) {
                        s = s.replace(s1, " " );
                    }

                    pattern.add(s);
                }
            }

            recipeJson.addProperty("type", "minecraft:crafting_shaped");

            outputItems.add(parseResultItem(listJS.get(0)));

            if (serializeOutputs) {
                recipeJson.add("result", outputItems.get(0).toResultJson());
            }

            if (serializeInputs) {
                var patternJson = new JsonArray();
                for (var s : pattern) {
                    patternJson.add(s);
                }
                recipeJson.add("pattern", patternJson);

                var keyJson = new JsonObject();
                for (var i = 0 ; i < key.size() ; i++) {
                    keyJson.add(key.get(i), inputItems.get(i).toJson());
                }
                recipeJson.add("key", keyJson);
            }
        }
    }

    @Override
    public void deserialize() {
        recipeJson = json.get("recipe").getAsJsonObject();
        RecipeUtils.populateIOFromJson(recipeJson, outputItems, inputItems);
    }

    @Override
    public DamageInputsShapedJS noMirror() {
        recipeJson.addProperty("mirror", false);
        save();
        return this;
    }

    @Override
    public DamageInputsShapedJS noShrink() {
        recipeJson.addProperty("shrink", false);
        save();
        return this;
    }

    @Override
    public void serialize() {
        json.add("recipe", recipeJson);
    }
}