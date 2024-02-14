package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

public interface ISupportExtendedProperties {

    @HideFromJS
    ExtendedProperties createExtendedProperties();

    BlockBehaviour.Properties createProperties();

    @Info(value = "Allows editing some of TFC's extended block properties")
    @Generics(value = ExtendedPropertiesJS.class)
    BlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties);

    @HideFromJS
    default ExtendedPropertiesJS extendedPropsJS() {
        return new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
    }

    record ExtendedPropertiesJS(@HideFromJS ExtendedProperties delegate) {

        public ExtendedPropertiesJS flammable(int flammability, int fireSpreadSpeed) {
            delegate.flammable(flammability, fireSpreadSpeed);
            return this;
        }

        public ExtendedPropertiesJS pathType(BlockPathTypes pathType) {
            delegate.pathType(pathType);
            return this;
        }

        public ExtendedPropertiesJS enchantPower(float power) {
            delegate.enchantPower(power);
            return this;
        }

        @Generics(value = BlockState.class)
        public ExtendedPropertiesJS enchantPowerFunction(ToDoubleFunction<BlockState> function) {
            delegate.enchantPower(function);
            return this;
        }
    }
}
