package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.event.TFCDataEventJS;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Helper class used by methods in {@link TFCDataEventJS TFCDataEventJS}
 */
public class DataUtils {

    public static ResourceLocation dataID(ResourceLocation base, String mod, String category) {
        return dataID(base.getNamespace(), base.getPath(), mod, category);
    }

    public static ResourceLocation dataID(String path, String mod, String category) {
        return dataID(KubeJSTFC.MODID, path, mod, category);
    }

    public static ResourceLocation dataIDFromObject(Object path, String mod, String category) {
        return dataID(simplifyObject(path), mod, category);
    }

    public static ResourceLocation dataID(String namespace, String path, String mod, String category) {
        return new ResourceLocation(namespace, mod + "/" + category + "/" + path);
    }

    public static String simplifyObject(Object object) {
        String out;
        if (object instanceof CharSequence s) {
            out = s.toString();
        } else {
            out = getObjectHashString(object);
        }
        out = out.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_") // Make the string not explode when parsed (yes I know there are a few more allowed chars, but it doesn't matter)
                .replaceAll("_+", "_")        // Remove duplicate underscores
                .replaceAll("^_", "")         // Remove leading underscores
                .replaceAll("_$", "");        // Remove trailing underscores
        return out.length() > 64 ? out.substring(0, 64).replaceAll("_$", "") : out; // Limit length to 64 chars
    }

    public static byte[] getObjectHashBytes(Object o) {
        var baos = new ByteArrayOutputStream();
        try {
            new DataOutputStream(baos).writeBytes(o.toString());
        } catch (IOException ignored) {
            var h = o.hashCode();
            return new byte[]{(byte) (h >> 24), (byte) (h >> 16), (byte) (h >> 8), (byte) h};
        }
        return baos.toByteArray();
    }

    public static String getObjectHashString(Object o) {
        try {
            var messageDigest = Objects.requireNonNull(MessageDigest.getInstance("MD5"));
            return new BigInteger(HexFormat.of().formatHex(messageDigest.digest(getObjectHashBytes(o))), 16).toString(36);
        } catch (Exception ignored) {
            return "%08x".formatted(o.hashCode());
        }
    }

    public static void handleResistances(JsonObject json, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) {
        if (piercing != null) {
            json.addProperty("piercing", piercing);
        }
        if (slashing != null) {
            json.addProperty("slashing", slashing);
        }
        if (crushing != null) {
            json.addProperty("crushing", crushing);
        }
    }

    public static void handleFertilizers(JsonObject json, @Nullable Number nitrogen, @Nullable Number phosphorus, @Nullable Number potassium) {
        if (nitrogen != null) {
            json.addProperty("nitrogen", nitrogen);
        }
        if (phosphorus != null) {
            json.addProperty("phosphorus", phosphorus);
        }
        if (potassium != null) {
            json.addProperty("potassium", potassium);
        }
    }

    public static JsonObject buildHeat(Ingredient ingredient, float heatCap, @Nullable Float forgeTemp, @Nullable Float weldTemp) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("heat_capacity", heatCap);
        if (forgeTemp != null) {
            json.addProperty("forging_temperature", forgeTemp);
        }
        if (weldTemp != null) {
            json.addProperty("welding_temperature", weldTemp);
        }
        return json;
    }

    // Should have been this way from the beginning
    public static void handleItemSize(JsonObject json, @Nullable Size size, @Nullable Weight weight) {
        if (size != null) {
            json.addProperty("size", size.name);
        }
        if (weight != null) {
            json.addProperty("weight", weight.name);
        }
    }

    public static JsonObject knappingType(Ingredient ingredient, int ingredientCount, int amountToConsume, ResourceLocation clickSound, boolean consumeAfterComplete, boolean useDisabledTexture, boolean spawnsParticles, ItemStack jeiIconItem) {
        final JsonObject isi = new JsonObject();
        isi.add("ingredient", ingredient.toJson());
        isi.addProperty("count", ingredientCount);
        final JsonObject json = new JsonObject();
        json.add("input", isi);
        json.addProperty("amount_to_consume", amountToConsume);
        json.addProperty("click_sound", clickSound.toString());
        json.addProperty("consume_after_complete", consumeAfterComplete);
        json.addProperty("use_disabled_texture", useDisabledTexture);
        json.addProperty("spawns_particles", spawnsParticles);
        json.add("jei_icon_item", IngredientHelpers.itemStackToJson(jeiIconItem));
        return json;
    }

    public static JsonObject makeMetal(String fluid, float meltTemp, float heatCap, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier) {
        var json = new JsonObject();
        json.addProperty("tier", tier);
        json.addProperty("fluid", fluid);
        json.addProperty("melt_temperature", meltTemp);
        json.addProperty("specific_heat_capacity", heatCap);
        if (ingot != null) {
            json.add("ingots", ingot.toJson());
        }
        if (doubleIngot != null) {
            json.add("double_ingots", doubleIngot.toJson());
        }
        if (sheet != null) {
            json.add("sheets", sheet.toJson());
        }
        return json;
    }

    /*
     * See comment in TFCDataEventJS
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
    */

    // "worldgen" is my favorite mod!
    public static ResourceLocation configuredFeatureName(String path) {
        return dataID(normalizeResourceLocation(path), "worldgen", "configured_feature");
    }

    public static ResourceLocation placedFeatureName(String path) {
        return dataID(normalizeResourceLocation(path), "worldgen", "placed_feature");
    }

    public static ResourceLocation normalizeResourceLocation(String resourceLocation) {
        if (resourceLocation.lastIndexOf(":") != -1) {
            return new ResourceLocation(resourceLocation);
        }
        return KubeJSTFC.identifier(resourceLocation);
    }
}
