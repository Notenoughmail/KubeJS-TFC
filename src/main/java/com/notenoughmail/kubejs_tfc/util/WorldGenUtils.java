package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * A collection of methods used commonly in worldgen related stuff<br><br>
 *
 * Or more accurately, where I hide all the awful things related to worldgen, so I can forget about them :)
 */
public class WorldGenUtils {

    public static final String notANumber = "[^0-9.-]";

    public static void nullableLenient(JsonObject json, String key, @Nullable String state) {
        ResourceUtils.nullable(json, key, state, WorldGenUtils::blockStateToLenient);
    }

    public static void nullableIntProvider(JsonObject json, String key, @Nullable IntProvider provider) {
        ResourceUtils.nullable(json, key, provider, p -> IntProvider.CODEC.encodeStart(JsonOps.INSTANCE, p).getOrThrow(false, KubeJSTFC::warningLog));
    }

    /**
     * Converts a string representation of a block state into an equivalent lenient block state json element
     */
    public static JsonElement blockStateToLenient(String block, boolean forceExpanded) {
        if (forceExpanded || (block.indexOf('[') != -1 && block.indexOf(']') != -1)) {
            var blockState = block.replace("]", "").split("\\[");
            var states = blockState.length > 1 ? blockState[1].split(",") : new String[0];

            return ResourceUtils.buildJson(json -> {
                json.addProperty("Name", blockState[0]);
                if (states.length > 0) {
                    var properties = new JsonObject();
                    for (String state : states) {
                        var value = state.split("=");
                        properties.addProperty(value[0], value[1]);
                    }
                    json.add("Properties", properties);
                }
            });
        }

        return new JsonPrimitive(block);
    }

    public static JsonElement blockStateToLenient(String block) {
        return blockStateToLenient(block, false);
    }

    /**
     * Converts the string representation of a weighted block state into an entry of a json weighted list
     */
    public static JsonObject weightedBlockState(String value, String type) {
        return weightedJsonListEntry(value, WorldGenUtils::blockStateToLenient, type);
    }

    /**
     * Converts the string representation of a weighted object into an entry of a json weighted list
     */
    public static JsonObject weightedJsonListEntry(String value, Function<String, JsonElement> applicator, String type) {
        return ResourceUtils.buildJson(json -> {
            final String[] biValue = value.split(" ");
            if (biValue.length == 2) {
                json.add(type, applicator.apply(biValue[1]));
                json.addProperty("weight", Float.parseFloat(biValue[0].replaceAll(notANumber, "")));
            } else {
                json.add(type, applicator.apply(biValue[0]));
                json.addProperty("weight", 1F);
            }
        });
    }

    /**
     * Used by:
     * - Boulders
     */
    public record BlockToBlockStatesMapEntry(String block, String[] blockStates) {

        public static void toJson(JsonObject obj, String key, String blockKey, BlockToBlockStatesMapEntry[] entries) {
            final JsonArray array = new JsonArray(entries.length);
            for (var e : entries) array.add(e.toJson(blockKey));
            obj.add(key, array);
        }

        public JsonObject toJson(String blockKey) {
            return ResourceUtils.buildJson(json -> {
                json.addProperty(blockKey, block);
                final JsonArray stateArray = new JsonArray(blockStates.length);
                for (String s: blockStates) stateArray.add(blockStateToLenient(s));
                json.add("blocks", stateArray);
            });
        }
    }

    /**
     * Used by:
     * - Veins
     * - Hot springs
     */
    public record BlockToWeightedBlockStateMapEntry(String[] blocks, String[] blockStates) {

        public static void toJson(JsonObject obj, String key, BlockToWeightedBlockStateMapEntry[] entries) {
            final JsonArray array = new JsonArray(entries.length);
            for (var e : entries) array.add(e.toJson());
            obj.add(key, array);
        }

        public JsonObject toJson() {
            return ResourceUtils.buildJson(json -> {
                final JsonArray blockArray = new JsonArray(blocks.length);
                for (String s : blocks) blockArray.add(s);
                json.add("replace", blockArray);
                final JsonArray stateArray = new JsonArray(blockStates.length);
                for (String s : blockStates) stateArray.add(weightedBlockState(s, "block"));
                json.add("with", stateArray);
            });
        }
    }

    /**
     * Used by:
     * - Soil discs
     */
    public record BlockToBlockStateMapEntry(String block, String state) {

