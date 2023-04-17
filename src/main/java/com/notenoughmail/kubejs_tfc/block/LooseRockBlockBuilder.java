package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.custom.ShapedBlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.minecraft.resources.ResourceLocation;

public class LooseRockBlockBuilder extends ShapedBlockBuilder {

    private transient int rotate;

    public LooseRockBlockBuilder(ResourceLocation i) {
        super(i);
        rotate = 0;
        noCollision = true;
    }

    public LooseRockBlockBuilder notAxisAligned() {
        rotate = 45;
        return this;
    }

    public LooseRockBlockBuilder collsion() {
        noCollision = false;
        return this;
    }

    @Override
    public LooseRockBlock createObject() {
        return new LooseRockBlock(createProperties());
    }

    // I hate dealing with this so much, why must it fight me
    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson != null) {
            generator.json(newID("blockstates/", ""), blockstateJson);
        } else {
            var blockModelLoc = newID("block/", "").toString();
            generator.blockState(id, m -> {
                m.variant("count=1", v -> {
                    v.model(blockModelLoc + "_pebble").y(rotate);
                    v.model(blockModelLoc + "_pebble").y(90 + rotate);
                    v.model(blockModelLoc + "_pebble").y(180 + rotate);
                    v.model(blockModelLoc + "_pebble").y(270 + rotate);
                });
                m.variant("count=2", v -> {
                    v.model(blockModelLoc + "_rubble").y(rotate);
                    v.model(blockModelLoc + "_rubble").y(90 + rotate);
                    v.model(blockModelLoc + "_rubble").y(180 + rotate);
                    v.model(blockModelLoc + "_rubble").y(270 + rotate);
                });
                m.variant("count=3", v -> {
                    v.model(blockModelLoc + "_boulder").y(rotate);
                    v.model(blockModelLoc + "_boulder").y(90 + rotate);
                    v.model(blockModelLoc + "_boulder").y(180 + rotate);
                    v.model(blockModelLoc + "_boulder").y(270 + rotate);
                });
            });
        }

        generator.blockModel(newID("", "_pebble"), m -> {
            m.parent("kubejs_tfc:block/groundcover/pebble");
            m.texture("all", textures.get("texture").getAsString());
        });
        generator.blockModel(newID("", "_rubble"), m -> {
            m.parent("kubejs_tfc:block/groundcover/rubble");
            m.texture("all", textures.get("texture").getAsString());
        });
        generator.blockModel(newID("", "_boulder"), m -> {
            m.parent("kubejs_tfc:block/groundcover/boulder");
            m.texture("all", textures.get("texture").getAsString());
        });

        if (itemBuilder != null) {
            generator.itemModel(itemBuilder.id, m -> {
                if (!model.isEmpty()) {
                    m.parent(model);
                } else {
                    m.parent("item/generated");
                }

                if (itemBuilder.textureJson.size() == 0) {
                    itemBuilder.texture(newID("item/", "").toString());
                }
                m.textures(itemBuilder.textureJson);
            });
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        if (lootTable != null && itemBuilder != null) {
            var lootbuilder = new LootBuilder(null);
            lootbuilder.type = "minecraft:block";

            // Come Hell and see thine madness
            var lootPools = new JsonArray();
             var poolObj = new JsonObject();
             poolObj.addProperty("name", "loot_pool");
             poolObj.addProperty("rolls", 1);
              var entries = new JsonArray();
               var entry = new JsonObject();
                entry.addProperty("type", "minecraft:item");
                entry.addProperty("name", itemBuilder.id.toString());
                var functions = new JsonArray();
                 var func_2 = new JsonObject();
                 func_2.addProperty("function", "minecraft:set_count");
                 func_2.addProperty("count", 2);
                  var conditions_2 = new JsonArray();
                   var condition_2 = new JsonObject();
                   condition_2.addProperty("condition", "minecraft:block_state_property");
                   condition_2.addProperty("block", id.toString());
                    var properties_2 = new JsonObject();
                    properties_2.addProperty("count", 2);
                   condition_2.add("properties", properties_2);
                  conditions_2.add(condition_2);
                 func_2.add("conditions", conditions_2);
                functions.add(func_2);
                 var func_3 = new JsonObject();
                 func_3.addProperty("function", "minecraft:set_count");
                 func_3.addProperty("count", 3);
                  var conditions_3 = new JsonArray();
                   var condition_3 = new JsonObject();
                   condition_3.addProperty("condition", "minecraft:block_state_property");
                   condition_3.addProperty("block", id.toString());
                    var properties_3 = new JsonObject();
                    properties_3.addProperty("count", 3);
                   condition_3.add("properties", properties_3);
                  conditions_3.add(condition_3);
                 func_3.add("conditions", conditions_3);
                functions.add(func_3);
               entry.add("functions", functions);
              entries.add(entry);
             poolObj.add("entries", entries);
            lootPools.add(poolObj);
            lootbuilder.pools = lootPools;

            generator.json(newID("loot_tables/blocks/", ""), lootbuilder.toJson());
        }
    }
}
