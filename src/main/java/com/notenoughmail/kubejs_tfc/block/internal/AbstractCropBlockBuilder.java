package com.notenoughmail.kubejs_tfc.block.internal;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.sub.DeadCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.SeedItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
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
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class AbstractCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int stages;
    public transient final Supplier<ClimateRange> climateRange;
    public final transient DeadCropBlockBuilder dead;
    public transient final SeedItemBuilder seeds;
    @Nullable
    public transient final ItemBuilder product;
    public transient FarmlandBlockEntity.NutrientType nutrient;
    public transient Type type;
    public transient boolean requiresStick;
    @Nullable
    public transient ResourceLocation productItem;
    public transient final Consumer<ModelGenerator>[] models = new Consumer[12];

    public AbstractCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 8;
        climateRange = ClimateRange.MANAGER.register(id);
        dead = new DeadCropBlockBuilder(newID("", "_dead"), this);
        seeds = new SeedItemBuilder(newID("", "_seeds"));
        seeds.blockBuilder = this;
        if (hasProduct()) {
            product = new BasicItemJS.Builder(newID("", "_product"));
        } else {
            product = null;
        }
        nutrient = FarmlandBlockEntity.NutrientType.NITROGEN;
        requiresStick = false;
        renderType("cutout");
        productItem = null;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.CROP, this);
        noItem();
    }

    protected boolean hasProduct() {
        return true;
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

    @Info(value = "Sets the crop's 'product' item to be an existing item")
    public AbstractCropBlockBuilder productItem(ResourceLocation productItem) {
        if (hasProduct()) {
            this.productItem = productItem;
        }
        return this;
    }

    @Override
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        itemBuilder = null;
        return this;
    }

    @Info(value = "Sets the nutrient the crop uses as fertilizer, defaults to nitrogen")
    public AbstractCropBlockBuilder nutrient(FarmlandBlockEntity.NutrientType nutrient) {
        this.nutrient = nutrient;
        return this;
    }

    @Info(value = "Texture the block for all growth stages")
    public AbstractCropBlockBuilder texture(String texture) {
        for (int i = 0 ; i < 12 ; i++) {
            texture(i, texture);
        }
        return this;
    }

    @Info(value = "Texture a specific key for all growth stages")
    public AbstractCropBlockBuilder textureAll(String id, String tex) {
        for (int i = 0 ; i < 12 ; i++) {
            texture(i, id, tex);
        }
        return this;
    }

    @Info(value = "Sets the model for all growth stages")
    @Override
    public AbstractCropBlockBuilder model(String m) {
        for (int i = 0 ; i < 12 ; i++) {
            model(i, m);
        }
        return this;
    }

    @Info(value = "Sets the model for all growth stages")
    public AbstractCropBlockBuilder model(Consumer<ModelGenerator> gen) {
        for (int i = 0 ; i < 12 ; i++) {
            model(i, gen);
        }
        return this;
    }

    @Info(value = "Sets the model for a specific growth stage")
    public AbstractCropBlockBuilder model(int stage, Consumer<ModelGenerator> gen) {
        models[stage] = gen;
        return this;
    }

    @Info(value = "Sets the model for a specific growth stage")
    public AbstractCropBlockBuilder model(int stage, String model) {
        models[stage] = m -> m.parent(model);
        return this;
    }

    @Info(value = "Textures a specific key for the given stage")
    public AbstractCropBlockBuilder texture(int stage, String id, String texture) {
        if (models[stage] == null) {
            models[stage] = m -> {
                m.parent("block/crop");
                m.texture(id, texture);
            };
        } else {
            models[stage] = models[stage].andThen(m -> m.texture(id, texture));
        }
        return this;
    }

    @Info(value = "Textures the block for the given growth stage")
    public AbstractCropBlockBuilder texture(int stage, String texture) {
        return texture(stage, "crop", texture);
    }

    @Info(value = "Sets the textures for all growth stages")
    public AbstractCropBlockBuilder textures(JsonObject textures) {
        for (int i = 0; i < 12 ; i++) {
            textures(i, textures);
        }
        return this;
    }

    @Info(value = "Sets the textures for the given growth stage")
    public AbstractCropBlockBuilder textures(int stage, JsonObject textures) {
        if (models[stage] != null) {
            models[stage] = models[stage].andThen(m -> m.textures(textures));
        } else {
            models[stage] = m -> {
                m.parent("block/crop");
                m.textures(textures);
            };
        }
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return texture("crop", tex);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.CROP)
                .serverTicks(CropBlockEntity::serverTick);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(dead);
        dead.createAdditionalObjects();
        RegistryInfo.ITEM.addBuilder(seeds);
        if (hasProduct() && productItem == null) {
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
                    p.addItem(new ItemStack(productItem != null ? RegistryInfo.ITEM.getValue(productItem) : product.get()))
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
        for (int i = 0 ; i <= stages ; i++) {
            final ResourceLocation age = newID("", "_age_" + i);
            if (models[i] != null) {
                int finalI = i;
                generator.blockModel(age, m -> {
                    m.textures(textures);
                    models[finalI].accept(m);
                });
            } else {
                final String texture = newID("block/", "_" + i).toString();
                generator.blockModel(age, m -> {
                    m.parent("block/crop");
                    m.texture("crop", texture);
                });
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (int i = 0 ; i <= stages ; i++) {
            bs.simpleVariant("age=" + i, newID("block/", "_age_" + i).toString());
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (model.isEmpty()) {
            m.parent(id.getNamespace() + ":block/" + id.getPath() + "_age_" + (stages - 1));
        } else {
            m.parent(model);
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
