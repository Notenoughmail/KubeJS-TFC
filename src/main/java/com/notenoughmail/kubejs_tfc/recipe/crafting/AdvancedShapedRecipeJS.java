package com.notenoughmail.kubejs_tfc.recipe.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.minecraft.ShapedRecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdvancedShapedRecipeJS extends ShapedRecipeJS {

    private ItemStackProviderJS itemProviderResult;
    private final List<String> pattern = new ArrayList<>();
    private final List<String> key = new ArrayList<>();
    private int row;
    private int column;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 4) {
            throw new RecipeExceptionJS("Requires five arguments - result, pattern, key, input row, and input column");
        }

        itemProviderResult = ItemStackProviderJS.of(listJS.get(0));

        if (listJS.size() < 5) {
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

            row = (int) Float.parseFloat(listJS.get(2).toString());
            column = (int) Float.parseFloat(listJS.get(3).toString());
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
                    s = s.replace(s1, " ");
                }

                pattern.add(s);
            }

            row = (int) Float.parseFloat(listJS.get(3).toString());
            column = (int) Float.parseFloat(listJS.get(4).toString());
        }
    }

    @Override
    public void deserialize() {
        itemProviderResult = ItemStackProviderJS.fromJson(json.get("result").getAsJsonObject());
        for (var e : json.get("pattern").getAsJsonArray()) {
            pattern.add(e.getAsString());
        }
        for (var entry : json.get("key").getAsJsonObject().entrySet()) {
            inputItems.add(parseIngredientItem(entry.getValue(), entry.getKey()));
            key.add(entry.getKey());
        }
        row = json.get("input_row").getAsInt();
        column = json.get("input_column").getAsInt();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemProviderResult.toJson());
        }

        if (serializeInputs) {
            var patternJson = new JsonArray();
            for (var s : pattern) {
                patternJson.add(s);
            }
            json.add("pattern", patternJson);

            var keyJson = new JsonObject();
            for (int i = 0 ; i < key.size() ; i++) {
                keyJson.add(key.get(i), inputItems.get(i).toJson());
            }
            json.add("key", keyJson);

            json.addProperty("input_row", row);
            json.addProperty("input_column", column);
        }
    }

    @Override
    public String getFromToString() {
        return inputItems + " -> " + itemProviderResult;
    }
}
