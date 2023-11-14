package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.util.TinyMap;
import net.dries007.tfc.common.recipes.AlloyRecipe;
import net.dries007.tfc.util.Metal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO: this is wrong
public record AlloyPartsComponent(RecipeComponent<String> metal, RecipeComponent<Double> min, RecipeComponent<Double> max) implements RecipeComponent<TinyMap<String, AlloyRecipe.Range>> {

    public static final AlloyPartsComponent ALLOY = new AlloyPartsComponent(StringComponent.ID, NumberComponent.doubleRange(0, 1), NumberComponent.doubleRange(0, 1));

    @Override
    public Class<?> componentClass() {
        return TinyMap.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, TinyMap<String, AlloyRecipe.Range> value) {
        JsonArray array = new JsonArray(value.entries().length);
        Arrays.stream(value.entries()).toList().forEach(entry -> {
            JsonObject json = new JsonObject();
            json.addProperty("metal", entry.key());
            json.addProperty("min", entry.value().min());
            json.addProperty("max", entry.value().max());
            array.add(json);
        });
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TinyMap<String, AlloyRecipe.Range> read(RecipeJS recipe, Object from) {
        if (from instanceof TinyMap<?,?> map) {
            return (TinyMap<String, AlloyRecipe.Range>) map;
        } else if (from instanceof JsonElement json) {
            Map<String, AlloyRecipe.Range> map = new HashMap<>();
            if (json.isJsonArray()) {
                json.getAsJsonArray().forEach(element -> {
                    if (element.isJsonObject()) {
                        JsonObject obj = element.getAsJsonObject();
                        map.put(obj.get("metal").getAsString(), new AlloyRecipe.Range(obj.get("min").getAsDouble(), obj.get("max").getAsDouble()));
                    }
                });
            } else if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                map.put(obj.get("metal").getAsString(), new AlloyRecipe.Range(obj.get("min").getAsDouble(), obj.get("max").getAsDouble()));
            } else if (json.isJsonPrimitive()) {
                map.put(json.getAsString(), new AlloyRecipe.Range(0, 1));
            }
            return TinyMap.ofMap(map);
        } else if (from instanceof Map<?,?> map) {
            return (TinyMap<String, AlloyRecipe.Range>) TinyMap.ofMap(map);
        } else {
            return unknownSingleton();
        }
    }

    private static TinyMap<String, AlloyRecipe.Range> unknownSingleton() {
        TinyMap.Entry<String, AlloyRecipe.Range> singleton = new TinyMap.Entry<>(Metal.UNKNOWN_ID.toString(), new AlloyRecipe.Range(0, 1));
        return new TinyMap<>(Collections.singleton(singleton));
    }
}
