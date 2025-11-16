package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ResourceUtils {

    public static JsonObject buildJson(Consumer<JsonObject> consumer) {
        return Util.make(new JsonObject(), consumer);
    }

    public static void lootTable(Consumer<LootBuilder> c, DataJsonGenerator generator, BlockBuilder builder) {
        // Kube uses EMPTY as an indicator that the block should not have a loot table
        if (builder.lootTable != EMPTY) {
            final LootBuilder b = new LootBuilder(null);
            b.type = "minecraft:block";

            if (builder.lootTable != null) {
                builder.lootTable.accept(b);
            } else {
                c.accept(b);
            }

            generator.json(builder.newID("loot_tables/blocks/", ""), b.toJson());
        }
    }

    public static JsonObject sharpToolsCondition() {
        return buildJson(json -> {
            json.addProperty("condition", "minecraft:match_tool");
            json.add("predicate", buildJson(predicate -> predicate.addProperty("tag", "tfc:sharp_tools")));
        });
    }

    public static JsonObject blockStatePropertyCondition(String block, Consumer<JsonObject> properties) {
        return buildJson(json -> {
            json.addProperty("condition", "minecraft:block_state_property");
            json.addProperty("block", block);
            json.add("properties", buildJson(properties));
        });
    }

    public static LootTableEntry createEntry(String item) {
        return new LootTableEntry(buildJson(json -> {
            json.addProperty("type", "minecraft:item");
            json.addProperty("name", item);
        }));
    }

    public static JsonObject alternatives(LootTableEntry... entries) {
        return buildJson(json -> {
            json.addProperty("type", "minecraft:alternatives");
            final JsonArray arr = new JsonArray(entries.length);
            for (LootTableEntry entry : entries) {
                arr.add(entry.json);
            }
            json.add("children", arr);
        });
    }

    public static final ItemStack STICK_STACK = new ItemStack(Items.STICK);

    public static final Direction[] CARDINAL_DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
}
