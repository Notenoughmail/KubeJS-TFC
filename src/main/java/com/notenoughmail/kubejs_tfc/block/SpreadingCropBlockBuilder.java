package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpreadingCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient Supplier<Supplier<? extends Block>> fruitBlock;

    public SpreadingCropBlockBuilder(ResourceLocation i) {
        super(i, Type.SPREADING);
        fruitBlock = () -> () -> Blocks.HONEY_BLOCK; // Why the hell not
    }

    @Override
    protected boolean hasProduct() {
        return false;
    }

    @Info("Sets the block that will be used as the block's fruit, defaults to honey blocks")
    public SpreadingCropBlockBuilder fruitBlock(ResourceLocation fruitBlock) {
        this.fruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtils.spreadingCrop(createExtendedProperties(), stages, dead.get(), seeds.get(), nutrient, climateRange, fruitBlock, growthMod, expiryMod);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("side", tex);
        return super.textureAll(tex);
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
        generator.blockModel(newID("", "_side"), m -> {
            m.parent("tfc:block/crop/spreading_crop_side");
            m.textures(textures);
            m.texture("crop", "#side"); // Use the side texture for the crop key, in case someone wants it to be unique
        });
        super.generateBlockModelJsons(generator);
    }

    private void blockStates(MultipartBlockStateGenerator ms) {
        final String side = newID("block/", "_side").toString();
        final String age = newID("block/", "_age_").toString();
        for (int i = 0 ; i <= stages ; i++) {
            ms.part("age=" + i, age + i);
        }
        for (int i = 0 ; i < 4 ; i++) {
            final int j = i;
            ms.part(ResourceUtils.CARDINAL_DIRECTIONS[j].getSerializedName() + "=true", p -> p.model(side).y(j * 90));
        }
    }
}
