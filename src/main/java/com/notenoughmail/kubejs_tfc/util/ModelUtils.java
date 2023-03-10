package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import net.minecraft.resources.ResourceLocation;

public class ModelUtils {

    public static class ITEMS {
        public static void moldItemModelJson(ModelGenerator m, ResourceLocation id) {
            m.parent("kubejs:item/generated_mold");
            m.textures(moldTextures(id));
        }

        private static JsonObject moldTextures(ResourceLocation id) {
            var json = new JsonObject();
            var nameSpace = newItemID(id).toString();
            json.addProperty("base", nameSpace + "_empty");
            json.addProperty("fluid", nameSpace + "_overlay");
            return json;
        }

        public static JsonObject javelinItemModelJson(ResourceLocation id) {
            var javelin = new JsonObject();
            javelin.addProperty("loader", "forge:separate-perspective");
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

        public static JsonObject fishingRodModelJson(ResourceLocation id, boolean diff) {
            var rod = new JsonObject();
            rod.addProperty("parent", "minecraft:item/handheld_rod");

            var texture = newItemID(id).toString();
            var textures = new JsonObject();
            textures.addProperty("layer0", texture);
            rod.add("textures", textures);

            var overrides = new JsonArray();
            var predicates = new JsonObject();
            var predicate = new JsonObject();
            var predTexture = new JsonObject();
            predicate.addProperty("tfc:cast", 1);
            predicates.add("predicate", predicate);
            predicates.addProperty("model", "kubejs:item/generated_fishing_rod"); // This currently doesn't do anything because who-knows-why
            predTexture.addProperty("tex", diff ? texture + "_cast" : "item/fishing_rod_cast"); // TODO: Fix this
            predicates.add("textures", predTexture);
            overrides.add(predicates);
            rod.add("overrides", overrides);

            System.out.println(rod);
            return rod;
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
