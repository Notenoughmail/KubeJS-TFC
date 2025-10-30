package io.github.notenoughmail.kubejstfc.builders.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public abstract class ExtendedPropertiesBlockBuilder extends BlockBuilder {

    public transient Consumer<ExtendedProperties> props;

    public ExtendedPropertiesBlockBuilder(ResourceLocation i) {
        super(i);
        props = p -> {};
    }

    @Info("Allows for editing of TFC's extended block properties")
    public BlockBuilder extendedProperties(Consumer<ExtendedProperties> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @HideFromJS
    public ExtendedProperties createExtendedProperties() {
        return Util.make(ExtendedProperties.of(createProperties()), e -> {
            props.accept(e);
            // By any means necessary...
            e.kubejs_tfc$SetBlockEntityFactory(null);
            e.kubejs_tfc$SetBlockEntityType(null);
            e.kubejs_tfc$SetClientTicker(null);
            e.kubejs_tfc$SetServerTick(null);
        });
    }
}
