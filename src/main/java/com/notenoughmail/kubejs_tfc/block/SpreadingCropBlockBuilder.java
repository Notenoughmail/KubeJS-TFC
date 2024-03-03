package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class SpreadingCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient Supplier<Supplier<? extends Block>> fruitBlock;

    public SpreadingCropBlockBuilder(ResourceLocation i) {
        super(i);
        fruitBlock = () -> () -> Blocks.HONEY_BLOCK; // Why the hell not
        type = Type.SPREADING;
    }

    @Override
    protected boolean hasProduct() {
        return false;
    }

    @Info(value = "Sets the block that will be used as the block's fruit, defaults to honey blocks")
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

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String base = newID("block/", "_").toString();
        generator.blockModel(newID("", "_side"), m -> {
            m.parent("tfc:block/crop/spreading_crop_side");
            m.texture("crop", base + "side");
        });
        for (int i = 0 ; i < stages ; i++) {
            final int j = i;
            generator.blockModel(newID("", "_age_" + j), m -> {
                m.parent("block/crop");
                m.texture("crop", base + j);
            });
        }
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
