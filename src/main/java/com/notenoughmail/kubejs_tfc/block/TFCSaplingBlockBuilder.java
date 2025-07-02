package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.world.feature.tree.TFCTreeGrower;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class TFCSaplingBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient Supplier<Integer> growth;
    public transient ResourceLocation normalTree, oldGrowthTree;
    public transient boolean sand;

    public TFCSaplingBlockBuilder(ResourceLocation i) {
        super(i);
        normalTree = oldGrowthTree = ResourceLocation.tryBuild("minecraft", "oak");
        sand = false;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
    }

    @Info("Makes it so the sapling can be placed on sand")
    public TFCSaplingBlockBuilder placeableOnSand() {
        sand = true;
        return this;
    }

    @Info("Sets the number of days it takes for the sapling to grow")
    public TFCSaplingBlockBuilder growthDays(int i) {
        growth = () -> i;
        return this;
    }

    @Info("Sets the number of days, via a supplier, it takes for the sapling to grow")
    public TFCSaplingBlockBuilder growthDaysSupplier(Supplier<Integer> days) {
        growth = days;
        return this;
    }

    @Info("Sets the normal and old growth configured features of the sapling")
    public TFCSaplingBlockBuilder features(ResourceLocation normal, ResourceLocation oldGrowth) {
        normalTree = normal;
        oldGrowthTree = oldGrowth;
        return this;
    }

    @Info("Sets the normal and old growth configured features of the sapling to the same feature")
    public TFCSaplingBlockBuilder features(ResourceLocation trees) {
        normalTree = oldGrowthTree = trees;
        return this;
    }

    @Override
    public Block createObject() {
        return new TFCSaplingBlock(new TFCTreeGrower(normalTree, oldGrowthTree), createExtendedProperties(), growth, sand);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("cross", tex);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(TFCBlockEntities.TICK_COUNTER);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("block/cross");
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!itemBuilder.parentModel.isEmpty()) {
            m.parent(itemBuilder.parentModel);
        } else if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("minecraft:item/generated");
        }
        if (itemBuilder.textureJson.size() == 0) {
            itemBuilder.texture("layer0", textures.get("cross").getAsString());
        }
        m.textures(itemBuilder.textureJson);
    }
}
