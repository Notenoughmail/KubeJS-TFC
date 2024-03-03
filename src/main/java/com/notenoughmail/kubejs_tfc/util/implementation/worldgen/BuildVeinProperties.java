package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public abstract class BuildVeinProperties {

    // Common values
    private final List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> blocks;
    @Nullable
    private JsonObject indicator;
    private final int rarity;
    private final float density;
    private final int minY;
    private final int maxY;
    @Nullable
    private Boolean project;
    @Nullable
    private Boolean projectOffset;
    private final String random_name;

    @Nullable
    private String biomes;
    @Nullable
    private Boolean nearLava;

    public BuildVeinProperties(List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> blocks, int rarity, float density, int minY, int maxY, String randomName) {
        this.blocks = blocks;
        this.rarity = rarity;
        this.density = density;
        this.minY = minY;
        this.maxY = maxY;
        random_name = randomName;
    }

    @Info(value = "Adds the 'indicator' property to the vein", params = {
            @Param(name = "depth", value = "Defines how many blocks above the top of the vein the indicators may spawn"),
            @Param(name = "rarity", value = "Sets the rarity of the indicator blocks"),
            @Param(name = "undergroundRarity", value = "Sets the rarity of the indicator blocks when underground"),
            @Param(name = "indicators", value = "A list of string representations of weighted block states, the blocks to be used as indicators")
    })
    @Generics(value = String.class)
    public BuildVeinProperties indicator(int depth, int rarity, int undergroundRarity, int undergroundCount, List<String> indicators) {
        final JsonObject indicatorJson = new JsonObject();
        indicatorJson.addProperty("depth", depth);
        indicatorJson.addProperty("rarity", rarity);
        indicatorJson.addProperty("underground_rarity", undergroundRarity);
        indicatorJson.addProperty("underground_count", undergroundCount);
        final JsonArray blocks = new JsonArray(indicators.size());
        for (String s: indicators) {
            blocks.add(WorldGenUtils.weightedBlockState(s, "block"));
        }
        indicatorJson.add("blocks", blocks);
        indicator = indicatorJson;
        return this;
    }

    @Info(value = "Determines if the vein should project itself to the surface, defaults to false")
    public BuildVeinProperties project(boolean b) {
        project = b;
        return this;
    }

    @Info(value = "Determines if the projection of the vein should be offset in the x and z directions, defaults to false")
    public BuildVeinProperties projectOffset(boolean b) {
        projectOffset = b;
        return this;
    }

    @Info(value = "Determines which biomes the vein may spawn in", params = @Param(name = "biomeTag", value = "The biome tag the vein may spawn in"))
    public BuildVeinProperties biomes(String biomeTag) {
        if (biomeTag.charAt(0) == '#') {
            this.biomes = biomeTag;
        } else {
            this.biomes = "#".concat(biomeTag);
        }
        return this;
    }

    @Info(value = "Determines if the vein should be near lava in order to spawn")
    public BuildVeinProperties nearLava(boolean b) {
        nearLava = b;
        return this;
    }

    protected JsonObject baseConfig() {
        final JsonObject config = new JsonObject();
        final JsonArray blocksArray = new JsonArray(blocks.size());
        blocks.forEach(entry -> blocksArray.add(entry.toJson()));
        config.add("blocks", blocksArray);
        if (indicator != null) {
            config.add("indicator", indicator);
        }
        config.addProperty("rarity", rarity);
        config.addProperty("density", density);
        config.addProperty("min_y", minY);
        config.addProperty("max_y", maxY);
        if (project != null) {
            config.addProperty("project", project);
        }
        if (projectOffset != null) {
            config.addProperty("project_offset", projectOffset);
        }
        config.addProperty("random_name", random_name);
        if (biomes != null) {
            config.addProperty("biomes", biomes);
        }
        if (nearLava != null) {
            config.addProperty("near_lava", nearLava);
        }
        return config;
    }

    public abstract JsonObject toJson();

    public static class Cluster extends BuildVeinProperties {

        private final int size;

        public Cluster(List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> blocks, int rarity, float density, int minY, int maxY, String randomName, int size) {
            super(blocks, rarity, density, minY, maxY, randomName);
            this.size = size;
        }

        @Override
        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("type", "tfc:cluster_vein");
            final JsonObject config = baseConfig();
            config.addProperty("size", size);
            json.add("config", config);
            return json;
        }
    }

    public static class Pipe extends BuildVeinProperties {

        private final int height;
        private final int radius;
        private final int minSkew;
        private final int maxSkew;
        private final int minSlant;
        private final int maxSlant;
        private final float sign;

        public Pipe(List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> blocks, int rarity, float density, int minY, int maxY, String randomName, int height, int radius, int minSkew, int maxSkew, int minSlant, int maxSlant, float sign) {
            super(blocks, rarity, density, minY, maxY, randomName);
            this.height = height;
            this.radius = radius;
            this.minSkew = minSkew;
            this.maxSkew = maxSkew;
            this.minSlant = minSlant;
            this.maxSlant = maxSlant;
            this.sign = sign;
        }

        @Override
        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("type", "tfc:pipe_vein");
            final JsonObject config = baseConfig();
            config.addProperty("height", height);
            config.addProperty("radius", radius);
            config.addProperty("min_skew", minSkew);
            config.addProperty("max_skew", maxSkew);
            config.addProperty("min_slant", minSlant);
            config.addProperty("max_slant", maxSlant);
            config.addProperty("sign", sign);
            json.add("config", config);
            return json;
        }
    }

    public static class Disc extends BuildVeinProperties {

        private final int size;
        private final int height;

        public Disc(List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> blocks, int rarity, float density, int minY, int maxY, String randomName, int size, int height) {
            super(blocks, rarity, density, minY, maxY, randomName);
            this.size = size;
            this.height = height;
        }

        @Override
        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("type", "tfc:disc_vein");
            final JsonObject config = baseConfig();
            config.addProperty("size", size);
            config.addProperty("height", height);
            json.add("config", config);
            return json;
        }
    }
}
