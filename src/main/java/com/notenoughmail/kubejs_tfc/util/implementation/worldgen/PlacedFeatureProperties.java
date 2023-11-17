package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.NativeObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PlacedFeatureProperties {

    private final String feature;
    private final List<JsonObject> placements = new ArrayList<>();

    public PlacedFeatureProperties(String name) {
        feature = DataUtils.normalizeResourceLocation(name).toString();
    }

    public PlacedFeatureProperties simplePlacement(String type) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", type);
        placements.add(json);
        return this;
    }

    public PlacedFeatureProperties jsonPlacement(JsonObject json) {
        placements.add(json);
        return this;
    }

    public PlacedFeatureProperties tfcBiome() {
        return simplePlacement("tfc:biome");
    }

    public PlacedFeatureProperties climate(Consumer<Climate> climate) {
        var placer = new Climate();
        climate.accept(placer);
        return jsonPlacement(placer.toJson());
    }

    public PlacedFeatureProperties flatEnough(Consumer<Flatness> flatness) {
        var placer = new Flatness();
        flatness.accept(placer);
        return jsonPlacement(placer.toJson());
    }

    public PlacedFeatureProperties nearWater(int i) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:near_water");
        json.addProperty("radius", i);
        return jsonPlacement(json);
    }

    public PlacedFeatureProperties shallowWater() {
        return shallowWater(3);
    }

    public PlacedFeatureProperties shallowWater(int depth) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:shallow_water");
        json.addProperty("max_depth", depth);
        return jsonPlacement(json);
    }

    public PlacedFeatureProperties underground() {
        return simplePlacement("tfc:underground");
    }

    public PlacedFeatureProperties volcano(float distance) {
        return volcano(false, distance);
    }

    public PlacedFeatureProperties volcano(boolean center, float distance) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:volcano");
        json.addProperty("center", center);
        json.addProperty("distance", distance);
        return jsonPlacement(json);
    }

    // Some of the vanilla modifiers that I can make heads or tails of
    public PlacedFeatureProperties inSquare() {
        return simplePlacement("minecraft:in_square");
    }

    public PlacedFeatureProperties rarityFilter(int i) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:rarity_filter");
        json.addProperty("chance", i);
        return jsonPlacement(json);
    }

    public PlacedFeatureProperties heightMap(String s) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:heightmap");
        json.addProperty("height_map", s.toUpperCase(Locale.ROOT));
        return jsonPlacement(json);
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("feature", feature);
        var array = new JsonArray();
        for (var obj : placements) {
            array.add(obj);
        }
        json.add("placement", array);
        return json;
    }

    public static class Climate {

        @Nullable
        private Float minTemp;
        @Nullable
        private Float maxTemp;
        @Nullable
        private Float minRain;
        @Nullable
        private Float maxRain;
        @Nullable
        private String minForest;
        @Nullable
        private String maxForest;
        private boolean fuzzy = false;

        public Climate minTemp(float f) {
            minTemp = f;
            return this;
        }

        public Climate maxTemp(float f) {
            maxTemp = f;
            return this;
        }

        public Climate minRain(float f) {
            minRain = f;
            return this;
        }

        public Climate maxRain(float f) {
            maxRain = f;
            return this;
        }

        public Climate minForest(String s) {
            minForest = s;
            return this;
        }

        public Climate maxForest(String s) {
            maxForest = s;
            return this;
        }

        public Climate fuzzy(boolean b) {
            fuzzy = b;
            return this;
        }

        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("type", "tfc:climate");
            if (minTemp != null) {
                json.addProperty("min_temperature", minTemp);
            }
            if (maxTemp != null) {
                json.addProperty("max_temperature", maxTemp);
            }
            if (minRain != null) {
                json.addProperty("min_rainfall", minRain);
            }
            if (maxRain != null) {
                json.addProperty("max_rainfall", maxRain);
            }
            if (minForest != null) {
                json.addProperty("min_forest", minForest);
            }
            if (maxForest != null) {
                json.addProperty("max_forest", maxForest);
            }
            json.addProperty("fuzzy", fuzzy);
            return json;
        }
    }

    public static class Flatness {

        private float flatness = 0.5f;
        private int radius = 2;
        private int maxDepth = 4;

        public Flatness flatness(float f) {
            flatness = f;
            return this;
        }

        public Flatness radius(int i) {
            radius = i;
            return this;
        }

        public Flatness maxDepth(int i) {
            maxDepth = i;
            return this;
        }

        public JsonObject toJson() {
            var json = new JsonObject();
            json.addProperty("type", "tfc:flat_enough");
            json.addProperty("flatness", flatness);
            json.addProperty("radius", radius);
            json.addProperty("max_depth", maxDepth);
            return json;
        }
    }
}
