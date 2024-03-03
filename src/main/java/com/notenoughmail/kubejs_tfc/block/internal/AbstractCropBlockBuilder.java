package com.notenoughmail.kubejs_tfc.block.internal;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.SeedItemBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractCropBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public transient int stages;
    public transient final Supplier<ClimateRange> climateRange;
    public transient DeadCropBlockBuilder dead;
    public transient final SeedItemBuilder seeds;
    @Nullable
    public transient final ItemBuilder product;
    public transient FarmlandBlockEntity.NutrientType nutrient;
    public transient Consumer<ExtendedPropertiesJS> props;
    public transient Type type;
    public transient boolean requiresStick;

    public AbstractCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 8;
        climateRange = ClimateRange.MANAGER.register(id);
        dead = new DeadCropBlockBuilder(newID("", "_dead"), this);
        seeds = new SeedItemBuilder(newID("", "_seeds"));
        if (hasProduct()) {
            product = new BasicItemJS.Builder(newID("", "_product"));
        } else {
            product = null;
        }
        nutrient = FarmlandBlockEntity.NutrientType.NITROGEN;
        props = p -> {};
        requiresStick = false;
        renderType("cutout");
    }

    protected boolean hasProduct() {
        return true;
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

    @Info(value = "Determines how many growth stages the crop will have")
    public AbstractCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 12) {
            stages = i;
        }
        return this;
    }

    @Info(value = "Modifies the crop's dead block")
    @Generics(value = DeadCropBlockBuilder.class)
    public AbstractCropBlockBuilder deadBlock(Consumer<DeadCropBlockBuilder> deadCrop) {
        deadCrop.accept(dead);
        return this;
    }

    @Info(value = "Modifies the crop's seed item")
    @Generics(value = ItemBuilder.class)
    public AbstractCropBlockBuilder seedItem(Consumer<SeedItemBuilder> seedItem) {
        seedItem.accept(seeds);
        return this;
    }

    @Info(value = "Modifies the crop's 'product' item")
    @Generics(value = ItemBuilder.class)
    public AbstractCropBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        if (hasProduct()) {
            productItem.accept(product);
        }
        return this;
    }

    @Info(value = "Sets the nutrient the crop uses as fertilizer, defaults to nitrogen")
    public AbstractCropBlockBuilder nutrient(FarmlandBlockEntity.NutrientType nutrient) {
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
        if (hasProduct()) {
            assert product != null;
            RegistryInfo.ITEM.addBuilder(product);
            product.createAdditionalObjects();
        }
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
                p.addItem(new ItemStack(seeds.get()));
            });

            if (hasProduct()) {
                assert product != null;
                lootBuilder.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(new ItemStack(product.get()))
                            .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("age", String.valueOf(stages - 1))))
                            .addFunction(cropYieldUniformFunction());
                });
            }
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    protected JsonObject cropYieldUniformFunction() {
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

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int i = 0 ; i < stages ; i++) {
            final int j = i;
            generator.blockModel(newID("", "_age_" + j), m -> {
                m.parent("block/crop");
                m.texture("crop", newID("block/", "_" + j).toString());
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (int i = 0 ; i < stages ; i++) {
            bs.simpleVariant("age=" + i, newID("block/", "_age_" + i).toString());
        }
    }

    public enum Type {
        DOUBLE,
        DEFAULT,
        FLOODED,
        SPREADING,
        PICKABLE
    }
}
