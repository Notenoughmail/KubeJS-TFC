package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.minecraft.resources.ResourceLocation;

public class ModelUtils {

    public static class ITEMS {
        public static void fluidContainerModelJson(ModelGenerator m, ResourceLocation id) {
            m.parent("kubejs_tfc:item/generated_fluid_container");
            m.textures(fluidContainerTextures(id));
        }

        private static JsonObject fluidContainerTextures(ResourceLocation id) {
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

        public static void fishingRod(ResourceLocation id, AssetJsonGenerator generator, String customCastModel) {

            final JsonObject rod = new JsonObject();
            rod.addProperty("parent", "minecraft:item/handheld_rod");
            final JsonObject rodTextures = new JsonObject();
            rodTextures.addProperty("layer0", newItemID(id).toString());
            rod.add("textures", rodTextures);
            final JsonObject predicate = new JsonObject();
            final JsonObject castPredicate = new JsonObject();
            castPredicate.addProperty("tfc:cast", 1);
            predicate.add("predicate", castPredicate);
            predicate.addProperty("model", customCastModel.isEmpty() ? newItemID(id) + "_cast" : customCastModel);
            final JsonArray overrides = new JsonArray(1);
            overrides.add(predicate);
            rod.add("overrides", overrides);

            generator.json(AssetJsonGenerator.asItemModelLocation(id), rod);

            if (customCastModel.isEmpty()) {
                final JsonObject cast = new JsonObject();
                cast.addProperty("parent", "item/fishing_rod");
                final JsonObject castTextures = new JsonObject();
                castTextures.addProperty("layer0", newItemID(id) + "_cast");
                cast.add("textures", castTextures);

                generator.json(AssetJsonGenerator.asItemModelLocation(newID(id, "", "_cast")), cast);
            }
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