        public static void toJson(JsonObject obj, String key, BlockToBlockStateMapEntry[] entries) {
            final JsonArray array = new JsonArray(entries.length);
            for (var e : entries) array.add(e.toJson());
            obj.add(key, array);
        }

        public JsonObject toJson() {
            return ResourceUtils.buildJson(json -> {
                json.addProperty("replace", block);
                json.add("with", blockStateToLenient(state));
            });
        }
    }

    /**
     * Used by:
     * - Hot springs
     */
    public record FissureDecoration(WorldGenUtils.BlockToWeightedBlockStateMapEntry[] blocks, int rarity, int radius, int count) {

        public JsonObject toJson() {
            return ResourceUtils.buildJson(json -> {
                BlockToWeightedBlockStateMapEntry.toJson(json, "blocks", blocks);
                json.addProperty("rarity", rarity);
                json.addProperty("radius", radius);
                json.addProperty("count", count);
            });
        }
    }

    /**
     * Used by:
     * - Forest
     */
    public record ForestTypesMapEntry(ForestType type, @Nullable IntProvider treeCount, @Nullable IntProvider groundcoverCount, @Nullable Float perChunkChance, @Nullable IntProvider bushCount, @Nullable Boolean hasSpoilerOldGrowth, @Nullable Boolean allowsOldGrowth, @Nullable IntProvider leafPileCount) {

        public void toJson(JsonObject obj) {
            obj.add(type.getSerializedName(), ResourceUtils.buildJson(type -> {
                nullableIntProvider(type, "tree_count", treeCount);
                nullableIntProvider(type, "groundcover_count", groundcoverCount);
                ResourceUtils.nullable(type, "per_chunk_chance", perChunkChance);
                nullableIntProvider(type, "bush_count", bushCount);
                ResourceUtils.nullable(type, "has_spoiler_old_growth", hasSpoilerOldGrowth);
                ResourceUtils.nullable(type, "allows_old_growth", allowsOldGrowth);
                nullableIntProvider(type, "leaf_pile_count", leafPileCount);
            }));
        }
    }

    /**
     * Used by:
     * - Overlay Tree
     * - Random Tree
     * - Stacked Tree
     */
    public record Trunk(String state, int minHeight, int maxHeight, boolean wide) {

        public JsonObject toJson() {
            return ResourceUtils.buildJson(obj -> {
                obj.add("state", blockStateToLenient(state));
                obj.addProperty("min_height", minHeight);
                obj.addProperty("max_height", maxHeight);
                obj.addProperty("wide", wide);
            });
        }
    }

    /**
     * Used by:
     * - Overlay Tree
     * - Random Tree
     * - Stacked Tree
     */
    public record TreePlacement(int width, int height, @Nullable TreePlacementConfig.GroundType groundType) {

        public JsonObject toJson() {
            return ResourceUtils.buildJson(obj -> {
                obj.addProperty("width", width);
                obj.addProperty("height", height);
                ResourceUtils.nullable(obj, "ground_type", groundType);
            });
        }
    }

    /**
     * Used by:
     * - Overlay Tree
     * - Random Tree
     * - Stacked Tree
     */
    public record Root(WorldGenUtils.BlockToWeightedBlockStateMapEntry[] blocks, @Nullable Integer width, @Nullable Integer height, @Nullable Integer tries, @Nullable Float specialPlacerSkewChance, @Nullable Boolean required) {

        public JsonObject toJson() {
            return ResourceUtils.buildJson(obj -> {
                BlockToWeightedBlockStateMapEntry.toJson(obj, "blocks", blocks);
                ResourceUtils.nullable(obj, "width", width);
                ResourceUtils.nullable(obj, "height", height);
                ResourceUtils.nullable(obj, "tries", tries);
                ResourceUtils.nullable(obj, "special_placer", specialPlacerSkewChance, f -> ResourceUtils.buildJson(j -> j.addProperty("skew_chance", f)));
                ResourceUtils.nullable(obj, "required", required);
            });
        }
    }

    /**
     * Used by:
     * - Stacked Tree
     */
    public record TreeLayer(String[] templates, int minCount, int maxCount) {

        public JsonObject toJson() {
            return ResourceUtils.buildJson(obj -> {
                final JsonArray array = new JsonArray(templates.length);
                for (String s : templates) array.add(s);
                obj.add("templates", array);
                obj.addProperty("min_count", minCount);
                obj.addProperty("max_count", maxCount);
            });
        }
    }
}
