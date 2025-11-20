package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonObject;
import net.minecraft.Util;

import java.util.function.Consumer;

public class ResourceUtils {

    public static JsonObject buildJson(Consumer<JsonObject> consumer) {
        return Util.make(new JsonObject(), consumer);
    }
}
