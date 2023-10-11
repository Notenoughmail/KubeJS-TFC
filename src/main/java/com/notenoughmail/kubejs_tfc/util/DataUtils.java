package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

/**
 * Helper class used by methods in {@link com.notenoughmail.kubejs_tfc.util.implementation.mixin.DataPackEventJSMixin DataPackEventJSMixin}
 */
public class DataUtils {

    private static final String notANumber = "[^0-9.-]";
    private static final String splitters = "[,;:]";

    public static ResourceLocation dataID(ResourceLocation base, String mod, String category) {
        return dataID(base.getNamespace(), base.getPath(), mod, category);
    }

    public static ResourceLocation dataID(String path, String mod, String category) {
        return dataID("kubejs_tfc", path, mod, category);
    }

    public static ResourceLocation dataID(String namespace, String path, String mod, String category) {
        return new ResourceLocation(namespace, mod + "/" + category + "/" + path);
    }

    public static String simplifyObject(Object object) {
        String out = object.toString().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_") // Make the string not explode when parsed (yes I know there are a few more allowed chars, but it doesn't matter)
                .replaceAll("_+", "_")        // Remove duplicate underscores
                .replaceAll("^_", "")         // Remove leading underscores
                .replaceAll("_$", "");        // Remove trailing underscores
        return out.length() > 64 ? out.substring(0, 64) : out; // Limit length to 64 chars
    }

    public static void handleResistances(String values, JsonObject objToAddTo) {
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(3, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.matches("p(?>iercing)?.+")) {
                objToAddTo.addProperty("piercing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("s(?>lashing)?.+")) {
                objToAddTo.addProperty("slashing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("c(?>rushing)?.+")) {
                objToAddTo.addProperty("crushing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(p|s|c)=\\d+/");
            }
        }
    }

    public static void handleFertilizers(String values, JsonObject objToAddTo) {
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(3, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.charAt(0) == 'n' || value.matches("nitrogen.+")) {
                objToAddTo.addProperty("nitrogen",  Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'p' || value.matches("phosphorus.+")) {
                objToAddTo.addProperty("phosphorus", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'k' || value.matches("potassium.+")) {
                objToAddTo.addProperty("potassium", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(n|p|k)=\\d*.\\d+/");
            }
        }
    }

    public static JsonObject baseHeat(IngredientJS ingredient, float heatCapacity) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("heat_capacity", heatCapacity);
        return json;
    }

    public static void handleItemSize(String values, JsonObject objToAddTo) {
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(2, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.matches("s(?>ize)?=.+")) {
                var constant = value.split("=");
                if (!constant[1].matches("(?>tiny|(?>very_)?(?>small|large)|normal|huge)")) {
                    ConsoleJS.SERVER.error("Size value cannot be '" + constant[1] + "', must be tiny, very_small, small, normal, large, very_large, or huge!");
                } else {
                    objToAddTo.addProperty("size", constant[1]);
                }
            } else if (value.matches("w(?>eight)?=.+")) {
                var constant = value.split("=");
                if (!constant[1].matches("(?>(?>very_)?(?>light|heavy)|medium)")) {
                    ConsoleJS.SERVER.error("Weight value cannot be '" + constant[1] + "', it must be very_light, light, medium, heavy, or very_heavy!");
                } else {
                    objToAddTo.addProperty("weight", constant[1]);
                }
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(s=(tiny|very_small|small|normal|large|very_large|huge)|w=(very_light|light|medium|heavy|very_heavy))/");
            }
        }
    }

    public static JsonObject makeMetal(String fluid, float meltTemp, float heatCap, IngredientJS ingot, IngredientJS sheet, int tier) {
        var ingotJS = ingot.unwrapStackIngredient().get(0);
        var sheetJS = sheet.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.addProperty("tier", tier);
        json.addProperty("fluid", fluid);
        json.addProperty("melt_temperature", meltTemp);
        json.addProperty("specific_heat_capacity", heatCap);
        json.add("ingots", ingotJS.toJson());
        json.add("sheets", sheetJS.toJson());
        return json;
    }

    public static void handleNetherFertilizers(String values, JsonObject objToAddTo) {
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(5, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.charAt(0) == 'd' || value.matches("death.+")) {
                objToAddTo.addProperty("death", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 't' || value.matches("destruction.+")) {
                objToAddTo.addProperty("destruction", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'f' || value.matches("flame.+")) {
                objToAddTo.addProperty("flame", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'c' || value.matches("decay.+")) {
                objToAddTo.addProperty("decay", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 's' || value.matches("sorrow.+")) {
                objToAddTo.addProperty("sorrow", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! the value should match /(d|t|f|c|s)=\\d*.\\d+/");
            }
        }
    }

    // "worldgen" is my favorite mod!
    public static ResourceLocation configuredFeatureName(String path) {
        return dataID(path, "worldgen", "configured_feature");
    }

    public static ResourceLocation placedFeatureName(String path) {
        return dataID(path, "worldgen", "placed_feature");
    }
}
