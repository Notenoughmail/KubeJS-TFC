package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ReturnsSelf
@SuppressWarnings("unused")
public abstract class WildCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "crop" };
    private static final ResourceLocation WILD_CROP = KubeJSTFC.tfc("block/wild_crop/crop");

    public transient final Function<ExtendedProperties, ? extends WildCropBlock> constructor;
    @Nullable
    public transient Supplier<Item> seedItem, foodItem;

    public WildCropBlockBuilder(ResourceLocation i, Function<ExtendedProperties, ? extends WildCropBlock> constructor) {
        super(i);
        this.constructor = constructor;
        seedItem = null;
        foodItem = null;
        renderType(BlockRenderType.CUTOUT);
        noCollision();
        BuilderRefs.grassColor.add(this);
    }

    protected boolean isTall() {
        return false;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks();
    }

    @Info("Sets the seeds that the crop drops when broken")
    public WildCropBlockBuilder seeds(Holder<Item> seedItem) {
        this.seedItem = Assistant.holderAsSupplier(seedItem);
        return this;
    }

    @Info("Sets the food item that the crop drops when broken")
    public WildCropBlockBuilder food(Holder<Item> foodItem) {
        this.foodItem = Assistant.holderAsSupplier(foodItem);
        return this;
    }

    @Override
    public Block createObject() {
        return constructor.apply(createExtendedProperties());
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.fullTable(drops, t -> {
            if (seedItem != null) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(Assistant.applyIf(
                            LootItem.lootTableItem(seedItem.get()),
                            isTall(),
                            b0 -> b0.when(LootUtil.withState(get(), b -> b.hasProperty(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                    ));
                });
            }
            if (foodItem != null) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(Assistant.applyIf(
                            LootItem.lootTableItem(foodItem.get())
                                    .apply(LootUtil.count(UniformGenerator.between(1F, 3F)))
                                    .when(LootUtil.withState(get(), b -> b.hasProperty(WildCropBlock.MATURE, true))),
                            isTall(),
                            b0 -> b0.when(LootUtil.withState(get(), b -> b.hasProperty(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                    ));
                });
            }
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        bs.simpleVariant("mature=true", ModelUtil.plainModel(this));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(WILD_CROP);
            m.textures(textures);
        });
    }

    public static Normal normal(ResourceLocation id) {
        return new Normal(id, WildCropBlock::new);
    }

    public static Normal flooded(ResourceLocation id) {
        return new Normal(id, FloodedWildCropBlock::new);
    }

    public static class Normal extends WildCropBlockBuilder {

        public transient ResourceLocation deadParentModel;
        public transient Consumer<ModelGenerator> deadModelGenerator;

        public Normal(ResourceLocation id, Function<ExtendedProperties, ? extends WildCropBlock> constructor) {
            super(id, constructor);
        }

        @Info("Sets the parent model for the immature state")
        public Normal juvenileParentModel(ResourceLocation model) {
            deadParentModel = model;
            return this;
        }

        @Info("Sets the model generation for the immature state")
        public Normal juvenileModelGenerator(Consumer<ModelGenerator> gen) {
            deadModelGenerator = gen;
            return this;
        }

        @Override
        protected void generateBlockState(VariantBlockStateGenerator bs) {
            super.generateBlockState(bs);
            bs.simpleVariant("mature=false", newID("block/", "_juvenile"));
        }

        @Override
        protected void generateBlockModels(KubeAssetGenerator generator) {
            super.generateBlockModels(generator);
            generator.blockModel(id.withSuffix("_juvenile"), m -> {
                if (deadParentModel != null) {
                    m.parent(deadParentModel);
                    m.textures(textures);
                } else if (deadModelGenerator != null) {
                    deadModelGenerator.accept(m);
                } else {
                    m.parent(WILD_CROP);
                    m.textures(textures);
                }
            });
        }
    }

    public static class Double extends WildCropBlockBuilder {

        public transient BiConsumer<TallModelType, ModelGenerator> models;

        public Double(ResourceLocation i) {
            super(i, WildDoubleCropBlock::new);
            models = (t, m) -> {
                m.parent(t.top ? ModelUtil.CROP : WILD_CROP);
                m.textures(textures);
            };
        }

        @Override
        protected boolean isTall() {
            return true;
        }

        @Info("""
                Sets the model generation of the tall wild crop block, accepts a `BiConsumer` of a `TallModelType` and a model generator.
                The generator is unique for each type.
                
                There are four types: `MATURE_TOP`, `MATURE_BOTTOM`, `JUVENILE_TOP`, and `JUVENILE_BOTTOM`. These have two boolean properties
                which can be used to determine the model being generated. The properties are `.mature` and `.top`.
                """)
        public Double models(BiConsumer<TallModelType, ModelGenerator> models) {
            this.models = this.models.andThen(models);
            return this;
        }

        @Override
        protected void generateBlockState(VariantBlockStateGenerator bs) {
            for (TallModelType m : TallModelType.VALUES) {
                bs.simpleVariant(m.v, m.modelEx(this));
            }
        }

        @Override
        protected void generateBlockModels(KubeAssetGenerator generator) {
            for (TallModelType t : TallModelType.VALUES) {
                generator.blockModel(t.model(this), m -> models.accept(t, m));
            }
        }
    }

    public enum TallModelType implements ISupplyModels {
        MATURE_TOP(true, true),
        MATURE_BOTTOM(true, false),
        JUVENILE_TOP(false, true),
        JUVENILE_BOTTOM(false, false);

        public static final TallModelType[] VALUES = values();

        @HideFromJS
        public final String v, str;
        public final boolean mature, top;

        TallModelType(boolean mature, boolean top) {
            v = "mature=" + mature + ",top=" + top;
            str = name().toLowerCase(Locale.ROOT);
            this.mature = mature;
            this.top = top;
        }

        @Override
        public String str() {
            return str;
        }
    }

    public static class Spreading extends WildCropBlockBuilder {

        private static final ResourceLocation SIDE = KubeJSTFC.tfc("block/crop/spreading_crop_side");

        public transient Supplier<? extends Block> fruit;
        private Supplier<? extends Block> f() { return fruit; }
        public transient BiConsumer<SpreadingModelPart, ModelGenerator> models;

        public Spreading(ResourceLocation i) {
            super(i, null);
            fruit = () -> Blocks.HONEY_BLOCK;
            models = (s, m) -> {
                m.parent(s.side ? SIDE : WILD_CROP);
                m.textures(textures);
            };
        }

        @Override
        protected boolean isTall() {
            return true;
        }

        @Info("Set the fruit block the crop spreads")
        public Spreading fruitBlock(Holder<Block> fruit) {
            this.fruit = Assistant.holderAsSupplier(fruit);
            return this;
        }

        @Info("""
                Sets the model generation of the spreading wild crop block, accepts a `BiConsumer` of a `SpreadingModelPart` and a model generator.
                The generator is unique for each part.
                
                There are four parts: `MATURE`, `MATURE_SIDE`, `JUVENILE`, and `JUVENILE_SIDE`. These have two boolean properties
                which can be used to determine the part currently being generated. The properties are `.mature` and `.side`.
                """)
        public Spreading models(BiConsumer<SpreadingModelPart, ModelGenerator> models) {
            this.models = this.models.andThen(models);
            return this;
        }

        @Override
        public Block createObject() {
            return new WildSpreadingCropBlock(createExtendedProperties(), this::f);
        }

        @Override
        protected boolean useMultipartBlockState() {
            return true;
        }

        @Override
        protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
            final ResourceLocation mature = SpreadingModelPart.MATURE.modelEx(this);
            final ResourceLocation matureSide = SpreadingModelPart.MATURE_SIDE.modelEx(this);
            final ResourceLocation juvenile = SpreadingModelPart.JUVENILE.modelEx(this);
            final ResourceLocation juvenileSide = SpreadingModelPart.JUVENILE_SIDE.modelEx(this);
            bs.part("mature=true", mature);
            bs.part("mature=false", juvenile);
            bs.part("east=true,mature=true", p -> p.model(matureSide).y(90));
            bs.part("east=true,mature=false", p -> p.model(juvenileSide).y(90));
            bs.part("north=true,mature=true", matureSide);
            bs.part("north=true,mature=false", juvenileSide);
            bs.part("south=true,mature=true", p -> p.model(matureSide).y(180));
            bs.part("south=true,mature=false", p -> p.model(juvenileSide).y(180));
            bs.part("west=true,mature=true", p -> p.model(matureSide).y(270));
            bs.part("west=true,mature=false", p -> p.model(juvenileSide).y(270));
        }

        @Override
        protected void generateBlockModels(KubeAssetGenerator generator) {
            for (SpreadingModelPart s : SpreadingModelPart.VALUES) {
                generator.blockModel(s.model(this), m -> models.accept(s, m));
            }
        }
    }

    public enum SpreadingModelPart implements ISupplyModels {
        MATURE(true, false),
        MATURE_SIDE(true, true),
        JUVENILE(false, false),
        JUVENILE_SIDE(false, true);

        public static final SpreadingModelPart[] VALUES = values();

        private final String str;
        public final boolean mature, side;

        SpreadingModelPart(boolean mature, boolean side) {
            str = name().toLowerCase(Locale.ROOT);
            this.mature = mature;
            this.side = side;
        }

        @Override
        public String str() {
            return str;
        }
    }
}
