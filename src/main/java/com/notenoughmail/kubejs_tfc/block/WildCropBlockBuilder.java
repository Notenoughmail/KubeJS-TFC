package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.FloodedWildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildSpreadingCropBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: Models, loot tables
public class WildCropBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public transient Consumer<ExtendedPropertiesJS> props;
    public transient Type type;
    public transient Supplier<Supplier<? extends Block>> spreadingFruitBlock;

    public WildCropBlockBuilder(ResourceLocation i) {
        super(i);
        props = p -> {};
        spreadingFruitBlock = () -> () -> Blocks.HONEY_BLOCK;
        type = Type.DEFAULT;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
        props.accept(propsJs);
        return propsJs.delegate()
                .randomTicks();
    }

    @Override
    public WildCropBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    public WildCropBlockBuilder spreadingFruitBlock(ResourceLocation fruitBlock) {
        spreadingFruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    public WildCropBlockBuilder type(Type type) {
        this.type = type;
        return this;
    }

    @Override
    public Block createObject() {
        return switch (type) {
            case DEFAULT -> new WildCropBlock(createExtendedProperties());
            case DOUBLE -> new WildDoubleCropBlock(createExtendedProperties());
            case FLOODED -> new FloodedWildCropBlock(createExtendedProperties());
            case SPREADING -> new WildSpreadingCropBlock(createExtendedProperties(), spreadingFruitBlock);
        };
    }

    public enum Type {
        DEFAULT,
        DOUBLE,
        FLOODED,
        SPREADING
    }
}
