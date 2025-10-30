package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.StationaryBerryBushBlock;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class StationaryBerryBushBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public static final Lifecycle[] LC_VALUES = Lifecycle.values();

    public transient final Lifecycle[] lifecycles;
    public transient final ItemBuilder productItem;
    @Nullable
    public transient ResourceLocation product;
    public transient ModelFunc models;

    public StationaryBerryBushBlockBuilder(ResourceLocation i) {
        super(i);
        lifecycles = new Lifecycle[]{Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT};
        productItem = new BasicItemJS.Builder(newID("", "_product"));
        product = null;
        models = initModels();
        renderType("cutout_mipped");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
    }

    protected ModelFunc initModels() {
        return (lc, stage, m) -> {
            m.parent("tfc:block/plant/stationary_bush_" + stage);
            m.texture(
                    "bush",
                    textures.has("#" + lc.ordinal() + "_" + stage) ?
                            textures.get("#" + lc.ordinal() + "_" + stage).getAsString() :
                            newID("block/", "_" + lc.getSerializedName()).toString()
            );
        };
    }

    @HideFromJS
    public Supplier<Item> productGetter()  {
        return product == null ? productItem : Lazy.of(() -> RegistryInfo.ITEM.getValue(product));
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
    @Generics(ItemBuilder.class)
    public StationaryBerryBushBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        productItem.accept(this.productItem);
        return this;
    }

    @Info("Sets the bush's product item to be an existing item, will prevent the customizable product item from being created")
    public StationaryBerryBushBlockBuilder productItem(ResourceLocation productId) {
        product = productId;
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

    @Deprecated
    @Info("Deprecated, please use `#models` and its new syntax")
    public StationaryBerryBushBlockBuilder allModels(BushModelsCreator modelsCreator) {
        return models(modelsCreator.upgrade());
    }

    @Info("Sets the texture for the given lifecycle and stage")
    public StationaryBerryBushBlockBuilder texture(Lifecycle lifecycle, int stage, String tex) {
        textures.addProperty("#" + lifecycle.ordinal() + "_" + stage, tex);
        return this;
    }

    @Override
    public Block createObject() {
        return new StationaryBerryBushBlock(createExtendedProperties(), productGetter(), lifecycles, ClimateRange.MANAGER.register(id));
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        if (product == null) {
            RegistryInfo.ITEM.addBuilder(productItem);
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent(newID("block/", "_healthy_1").toString());
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (Lifecycle l : LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final int stage = i;
                generator.blockModel(newID("", "_" + l.getSerializedName() + "_" + i), m -> models.apply(l, stage, m));
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (Lifecycle lc : LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                bs.simpleVariant("lifecycle=" + lc.getSerializedName() + ",stage=" + i, newID("block/", "_" + lc.getSerializedName() + "_" + i).toString());
            }
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(generator, this, p -> {
            p.survivesExplosion();
            p.addItem(itemBuilder.get().getDefaultInstance())
                    .addCondition(ResourceUtils.sharpToolsCondition());
        });
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .randomTicks()
                .blockEntity(TFCBlockEntities.BERRY_BUSH)
                .serverTicks(BerryBushBlockEntity::serverTick);
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

    @Deprecated
    @FunctionalInterface
    public interface BushModelsCreator {
        @Nullable
        @Generics({ ModelGenerator.class })
        Consumer<ModelGenerator> getFor(Lifecycle lifecycle, int stage);

        // The consumers must be 'precomputed' so there is a top level context for interface adaption
        default ModelFunc upgrade() {
            final Consumer<ModelGenerator>[][] gens = new Consumer[4][3];
            for (Lifecycle lc : LC_VALUES) {
                for (int i = 0 ; i < 3 ; i++) {
                    gens[lc.ordinal()][i] = getFor(lc, i);
                }
            }
            return (l, s, m) -> {
                final Consumer<ModelGenerator> gen = gens[l.ordinal()][s];
                if (gen != null) gen.accept(m);
            };
        }
    }
}
