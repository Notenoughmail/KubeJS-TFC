package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
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
import net.dries007.tfc.common.blockentities.BerryBushBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.StationaryBerryBushBlock;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class StationaryBerryBushBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final Lifecycle[] lifecycles;
    public transient final ItemBuilder productItem;
    public static final String[] lc = {"healthy", "dormant", "fruiting", "flowering"};
    @Nullable
    public transient ResourceLocation product;

    public StationaryBerryBushBlockBuilder(ResourceLocation i) {
        super(i);
        lifecycles = new Lifecycle[]{Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT};
        productItem = new BasicItemJS.Builder(newID("", "_product"));
        product = null;
        renderType("cutout_mipped");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
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

    @Override
    public Block createObject() {
        return new StationaryBerryBushBlock(createExtendedProperties(), product == null ? productItem : () -> RegistryInfo.ITEM.getValue(product), lifecycles, ClimateRange.MANAGER.register(id));
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
        for (String lifecycle : lc) {
            final String tex = newID("block/", "_" + lifecycle).toString();
            for (int i = 0 ; i < 3 ; i++) {
                final String parent = "tfc:block/plant/stationary_bush_" + i;
                generator.blockModel(newID("", "_" + lifecycle + "_" + i), m -> {
                    m.parent(parent);
                    m.texture("bush", tex);
                });
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
                        .addCondition(DataUtils.sharpToolsCondition());
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
}
