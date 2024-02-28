package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

// TODO: 1.1.0 | JSDoc, models
public class SpreadingCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient Supplier<Supplier<? extends Block>> fruitBlock;

    public SpreadingCropBlockBuilder(ResourceLocation i) {
        super(i);
        fruitBlock = () -> () -> Blocks.HONEY_BLOCK; // Why the hell not
        type = Type.SPREADING;
    }

    public SpreadingCropBlockBuilder fruitBlock(ResourceLocation fruitBlock) {
        this.fruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtils.spreadingCrop(createExtendedProperties(), stages, dead, seeds, nutrient, climateRange, fruitBlock);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson == null) {
            blockstateJson = Util.make(new MultipartBlockStateGenerator(), this::blockStates).toJson();
        }
        super.generateAssetJsons(generator);
    }

    private void blockStates(MultipartBlockStateGenerator ms) {
        final String side = newID("block/", "_side").toString();
        final String age = newID("block/", "_age_").toString();
        for (int i = 0 ; i < stages ; i++) {
            ms.part("age=" + i, age + i);
        }
        for (int i = 0 ; i < 4 ; i++) {
            final int j = i;
            ms.part(ModelUtils.cardinalDirections[j] + "=true", p -> p.model(side).y(j * 90));
        }
    }
}
