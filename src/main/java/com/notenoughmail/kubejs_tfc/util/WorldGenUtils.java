package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.typings.Generics;

import java.util.List;
import java.util.function.Function;

/**
 * A collection of methods used commonly in worldgen related stuff<br><br>
 *
 * Or more accurately, where I hide all the awful things related to worldgen, so I can forget about them :)
 */
public class WorldGenUtils {

    /**
     * If TFC's worldgen has been transformed, used to prevent {@code TFCEvents.defaultWorldSettings} from firing a
     * second time when the worlds saved settings are loaded from disk
     * <p>
     * This gets reset to false once Forge's {@link net.minecraftforge.event.server.ServerAboutToStartEvent ServerAboutToStart}
     * event fires
     */
    public static boolean worldgenHasBeenTransformed = false;

    public static final String notANumber = "[^0-9.-]";

    /**
     * Converts a string representation of a block state into an equivalent lenient block state json element
     */
    public static JsonElement blockStateToLenient(String block) {
        if (block.indexOf('[') != -1 && block.indexOf(']') != -1) {
            var blockState = block.replace("]", "").split("\\[");
            var states = blockState[1].split(",");

            var json = new JsonObject();
            json.addProperty("Name", blockState[0]);
            var properties = new JsonObject();
            for (String state : states) {
                var value = state.split("=");
                properties.addProperty(value[0], value[1]);
            }
            json.add("Properties", properties);
            return json;
        }

        return new JsonPrimitive(block);
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
        final JsonObject json = new JsonObject();
        final String[] biValue = value.split(" ");
        if (biValue.length == 2) {
            json.add(type, applicator.apply(biValue[1]));
            json.addProperty("weight", Float.parseFloat(biValue[0].replaceAll(notANumber, "")));
        } else {
            json.add(type, applicator.apply(biValue[0]));
            json.addProperty("weight", 1F);
        }
        return json;
    }

    /**
     * Used by:
     * - Boulders
     */
    @Generics(value = String.class)
    public record BlockToBlockStatesMapEntry(String block, List<String> blockStates) {

        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("rock", block);
            final JsonArray stateArray = new JsonArray(blockStates.size());
            blockStates.forEach(s -> stateArray.add(blockStateToLenient(s)));
            json.add("blocks", stateArray);
            return json;
        }
    }

    /**
     * Used by:
     * - Veins
     * - Hot springs
     */
    @Generics(value = {String.class, String.class})
    public record BlockToWeightedBlockStateMapEntry(List<String> blocks, List<String> blockStates) {

        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            final JsonArray blockArray = new JsonArray(blocks.size());
            blocks.forEach(blockArray::add);
            json.add("replace", blockArray);
            final JsonArray stateArray = new JsonArray(blockStates.size());
            blockStates.forEach(s -> stateArray.add(weightedBlockState(s, "block")));
            json.add("with", stateArray);
            return json;
        }
    }

    /**
     * Used by:
     * - Soil discs
     */
    public record BlockToBlockStateMapEntry(String block, String state) {

        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("replace", block);
            json.add("with", blockStateToLenient(state));
            return json;
        }
    }

    /**
     * Used by:
     * - Hot springs
     */
    public record FissureDecoration(List<BlockToWeightedBlockStateMapEntry> blocks, int rarity, int radius, int count) {

        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            final JsonArray blocksArray = new JsonArray(blocks.size());
            blocks.forEach(entry -> blocksArray.add(entry.toJson()));
            json.add("blocks", blocksArray);
            json.addProperty("rarity", rarity);
            json.addProperty("radius", radius);
            json.addProperty("count", count);
            return json;
        }
    }
}
