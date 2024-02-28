package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.internal.SpreadingCaneBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpreadingBushBlockBuilder extends StationaryBerryBushBlockBuilder {

    public transient final SpreadingCaneBlockBuilder child;
    public transient int maxHeight;
    public transient final Supplier<ClimateRange> climateRange;

    public SpreadingBushBlockBuilder(ResourceLocation i) {
        super(i);
        child = new SpreadingCaneBlockBuilder(newID("", "_cane"), this);
        maxHeight = 3;
        climateRange = ClimateRange.MANAGER.register(id);
        texture("layer0", newID("item/", "").toString());
    }

    public SpreadingBushBlockBuilder maxHeight(int i) {
        maxHeight = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new SpreadingBushBlock(createExtendedProperties(), productItem, lifecycles, child, maxHeight, climateRange);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(child);
        RegistryUtils.addBerryBush(child);
    }

    @Override
    @Generics(value = BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(DataUtils.STICK_STACK);
            });
            if (itemBuilder != null) {
                lootBuilder.addPool(p -> {
                    p.survivesExplosion();
                    p.addEntry(lootEntry());
                });
            }
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    private JsonObject lootEntry() {
        final  JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:alternatives");
        final JsonArray children = new JsonArray(2);

        // Stage 2
        {
            final JsonObject entry = lootEntryBase();
            final JsonArray conditions = new JsonArray(2);
            conditions.add(DataUtils.sharpToolsCondition());

            final JsonObject stateCondition = new JsonObject();
            stateCondition.addProperty("condition", "minecraft:block_state_property");
            stateCondition.addProperty("block", id.toString());
            final JsonObject properties = new JsonObject();
            properties.addProperty("stage", "2");
            stateCondition.add("properties", properties);
            conditions.add(stateCondition);

            children.add(entry);
        }
        // 50% chance
        {
            final JsonObject entry = lootEntryBase();
            final JsonArray conditions = new JsonArray(2);
            conditions.add(DataUtils.sharpToolsCondition());

            final JsonObject chanceCondition = new JsonObject();
            chanceCondition.addProperty("condition", "minecraft:random_chance");
            chanceCondition.addProperty("chance", 0.5);
            conditions.add(chanceCondition);

            entry.add("conditions", conditions);
            children.add(entry);
        }

        json.add("children", children);
        return json;
    }

    private JsonObject lootEntryBase() {
        final JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:item");
        json.addProperty("name", itemBuilder.id.toString());
        return json;
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
            m.textures(textures);
        }
    }
}
