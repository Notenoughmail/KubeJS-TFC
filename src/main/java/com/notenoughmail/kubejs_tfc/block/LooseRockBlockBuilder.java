package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.minecraft.resources.ResourceLocation;

public class LooseRockBlockBuilder extends BlockBuilder {

    private int rotate;
    public JsonObject itemTextures;

    public LooseRockBlockBuilder(ResourceLocation i) {
        super(i);
        rotate = 0;
        noCollision = true;
        itemTextures = new JsonObject();
    }

    public LooseRockBlockBuilder rotateModel(int i) {
        rotate = i;
        return this;
    }

    @Info(value = "Makes the block collide blockStates entities")
    public LooseRockBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    @Info("Sets the item's texture (layer0).")
    public LooseRockBlockBuilder itemTexture(String tex) {
        itemTextures.addProperty("layer0", tex);
        return this;
    }

    @Info("Sets the item's texture by given key.")
    public LooseRockBlockBuilder itemTexture(String key, String tex) {
        itemTextures.addProperty(key, tex);
        return this;
    }

    @Info("Directlys set the item's texture json.")
    public LooseRockBlockBuilder itemTextureJson(JsonObject json) {
        itemTextures = json;
        return this;
    }

    @Override
    public LooseRockBlock createObject() {
        return new LooseRockBlock(createProperties());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String texture = newID("block/", "").toString();
        generator.blockModel(newID("", "_pebble"), m -> {
            m.parent("kubejs_tfc:block/groundc_over/pebble");
            m.texture("all", texture);
        });
        generator.blockModel(newID("", "_rubble"), m -> {
            m.parent("kubejs_tfc:block/ground_cover/rubble");
            m.texture("all", texture);
        });
        generator.blockModel(newID("", "_boulder"), m -> {
            m.parent("kubejs_tfc:block/ground_cover/boulder");
            m.texture("all", texture);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String blockModelLocation = newID("block/", "").toString();
        bs.variant("count=1", v -> {
            v.model(blockModelLocation + "_pebble").y(rotate);
            v.model(blockModelLocation + "_pebble").y(90 + rotate);
            v.model(blockModelLocation + "_pebble").y(180 + rotate);
            v.model(blockModelLocation + "_pebble").y(270 + rotate);
        });
        bs.variant("count=2", v -> {
            v.model(blockModelLocation + "_rubble").y(rotate);
            v.model(blockModelLocation + "_rubble").y(90 + rotate);
            v.model(blockModelLocation + "_rubble").y(180 + rotate);
            v.model(blockModelLocation + "_rubble").y(270 + rotate);
        });
        bs.variant("count=3", v -> {
            v.model(blockModelLocation + "_boulder").y(rotate);
            v.model(blockModelLocation + "_boulder").y(90 + rotate);
            v.model(blockModelLocation + "_boulder").y(180 + rotate);
            v.model(blockModelLocation + "_boulder").y(270 + rotate);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
        }

        if (itemTextures.size() == 0) {
            itemTexture(newID("item/", "").toString());
        }
        m.textures(itemTextures);
    }

    // TODO: Make this work in general?
    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        // Did this ever work...
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
