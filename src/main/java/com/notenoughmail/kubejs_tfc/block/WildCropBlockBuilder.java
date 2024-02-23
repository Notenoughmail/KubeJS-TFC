package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
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

// TODO: Loot tables
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

    @Info(value = "Sets the block to use as the crop's fruit block, only applicable to the spreading type")
    public WildCropBlockBuilder spreadingFruitBlock(ResourceLocation fruitBlock) {
        spreadingFruitBlock = () -> () -> RegistryInfo.BLOCK.getValue(fruitBlock);
        return this;
    }

    @Info(value = "Sets the type of wild crop being made, may be 'default', 'double', 'flooded', or 'spreading'")
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

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson == null && type == Type.SPREADING) {
            generator.multipartState(id, this::spreadingBlockState);
        }
        super.generateAssetJsons(generator);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        super.generateDataJsons(generator);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String base = newID("block/", "").toString();
        switch (type) {
            case DEFAULT, FLOODED -> generator.blockModel(id, m -> {
                m.parent("tfc:block/wild_crop/crop");
                m.texture("crop", base);
            });
            case DOUBLE -> {
                generator.blockModel(newID("", "_top"), m -> {
                    m.parent("block/crop");
                    m.texture("crop", base + "_top");
                });
                generator.blockModel(newID("", "_bottom"), m -> {
                    m.parent("tfc:block/wild_crop/crop");
                    m.texture("crop", base + "_bottom");
                });
            }
            case SPREADING -> {
                generator.blockModel(id, m -> {
                    m.parent("tfc:block/wild_crop/crop");
                    m.texture("crop", base);
                });
                generator.blockModel(newID("", "_side"), m -> {
                    m.parent("tfc:block/crop/spreading_crop_side");
                    m.texture("crop", base + "_side");
                });
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        if (type != Type.SPREADING) {
            final String baseModel = newID("block/", "").toString();
            switch (type) {
                case DEFAULT, FLOODED -> {
                    bs.simpleVariant("mature=true", baseModel);
                    bs.simpleVariant("mature=false", baseModel);
                }
                case DOUBLE -> {
                    final String top = baseModel + "_top";
                    final String bottom = baseModel + "_bottom";
                    bs.simpleVariant("part=top,mature=true", top);
                    bs.simpleVariant("part=top,mature=false", top);
                    bs.simpleVariant("part=bottom,mature=true", bottom);
                    bs.simpleVariant("part=bottom,mature=false", bottom);
                }
            }
        }
    }

    private void spreadingBlockState(MultipartBlockStateGenerator ms) {
        final String baseModel = newID("block/", "").toString();
        final String side = baseModel + "_side";
        ms.part("mature=true", baseModel);
        ms.part("mature=false", baseModel);
        ms.part("east=true,mature=true", p -> p.model(side).y(90));
        ms.part("east=true,mature=false", p -> p.model(side).y(90));
        ms.part("north=true,mature=true", side);
        ms.part("north=true,mature=false", side);
        ms.part("south=true,mature=true", p -> p.model(side).y(180));
        ms.part("south=true,mature=false", p -> p.model(side).y(180));
        ms.part("west=true,mature=true", p -> p.model(side).y(270));
        ms.part("west=true,mature=false", p -> p.model(side).y(270));
    }

    public enum Type {
        DEFAULT,
        DOUBLE,
        FLOODED,
        SPREADING
    }
}
