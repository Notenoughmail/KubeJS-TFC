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

        private static final String[] javelinPerspectives = {"none", "fixed", "ground", "gui"};

        public static void javelin(ResourceLocation id, AssetJsonGenerator generator) {
            final String baseModelLocation = newItemID(id).toString();
            final String throwing = baseModelLocation + "_throwing";
            final String throwingBase = throwing + "_base";
            final String inHand = baseModelLocation + "_in_hand";
            final String gui = baseModelLocation + "_gui";

            final JsonObject guiPerspective = new JsonObject();
            guiPerspective.addProperty("parent", gui);

            // Base model
            {
                final JsonObject model = new JsonObject();
                model.addProperty("loader", "forge:separate_transforms");
                model.addProperty("gui_light", "front");

                final JsonArray overrides = new JsonArray(1);
                final JsonObject override = new JsonObject();
                final JsonObject predicate = new JsonObject();
                predicate.addProperty("tfc:throwing", 1);
                override.add("predicate", predicate);
                override.addProperty("model", throwing);
                overrides.add(override);
                model.add("overrides", overrides);

                final JsonObject base = new JsonObject();
                base.addProperty("parent", inHand);
                model.add("base", base);

                final JsonObject perspectives = new JsonObject();
                for (String s : javelinPerspectives) {
                    perspectives.add(s, guiPerspective);
                }
                model.add("perspectives", perspectives);

                generator.json(AssetJsonGenerator.asItemModelLocation(id), model);
            }

            // Throwing model
            {
                final JsonObject model = new JsonObject();
                model.addProperty("loader", "forge:separate_transforms");
                model.addProperty("gui_light", "front");

                final JsonObject base = new JsonObject();
                base.addProperty("parent", throwingBase);
                model.add("base", base);

                final JsonObject perspectives = new JsonObject();
                for (String s : javelinPerspectives) {
                    perspectives.add(s, guiPerspective);
                }
                model.add("perspectives", perspectives);

                generator.json(newID(AssetJsonGenerator.asItemModelLocation(id), "", "_throwing"), model);
            }

            // Throwing base
            generator.itemModel(newID(id, "", "_throwing_base"), m -> {
                m.parent("item/trident_throwing");
                m.texture("particle", baseModelLocation);
            });

            // In hand model
            generator.itemModel(newID(id, "", "_in_hand"), m -> {
                m.parent("item/trident_in_hand");
                m.texture("particle", baseModelLocation);
            });


            // Gui model
            generator.itemModel(newID(id, "", "_gui"), m -> {
                m.parent("item/generated");
                m.texture("layer0", baseModelLocation);
            });
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

    public static final String[] cardinalDirections = {"north", "east", "south", "west"};
}
