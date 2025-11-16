package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.StationaryBerryBushBlock;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class StationaryBerryBushBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public static final Lifecycle[] LC_VALUES = Lifecycle.values();

    public transient final Lifecycle[] lifecycles;
    public transient final DataManager.Reference<ClimateRange> climateRange;
    @Nullable
    public transient ItemBuilder productItem;
    @Nullable
    public transient Supplier<Item> product;
    public transient ModelFunc models;

    public StationaryBerryBushBlockBuilder(ResourceLocation i) {
        super(i);
        lifecycles = new Lifecycle[]{Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT};
        climateRange = ClimateRange.MANAGER.getReference(id);
        productItem = new ItemBuilder(id.withSuffix("_product"));
        product = null;
        models = initModels();
        renderType(BlockRenderType.CUTOUT_MIPPED);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
    }

    protected ModelFunc initModels() {
        return (lc, stage, m) -> {
            m.parent(KubeJSTFC.tfc("block/plant/stationary_bush_" + stage));
            m.textures(textures);
        };
    }

    @HideFromJS
    public Supplier<Item> productGetter()  {
        return product == null ? productItem : product;
    }

    @Info(value = "Sets the bush's lifecycle for the given month", params = {
            @Param(name = "month", value = "The month that the bush will have the given life cycle"),
            @Param(name = "lifecycle", value = "The lifecycle the bush will have for the given month")
    })
    public StationaryBerryBushBlockBuilder lifecycle(Month month, Lifecycle lifecycle) {
        lifecycles[month.ordinal()] = lifecycle;
        return this;
    }

    @Info("Modifies the bush's product item")
    public StationaryBerryBushBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        productItem.accept(this.productItem);
        return this;
    }

    @Info("Sets the bush's product item to be an existing item, will prevent the customizable product item from being created")
    public StationaryBerryBushBlockBuilder WithProduct(Holder<Item> product) {
        this.product = Assistant.holderAsSupplier(product);
        productItem = null;
        return this;
    }

    @Info("Sets the model for the given lifecycle and stage")
    public StationaryBerryBushBlockBuilder model(Lifecycle lifecycle, int stage, Consumer<ModelGenerator> modelGenerator) {
        models = models.andThen((l, s, m) -> {
            if (l == lifecycle && s == stage) {
                modelGenerator.accept(m);
            }
        });
        return this;
    }

    @Info("""
            Sets the model generation of the berry block, accepts a `TriConsumer` of a `Lifecycle`, an integer in the
            range [0, 2] representing the growth stage, and a model generator.
            The generator is unique for each lifecycle & stage combination.
            """)
    public StationaryBerryBushBlockBuilder models(ModelFunc models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return new StationaryBerryBushBlock(createExtendedProperties(), productGetter(), lifecycles, climateRange);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addItem(registry, productItem);
    }

    private static String modelSuffix(int stage, Lifecycle lc) {
        return "_" + lc.getSerializedName() + "_" + stage;
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (Lifecycle lc : LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final int stage = i;
                generator.blockModel(id.withSuffix(modelSuffix(stage, lc)), m -> models.apply(lc, stage, m));
            }
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> {
            m.parent(newID("block/", "_healthy_1"));
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (Lifecycle lc : LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                bs.simpleVariant("lifecycle=" + lc.getSerializedName() + ",stage=" + i, newID("block/", modelSuffix(i, lc)));
            }
        }
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        ModelUtil.basicItemModelGen(productItem, generator);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.determinedSinglePool(this, p -> p.when(LootUtil.sharpTools()));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .randomTicks()
                .blockEntity(TFCBlockEntities.BERRY_BUSH);
    }

    @FunctionalInterface
    public interface ModelFunc {
        void apply(Lifecycle lifecycle, int stage, ModelGenerator generator);

        default ModelFunc andThen(ModelFunc func) {
            return (l, s, m) -> {
                apply(l, s, m);
                func.apply(l, s, m);
            };
        }
    }
}
