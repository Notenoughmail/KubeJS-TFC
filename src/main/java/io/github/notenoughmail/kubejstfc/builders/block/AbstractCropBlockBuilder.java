package io.github.notenoughmail.kubejstfc.builders.block;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.block.SeedItemBuilder;
import dev.latvian.mods.kubejs.block.drop.BlockDrops;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.blocks.sub.DeadCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class AbstractCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient int ages;
    public transient final Supplier<ClimateRange> climateRange;
    public final transient DeadCropBlockBuilder dead;
    public transient final SeedItemBuilder seeds;
    public transient float p, n, k;
    public transient final Type type;
    public transient boolean requiresStick;
    public transient BiConsumer<Integer, ModelGenerator> models;
    public transient Supplier<Float> growthMod = () -> 1F, expiryMod = () -> 1F;

    public AbstractCropBlockBuilder(ResourceLocation i, Type type) {
        super(i);
        this.type = type;
        ages = 8;
        climateRange = ClimateRange.MANAGER.getReference(id);
        dead = new DeadCropBlockBuilder(id.withSuffix("_dead"), this);
        seeds = new SeedItemBuilder(id.withSuffix("_seeds"));
        seeds.blockBuilder = this;
        requiresStick = false;
        renderType(BlockRenderType.CUTOUT);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.CROP, this);
        itemBuilder = null;
        noCollision();
        drops = () -> BlockDrops.createDefault(seeds.get().getDefaultInstance());
        models = (stage, m) -> {
            m.parent(ModelUtil.CROP);
            m.textures(textures);
        };
        p = n = k = 0F;
    }

    @HideFromJS
    public IntegerProperty getAges() {
        return TFCBlockStateProperties.getAgeProperty(ages);
    }

    @Deprecated
    protected void fill(Consumer<ModelGenerator>[] fill) {
        Arrays.fill(fill, (Consumer<ModelGenerator>) (ModelGenerator m) -> {
            m.parent(ModelUtil.CROP);
            m.textures(textures);
        });
    }

    // TODO: 2.0.0 | Is there a better way?
    @HideFromJS
    public DeadCropBlockBuilder.DeadModelVariant[] deadModels() {
        return DeadModels.VALUES;
    }

    @Info("Sets how many growth stages the crop will have")
    public AbstractCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 8) {
            ages = i;
        }
        return this;
    }

    @Info("Sets the model generation of the crop, accepts a `BiConsumer` of a number, representing the age of the crop, and a model generator. The generator is unique for each age")
    public AbstractCropBlockBuilder models(BiConsumer<Integer, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Info("Modifies the crop's dead block")
    public AbstractCropBlockBuilder deadBlock(Consumer<DeadCropBlockBuilder> deadCrop) {
        deadCrop.accept(dead);
        return this;
    }

    @Info("Modifies the crop's seed item")
    public AbstractCropBlockBuilder seedItem(Consumer<SeedItemBuilder> seedItem) {
        seedItem.accept(seeds);
        return this;
    }

    @Info("Sets the potassium amount the crop requires to grow")
    public AbstractCropBlockBuilder requiredPotassium(float k) {
        this.k = k;
        return this;
    }

    @Info("Sets the nitrogen amount the crop requires to grow")
    public AbstractCropBlockBuilder requiredNitrogen(float n) {
        this.n = n;
        return this;
    }

    @Info("Sets the phosphorous amount the crop requires to grow")
    public AbstractCropBlockBuilder requiredPhosphorous(float p) {
        this.p = p;
        return this;
    }

    @Info("Sets the growth modifier of the crop, a higher value means it takes longer to grow")
    public AbstractCropBlockBuilder growthModifier(float mod) {
        growthMod = () -> mod;
        return this;
    }

    @Info("Sets the growth modifier supplier of the crop, a higher value means it takes longer to grow")
    public AbstractCropBlockBuilder growthModifierSupplier(Supplier<Float> mod) {
        growthMod = mod;
        return this;
    }

    @Info("sets the expiry modifier of the crop, a higher value means it takes longer for the crop to die")
    public AbstractCropBlockBuilder expiryModifier(float mod) {
        expiryMod = () -> mod;
        return this;
    }

    @Info("Sets the expiry modifier supplier of the crop, a higher value means it takes longer for the crop to die")
    public AbstractCropBlockBuilder expiryModifierSupplier(Supplier<Float> mod) {
        expiryMod = mod;
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.CROP)
                .serverTicks(CropBlockEntity::serverTick);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, dead);
        Assistant.addItem(registry, seeds);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.basic(seeds.get());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        getAges().getPossibleValues().forEach(age -> generator.blockModel(id.withSuffix("_age_" + age), m -> models.accept(age, m)));
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        getAges().getPossibleValues().forEach(age -> bs.simpleVariant("age=" + age, newID("block/", "_age_" + age)));
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> g.parent(newID("block/", "_age_" + (ages - 1))));
    }

    public enum Type {
        DOUBLE,
        DEFAULT,
        FLOODED,
        SPREADING,
        CLIMBING,
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
        public String str() {
            return mature() ? "mature" : "juvenile";
        }
    }

    public abstract static class WithProduct extends AbstractCropBlockBuilder {

        public transient Supplier<Item> existingProduct;
        public transient ItemBuilder product;

        public WithProduct(ResourceLocation i, Type type) {
            super(i, type);
            product = new ItemBuilder(id.withSuffix("_product"));
        }

        @Nullable
        protected Item getProduct() {
            if (existingProduct != null) {
                return existingProduct.get();
            } else if (product != null) {
                return product.get();
            } return null;
        }

        @Info("Modifies the properties of the default product item")
        public WithProduct productItem(@Nullable Consumer<ItemBuilder> item) {
            if (item != null) {
                item.accept(product);
            } else {
                product = null;
            }
            return this;
        }

        @Info("Sets the crop to have an existing item for its product")
        public WithProduct existingProductItem(Holder<Item> item) {
            existingProduct = Assistant.holderAsSupplier(item);
            product = null;
            return this;
        }

        @Override
        public void createAdditionalObjects(AdditionalObjectRegistry registry) {
            super.createAdditionalObjects(registry);
            Assistant.addItem(registry, product);
        }

        @Override
        @Nullable
        public LootTable generateLootTable(KubeDataGenerator generator) {
            return LootUtil.fullTable(null, t -> {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(LootItem.lootTableItem(seeds.get()));
                });
                final Item prod = getProduct();
                if (prod != null) {
                    LootUtil.pool(t, p -> {
                        LootUtil.survivesExplosion(p);
                        p.add(LootItem.lootTableItem(prod)
                                .when(LootUtil.withState(get(), s -> s.hasProperty(getAges(), ages)))
                                .apply(LootUtil.count(new CropYieldProvider(
                                        ConstantValue.exactly(0F),
                                        UniformGenerator.between(6F, 10F)
                                ))));
                    });
                }
            });
        }
    }

    public static WithProduct normal(ResourceLocation id) {
        return new WithProduct(id, Type.DEFAULT) {
            @Override
            public Block createObject() {
                return CropUtil.defaultCrop(this);
            }
        };
    }

    public static WithProduct flooded(ResourceLocation id) {
        return new WithProduct(id, Type.FLOODED) {
            @Override
            public Block createObject() {
                return CropUtil.floodedCrop(this);
            }
        };
    }
}
