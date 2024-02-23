package com.notenoughmail.kubejs_tfc.block.internal;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: JSDoc
public abstract class AbstractCropBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public transient int stages;
    public transient final Supplier<ClimateRange> climateRange;
    public transient DeadCropBlockBuilder dead;
    public transient final ItemBuilder seeds;
    public transient final ItemBuilder product;
    public transient FarmlandBlockEntity.NutrientType nutrient;
    public transient Consumer<ExtendedPropertiesJS> props;
    public transient Type type;

    public AbstractCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 8;
        climateRange = ClimateRange.MANAGER.register(id);
        dead = new DeadCropBlockBuilder(newID("", "_dead"), this);
        seeds = new BasicItemJS.Builder(newID("", "_seeds"));
        product = new BasicItemJS.Builder(newID("", "_product"));
        nutrient = FarmlandBlockEntity.NutrientType.NITROGEN;
        props = p -> {};
    }

    public AbstractCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 12) {
            stages = i;
        }
        return this;
    }

    @Generics(value = DeadCropBlockBuilder.class)
    public AbstractCropBlockBuilder deadBlock(Consumer<DeadCropBlockBuilder> deadCrop) {
        deadCrop.accept(dead);
        return this;
    }

    @Generics(value = ItemBuilder.class)
    public AbstractCropBlockBuilder seedItem(Consumer<ItemBuilder> seedItem) {
        seedItem.accept(seeds);
        return this;
    }

    @Generics(value = ItemBuilder.class)
    public AbstractCropBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        productItem.accept(product);
        return this;
    }

    public AbstractCropBlockBuilder primaryNutrient(FarmlandBlockEntity.NutrientType nutrient) {
        this.nutrient = nutrient;
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = extendedPropsJS();
        props.accept(propsJs);
        return propsJs.delegate()
                .blockEntity(RegistryUtils.getCrop())
                .serverTicks(CropBlockEntity::serverTick);
    }

    @Override
    public AbstractCropBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryUtils.addCrop(this);
        RegistryInfo.BLOCK.addBuilder(dead);
        dead.createAdditionalObjects();
        RegistryInfo.ITEM.addBuilder(seeds);
    }

    // TODO: Fix for double, spreading crops
    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        lootBuilder.addPool(p -> {
            p.survivesExplosion();
            p.addItem(new ItemStack(seeds.get()));
        });

        lootBuilder.addPool(p -> {
            p.survivesExplosion();
            p.addItem(new ItemStack(product.get()))
                    .addCondition(condition())
                    .addFunction(function());
        });

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    private JsonObject condition() {
        final JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:block_state_property");
        json.addProperty("block", id.toString());
        final JsonObject properties = new JsonObject();
        properties.addProperty("age", String.valueOf(stages - 1));
        json.add("properties", properties);
        return json;
    }

    private JsonObject function() {
        final JsonObject json = new JsonObject();
        json.addProperty("function", "minecraft:set_count");
        final JsonObject count = new JsonObject();
        count.addProperty("type", "tfc:crop_yield_uniform");
        count.addProperty("min", 0);
        final JsonObject max = new JsonObject();
        max.addProperty("type", "minecraft:uniform");
        max.addProperty("min", 6);
        max.addProperty("max", 10);
        count.add("max", max);
        json.add("count", count);
        return json;
    }

    // TODO: Fix for double, spreading crops
    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int i = 0 ; i < stages ; i++) {
            final int lambdaI = i;
            generator.blockModel(newID("", "_age_" + i), m -> {
                m.parent("block/crop");
                m.texture("crop", newID("block/", "_" + lambdaI).toString());
            });
        }
    }

    // TODO: Fix for double crops
    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (int i = 0 ; i < stages ; i++) {
            bs.simpleVariant("age=" + i, newID("block/", "_age_" + i).toString());
        }
    }

    public enum Type {
        CLIMBING,
        DEFAULT,
        FLOODED,
        SPREADING,
        PICKABLE
    }
}
