package io.github.notenoughmail.kubejstfc.builders.item;

import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class BlockItemWithAssetsBuilder extends BlockItemBuilder {

    private final Function<ItemBuilder, Item> builder;

    public BlockItemWithAssetsBuilder(ResourceLocation i, Function<ItemBuilder, Item> itemFunction) {
        super(i);
        builder = itemFunction;
    }

    @Override
    public Item createObject() {
        return builder.apply(this);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        generateItemModels(generator);
    }
}
