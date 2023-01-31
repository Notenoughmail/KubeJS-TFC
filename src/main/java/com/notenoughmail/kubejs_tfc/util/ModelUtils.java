package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import net.minecraft.resources.ResourceLocation;

public class ModelUtils extends ModelGenerator {

    public static void moldItemModelJson(ModelGenerator m, ResourceLocation id) {
        m.parent("kubejs:item/generated_mold");
        m.textures(moldTextures(id));
    }

    private static JsonObject moldTextures(ResourceLocation id) {
        var json = new JsonObject();
        String nameSpace = id.toString();
        json.addProperty("base", nameSpace + "_empty");
        json.addProperty("fluid", nameSpace + "_overlay");
        return json;
    }
}
