package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.minecraft.Util;
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

    @Info(value = "Adds a placement with the provided type and no extra arguments")
    public PlacedFeatureProperties simplePlacement(String type) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", type);
        placements.add(json);
        return this;
    }

    @Info(value = "Adds the provided json object to the list of placement modifiers")
    public PlacedFeatureProperties jsonPlacement(JsonObject json) {
        placements.add(json);
        return this;
    }

    @Info(value = "Adds the 'tfc:biome' placement modifier")
    public PlacedFeatureProperties tfcBiome() {
        return simplePlacement("tfc:biome");
    }

    @Info(value = "Adds a 'tfc:climate' placement modifier", params = @Param(name = "climate", value = "The climate placement properties"))
    @Generics(value = Climate.class)
    public PlacedFeatureProperties climate(Consumer<Climate> climate) {
        return jsonPlacement(Util.make(new Climate(), climate).toJson());
    }

    @Info(value = "Adds a 'tfc:flat_enough' placement modifier", params = @Param(name = "flatness", value = "The flatness placement properties"))
    @Generics(value = Flatness.class)
    public PlacedFeatureProperties flatEnough(Consumer<Flatness> flatness) {
        return jsonPlacement(Util.make(new Flatness(), flatness).toJson());
    }

    @Info(value = "Adds a 'tfc:near_water' placement modifier", params = @Param(name = "radius", value = "The 'radius' property of the modifier"))
    public PlacedFeatureProperties nearWater(int radius) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:near_water");
        json.addProperty("radius", radius);
        return jsonPlacement(json);
    }

    @Info(value = "Adds a 'tfc:shallow_water' placement modifier", params = @Param(name = "depth", value = "the 'max_depth' property of the modifier"))
    public PlacedFeatureProperties shallowWater(int depth) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:shallow_water");
        json.addProperty("max_depth", depth);
        return jsonPlacement(json);
    }

    @Info(value = "Adds a 'tfc:underground' placement modifier")
    public PlacedFeatureProperties underground() {
        return simplePlacement("tfc:underground");
    }

    @Info(value = "Adds a 'tfc:volcano' placement modifier", params = {
            @Param(name = "center", value = "If true, the feature will be placed at the exact center of the volcano and disregard the 'distance' property"),
            @Param(name = "distance", value = "Sets the distance, in the range [0, 1], from the center of a volcano needed to generate")
    })
    public PlacedFeatureProperties volcano(boolean center, float distance) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:volcano");
        json.addProperty("center", center);
        json.addProperty("distance", distance);
        return jsonPlacement(json);
    }

    // Some of the vanilla modifiers that I can make heads or tails of
    @Info(value = "Adds a 'minecraft:in_square' placement modifier")
    public PlacedFeatureProperties inSquare() {
        return simplePlacement("minecraft:in_square");
    }

    @Info(value = "Adds a 'minecraft:rarity_filter' placement modifier", params = @Param(name = "chance", value = "Sets the 'chance' property of the modifier"))
    public PlacedFeatureProperties rarityFilter(int chance) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:rarity_filter");
        json.addProperty("chance", chance);
        return jsonPlacement(json);
    }

    @Info(value = "Adds a 'minecraft:heightmap' placement modifier", params = @Param(name = "heightMap", value = "Sets t=the 'height_map' property of the modifier"))
    public PlacedFeatureProperties heightMap(String heightMap) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:heightmap");
        json.addProperty("heightmap", heightMap.toUpperCase(Locale.ROOT));
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

        private final JsonObject json;

        public Climate() {
            json = new JsonObject();
            json.addProperty("type", "tfc:climate");
        }

        @Info(value = "Sets the minimum temperature of the climate decorator")
        public Climate minTemp(float f) {
            json.addProperty("min_temperature", f);
            return this;
        }

        @Info(value = "Sets the maximum temperature of the climate decorator")
        public Climate maxTemp(float f) {
            json.addProperty("max_temperature", f);
            return this;
        }

        @Info(value = "Sets the minimum rainfall of the climate decorator")
        public Climate minRain(float f) {
            json.addProperty("min_rainfall", f);
            return this;
        }

        @Info(value = "Sets the maximum rainfall of the climate decorator")
        public Climate maxRain(float f) {
            json.addProperty("max_rainfall", f);
            return this;
        }

        @Info(value = "Sets the minimum forest type of the climate decorator. Accepts 'none', 'sparse', 'edge', 'normal', and 'old_growth'")
        public Climate minForest(ForestType type) {
            json.addProperty("min_forest", type.getSerializedName());
            return this;
        }

        @Info(value = "Sets the maximum forest type of the climate decorator. Accepts 'none', 'sparse', 'edge', 'normal', and 'old_growth'")
        public Climate maxForest(ForestType type) {
            json.addProperty("max_forest", type.getSerializedName());
            return this;
        }

        @Info(value = "Determines if the temperature and rainfall requirements will be probabilistic relative to the center point")
        public Climate fuzzy(boolean b) {
            json.addProperty("fuzzy", b);
            return this;
        }

        public JsonObject toJson() {
            if (!json.has("fuzzy")) {
                json.addProperty("fuzzy", false);
            }
            return json;
        }
    }

    public static class Flatness {

        private float flatness = 0.5f;
        private int radius = 2;
        private int maxDepth = 4;

        @Info(value = "Sets the required flatness of the surrounding area, in the range [0, 1]. Defaults to 0.5")
        public Flatness flatness(float f) {
            flatness = f;
            return this;
        }

        @Info(value = "The radius around the initial position the area is checked for when calculation flatness, defaults to 2")
        public Flatness radius(int i) {
            radius = i;
            return this;
        }

        @Info(value = "How deep from the initial position the decorator should search, defaults to 4")
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
