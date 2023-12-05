package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import net.minecraft.resources.ResourceLocation;

public class ModelUtils {

    public static class ITEMS {
        public static void fluidContainerModelJson(ModelGenerator m, ResourceLocation id) {
            m.parent("kubejs_tfc:item/generated_fluid_container");
            m.textures(moldTextures(id));
        }

        private static JsonObject moldTextures(ResourceLocation id) {
            var json = new JsonObject();
            var nameSpace = newItemID(id).toString();
            json.addProperty("base", nameSpace);
            json.addProperty("fluid", nameSpace + "_overlay");
            return json;
        }

        // TODO: Check if this is still how the models are
        public static JsonObject javelinItemModelJson(ResourceLocation id) {
            var javelin = new JsonObject();
            javelin.addProperty("loader", "forge:separate_transforms");
            javelin.addProperty("gui_light", "front");

            var texture = newItemID(id).toString();
            var textures = new JsonObject();
            textures.addProperty("particle", texture);
            textures.addProperty("layer0", texture);

            var overrides = new JsonArray();
            var predicates = new JsonObject();
            var predicate = new JsonObject();
            predicate.addProperty("tfc:throwing", 1);
            predicates.add("predicate", predicate);
            predicates.addProperty("model", "item/trident_throwing");
            predicates.add("textures", textures);
            overrides.add(predicates);
            javelin.add("overrides", overrides);

            var base = new JsonObject();
            base.addProperty("parent", "item/trident_in_hand");
            base.add("textures", textures);
            javelin.add("base", base);

            var perspectives = new JsonObject();
            var gui = new JsonObject();
            gui.addProperty("parent", "item/generated");
            gui.add("textures", textures);
            perspectives.add("none", gui);
            perspectives.add("fixed", gui);
            perspectives.add("ground", gui);
            perspectives.add("gui", gui);
            javelin.add("perspectives", perspectives);

            return javelin;
        }

        private static ResourceLocation newItemID(ResourceLocation id) {
            return newID(id, "item/", "");
        }
    }

    public static class BLOCKS {
        // Needed because apparently the 'textures' JsonObject doesn't include particles
        public static JsonObject addTextureAndParticle(String texture) {
            var json = new JsonObject();
            json.addProperty("texture", texture);
            json.addProperty("particle", texture);
            return json;
        }
    }

    public static ResourceLocation newID(ResourceLocation id, String pre, String post) {
        return new ResourceLocation(id.getNamespace(), pre + id.getPath() + post);
    }
}
