package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;

public class ModelUtils {

    public static void fluidContainer(ItemBuilder builder, AssetJsonGenerator generator) {
        if (builder.modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(builder.id), builder.modelJson);
        } else {
            generator.itemModel(builder.id, m -> {
                if (!builder.parentModel.isEmpty()) {
                    m.parent(builder.parentModel);
                } else {
                    m.parent("kubejs_tfc:item/generated_fluid_container");
                }

                if (builder.textureJson.size() == 0) {
                    final String tex = newItemID(builder.id).toString();
                    builder.texture("base", tex);
                    builder.texture("fluid", tex + "_overlay");
                }

                m.textures(builder.textureJson);
            });
        }
    }

    // TODO: 1.2.3 | Dehardcode texture references, merge into builder classes
    public static class ITEMS {

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
    }

    private static ResourceLocation newID(ResourceLocation id, String pre, String post) {
        return new ResourceLocation(id.getNamespace(), pre + id.getPath() + post);
    }

    private static ResourceLocation newItemID(ResourceLocation id) {
        return newID(id, "item/", "");
    }

    public static final String[] cardinalDirections = {"north", "east", "south", "west"};
}
