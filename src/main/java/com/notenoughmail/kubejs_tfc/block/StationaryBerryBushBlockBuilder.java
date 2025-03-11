package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
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
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.StationaryBerryBushBlock;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public static final String[] lc = {"healthy", "dormant", "fruiting", "flowering"};
    @Nullable
    public transient ResourceLocation product;
    public transient final Consumer<ModelGenerator>[][] models;

    public StationaryBerryBushBlockBuilder(ResourceLocation i) {
        super(i);
        lifecycles = new Lifecycle[]{Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT};
        productItem = new BasicItemJS.Builder(newID("", "_product"));
        product = null;
        models = new Consumer[4][3];
        initModels();
        renderType("cutout_mipped");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
    }

    protected void initModels() {
        allModels((lc, stage) -> m -> {
            m.parent("tfc:block/plant/stationary_bush_" + stage);
            m.texture(
                    "bush",
                    (textures.has("#" + lc.ordinal() + "_" + stage) ?
                            textures.get("#" + lc.ordinal() + "_" + stage) :
                            newID("block/", "_" + lc.getSerializedName())
                    ).toString()
            );
        });
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

    @Info(value = "Modifies the bush's product item")
    @Generics(value = ItemBuilder.class)
    public StationaryBerryBushBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        productItem.accept(this.productItem);
        return this;
    }

    @Info(value = "Sets the bush's product item to be an existing item, will prevent the customizable product item from being created")
    public StationaryBerryBushBlockBuilder productItem(ResourceLocation productId) {
        product = productId;
        return this;
    }

    @Info("Sets the model for the given lifecycle and stage")
    public StationaryBerryBushBlockBuilder model(Lifecycle lifecycle, int stage, Consumer<ModelGenerator> modelGenerator) {
        models[lifecycle.ordinal()][stage] = modelGenerator;
        return this;
    }

    @Info("Sets the model for all lifecycle and stage combinations via a callback")
    public StationaryBerryBushBlockBuilder allModels(BushModelsCreator modelsCreator) {
        for (Lifecycle lc : LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final var m = modelsCreator.getFor(lc, i);
                if (m != null) {
                    models[lc.ordinal()][i] = m;
                }
            }
        }
        return this;
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
        for (int i = 0 ; i < 4 ; i++) {
            for (int j = 0 ; j < 3 ; j++) {
                generator.blockModel(newID("", "_" + lc[i] + "_" + j), models[i][j]);
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (String lifecycle : lc) {
            for (int i = 0 ; i < 3 ; i++) {
                bs.simpleVariant("lifecycle=" + lifecycle + ",stage=" + i, newID("block/", "_" + lifecycle + "_" + i).toString());
            }
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else if (itemBuilder != null) {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(itemBuilder.get()))
                        .addCondition(ResourceUtils.sharpToolsCondition());
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
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
    public interface BushModelsCreator {
        @Nullable
        @Generics({ ModelGenerator.class })
        Consumer<ModelGenerator> getFor(Lifecycle lifecycle, int stage);
    }
}
