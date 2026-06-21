package io.github.notenoughmail.kubejstfc.builders.item;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

import java.util.function.Supplier;

public class StandingAndWallBlockItemBuilder extends BlockItemBuilder {

    public transient final Supplier<? extends BlockBuilder> wallBlock;

    public StandingAndWallBlockItemBuilder(ResourceLocation i, BlockBuilder block, Supplier<? extends BlockBuilder> wallBlock) {
        super(i);
        this.blockBuilder = block;
        this.wallBlock = wallBlock;
    }

    @Override
    public Item createObject() {
        return new StandingAndWallBlockItem(blockBuilder.get(), wallBlock.get().get(), createItemProperties(), Direction.DOWN);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        generateItemModels(generator);
    }
}
