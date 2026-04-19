package com.notenoughmail.kubejs_tfc.block.internal;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.sub.DeadCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.SeedItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class AbstractCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int stages;
    public transient final Supplier<ClimateRange> climateRange;
    public final transient DelayedBuilder<DeadCropBlockBuilder> dead;
    public transient final DelayedBuilder<SeedItemBuilder> seeds;
    public transient final DelayedBuilder.NullCapable<ItemBuilder> product;
    public transient FarmlandBlockEntity.NutrientType nutrient;
    public transient final Type type;
    public transient boolean requiresStick;
    @Nullable
    public transient ResourceLocation productItem;
    public transient final Consumer<ModelGenerator>[] models = new Consumer[12];
    public transient Supplier<Double> growthMod = () -> 1D, expiryMod = () -> 1D;

    public AbstractCropBlockBuilder(ResourceLocation i, Type type) {
        super(i);
        this.type = type;
        stages = 8;
        climateRange = ClimateRange.MANAGER.register(id);
        dead = new DelayedBuilder<>(r -> new DeadCropBlockBuilder(r, this), () -> newID("", "_dead"));
        seeds = new DelayedBuilder<>(
                r -> Util.make(
                        new SeedItemBuilder(r),
                        s -> s.blockBuilder = this
                ),
                () -> newID("", "_seeds")
        );
        product = new DelayedBuilder.NullCapable<>(BasicItemJS.Builder::new, () -> newID("", "_product"));
        if (!hasProduct()) product.markNull();
        nutrient = FarmlandBlockEntity.NutrientType.NITROGEN;
        requiresStick = false;
        renderType("cutout");
        productItem = null;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.CROP, this);
        itemBuilder = null;
        noCollision();
        fill(models);
    }

    protected Item getProductItem() {
        if (!hasProduct()) throw new IllegalStateException("There cannot be a product item if a builder has no products");
        return productItem == null ? Objects.requireNonNull(product.get()).get() : RegistryInfo.ITEM.getValue(productItem);
    }

    protected void fill(Consumer<ModelGenerator>[] fill) {
        Arrays.fill(fill, (Consumer<ModelGenerator>) (ModelGenerator m) -> {
            m.parent("block/crop");
            m.textures(textures);
        });
    }

    @HideFromJS
    public <T extends Enum<T> & DeadCropBlockBuilder.DeadModelVariant> T[] deadModels() {
        return (T[]) DeadModels.VALUES;
    }

    protected boolean hasProduct() {
        return true;
    }

    @Info("Sets how many growth stages the crop will have")
    public AbstractCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 8) {
            stages = i;
        }
        return this;
    }

    @Info("Modifies the crop's dead block")
    @Generics(DeadCropBlockBuilder.class)
    public AbstractCropBlockBuilder deadBlock(Consumer<DeadCropBlockBuilder> deadCrop) {
        return deadBlock(dead.fallbackId(), deadCrop);
    }

    @Info("Modifies the crop's dead block")
    @Generics(DeadCropBlockBuilder.class)
    public AbstractCropBlockBuilder deadBlock(ResourceLocation id, Consumer<DeadCropBlockBuilder> deadCrop) {
        deadCrop.accept(dead.get(id));
        return this;
    }

    @Info("Modifies the crop's seed item")
    @Generics(ItemBuilder.class)
    public AbstractCropBlockBuilder seedItem(Consumer<SeedItemBuilder> seedItem) {
        return seedItem(seeds.fallbackId(), seedItem);
    }

    @Info("Modifies the crop's seed item")
    @Generics(ItemBuilder.class)
    public AbstractCropBlockBuilder seedItem(ResourceLocation id, Consumer<SeedItemBuilder> seedItem) {
        seedItem.accept(seeds.get(id));
        return this;
    }

    @Info("Modifies the crop's 'product' item")
    @Generics(ItemBuilder.class)
    public AbstractCropBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        return productItem(product.fallbackId(), productItem);
    }

    @Info("Modifies the crop's 'product' item")
    @Generics(ItemBuilder.class)
    public AbstractCropBlockBuilder productItem(ResourceLocation id, Consumer<ItemBuilder> productItem) {
        if (hasProduct()) {
            productItem.accept(product.get(id));
        }
        return this;
    }

    @Info("Sets the crop's 'product' item to be an existing item")
    public AbstractCropBlockBuilder existingProductItem(ResourceLocation productItem) {
        if (hasProduct()) {
            this.productItem = productItem;
            product.markNull();
        }
        return this;
    }

    @Info("Sets the nutrient the crop uses as fertilizer, defaults to nitrogen")
    public AbstractCropBlockBuilder nutrient(FarmlandBlockEntity.NutrientType nutrient) {
        this.nutrient = nutrient;
        return this;
    }

    @Info("Texture the block for all growth stages")
    public AbstractCropBlockBuilder texture(String texture) {
        for (int i = 0 ; i < 12 ; i++) {
            textureAt(i, texture);
        }
        return this;
    }

    @Info("Texture a specific key for all growth stages")
    public AbstractCropBlockBuilder textureAll(String id, String tex) {
        for (int i = 0 ; i < 12 ; i++) {
            textureAt(i, id, tex);
        }
        return this;
    }

    @Info("Sets the model for all growth stages")
    @Override
    public AbstractCropBlockBuilder model(String m) {
        for (int i = 0 ; i < 12 ; i++) {
            model(i, m);
        }
        model = m;
        return this;
    }

    @Info("Sets the model for all growth stages")
    public AbstractCropBlockBuilder setModel(Consumer<ModelGenerator> gen) {
        Arrays.fill(models, gen);
        return this;
    }

    @Info("Sets the model for a specific growth stage")
    public AbstractCropBlockBuilder setModel(int stage, Consumer<ModelGenerator> gen) {
        models[stage] = gen;
        return this;
    }

    @Info("Sets the model for a specific growth stage")
    public AbstractCropBlockBuilder model(int stage, String model) {
        models[stage] = m -> m.parent(model);
        return this;
    }

    @Info("Textures a specific key for the given stage")
    public AbstractCropBlockBuilder textureAt(int stage, String id, String texture) {
        models[stage] = models[stage].andThen(m -> m.texture(id, texture));
        return this;
    }

    @Info("Textures the block for the given growth stage")
    public AbstractCropBlockBuilder textureAt(int stage, String texture) {
        return textureAt(stage, "crop", texture);
    }

    @Info("Sets the textures for all growth stages")
    public AbstractCropBlockBuilder textures(JsonObject textures) {
        for (int i = 0; i < 12 ; i++) {
            textures(i, textures);
        }
        return this;
    }

    @Info("Sets the textures for the given growth stage")
    public AbstractCropBlockBuilder textures(int stage, JsonObject textures) {
        models[stage] = models[stage].andThen(m -> m.textures(textures));
        return this;
    }

    @Info("Sets the growth modifier of the crop, a higher value means it takes longer to grow")
    public AbstractCropBlockBuilder growthModifier(double mod) {
        growthMod = () -> mod;
        return this;
    }

    @Info("Sets the growth modifier supplier of the crop, a higher value means it takes longer to grow")
    public AbstractCropBlockBuilder growthModifierSupplier(Supplier<Double> mod) {
        growthMod = mod;
        return this;
    }

    @Info("sets the expiry modifier of the crop, a higher value means it takes longer for the crop to die")
    public AbstractCropBlockBuilder expiryModifier(double mod) {
        expiryMod = () -> mod;
        return this;
    }

    @Info("Sets the expiry modifier supplier of the crop, a higher value means it takes longer for the crop to die")
    public AbstractCropBlockBuilder expiryModifierSupplier(Supplier<Double> mod) {
        expiryMod = mod;
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
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
        RegistryInfo.BLOCK.addBuilder(dead.get());
        dead.get().createAdditionalObjects();
        RegistryInfo.ITEM.addBuilder(seeds.get());
        product.ifNotMarkedNull(i -> {
            RegistryInfo.ITEM.addBuilder(i);
            i.createAdditionalObjects();
        });
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(b -> {
            b.addPool(p -> {
                p.survivesExplosion();
                p.addItem(seeds.get().get().getDefaultInstance());
            });
            if (hasProduct()) {
                b.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(getProductItem().getDefaultInstance())
                            .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("age", String.valueOf(stages))))
                            .count(new CropYieldProvider(
                                    ConstantValue.exactly(0.0F),
                                    UniformGenerator.between(6F, 10F)
                            ));
                });
            }
        }, generator, this);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int i = 0 ; i <= stages ; i++) {
            generator.blockModel(newID("", "_age_" + i), models[i]);
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

    public enum DeadModels implements DeadCropBlockBuilder.DeadModelVariant {
        MATURE,
        YOUNG;

        public static final DeadModels[] VALUES = values();

        @Override
        public String variant() {
            return "mature=" + mature();
        }

        @Override
        public boolean mature() {
            return this == MATURE;
        }

        @Override
        public ResourceLocation model(DeadCropBlockBuilder dead) {
            return dead.newID("", mature() ? "": "_young");
        }
    }
}
