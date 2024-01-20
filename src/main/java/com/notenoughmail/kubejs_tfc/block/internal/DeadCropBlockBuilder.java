package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DeadClimbingCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.FloodedDeadCropBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class DeadCropBlockBuilder extends BlockBuilder implements ISupportExtendedProperties {

    private final AbstractCropBlockBuilder alive;
    private Consumer<ExtendedPropertiesJS> props;

    public DeadCropBlockBuilder(ResourceLocation i, AbstractCropBlockBuilder alive) {
        super(i);
        this.alive = alive;
        props = p -> {};
    }

    @Override
    public Block createObject() {
        return switch (alive.type) {
            case DEFAULT, SPREADING, PICKABLE -> new DeadCropBlock(createExtendedProperties(), alive.climateRange);
            case FLOODED -> new FloodedDeadCropBlock(createExtendedProperties(), alive.climateRange);
            case CLIMBING -> new DeadClimbingCropBlock(createExtendedProperties(), alive.climateRange);
        };
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = extendedPropsJS();
        props.accept(propsJs);
        return propsJs.delegate()
                .randomTicks();
    }

    @Override
    public DeadCropBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }
}
