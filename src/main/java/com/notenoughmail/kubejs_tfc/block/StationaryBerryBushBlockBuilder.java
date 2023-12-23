package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.BerryBushBlockEntityJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.StationaryBerryBushBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

// TODO: Loot table
public class StationaryBerryBushBlockBuilder extends BlockBuilder implements ISupportExtendedProperties<StationaryBerryBushBlockBuilder> {

    public transient final Lifecycle[] lifecycles;
    public transient final ItemBuilder productItem;
    public static final String[] lc = {"healthy", "dormant", "fruiting", "flowering"};
    public transient Consumer<ExtendedPropertiesJS> props;

    public StationaryBerryBushBlockBuilder(ResourceLocation i) {
        super(i);
        lifecycles = new Lifecycle[]{Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT, Lifecycle.DORMANT};
        productItem = new BasicItemJS.Builder(newID("", "_product"));
        props = p -> {};
    }

    public StationaryBerryBushBlockBuilder lifecycle(int i, Lifecycle lifecycle) {
        lifecycles[i] = lifecycle;
        return this;
    }

    @Generics(value = ItemBuilder.class)
    public StationaryBerryBushBlockBuilder productItem(Consumer<ItemBuilder> productItem) {
        productItem.accept(this.productItem);
        return this;
    }

    @Override
    public Block createObject() {
        return new StationaryBerryBushBlock(createExtendedProperties(), productItem, lifecycles, ClimateRange.MANAGER.register(id));
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryUtils.addBerryBush(this);
        RegistryInfo.ITEM.addBuilder(productItem);
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
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
        props.accept(propsJs);
        return propsJs.delegate()
                .noOcclusion()
                .randomTicks()
                .blockEntity(RegistryUtils.getBerryBush())
                .serverTicks(BerryBushBlockEntityJS::serverTick);
    }

    @Override
    public StationaryBerryBushBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }
}
