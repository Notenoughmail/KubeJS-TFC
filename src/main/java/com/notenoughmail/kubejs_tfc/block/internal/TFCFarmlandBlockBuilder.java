package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class TFCFarmlandBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    public transient final TFCDirtBlockBuilder parent;
    public transient Consumer<ExtendedPropertiesJS> props;

    public TFCFarmlandBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        props = p -> {};
    }

    @Override
    public Block createObject() {
        return new FarmlandBlock(createExtendedProperties(), parent);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
        props.accept(propsJs);
        return propsJs.delegate()
                .blockEntity(RegistryUtils.getFarmland());
    }

    @Override
    public TFCFarmlandBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("block/template_farmland");
            m.texture("top", newID("block/", "").toString());
            m.texture("dirt", parent.textures.get("particle").getAsString());
        });
    }
}
