package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

@SuppressWarnings("unused")
public interface ISupportExtendedProperties {

    @HideFromJS
    ExtendedProperties createExtendedProperties();

    BlockBehaviour.Properties createProperties();

    @Info(value = "Allows editing some of TFC's extended block properties")
    @Generics(value = ExtendedPropertiesJS.class)
    BlockBuilder extendedProperties(Consumer<ExtendedPropertiesJS> extendedProperties);

    @HideFromJS
    default ExtendedPropertiesJS extendedPropsJS() {
        return new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
    }

    record ExtendedPropertiesJS(@HideFromJS ExtendedProperties delegate) {

        @Info(value = "Sets the flammability and fire spread speed of the block", params = {
                @Param(name = "flammability", value = "The flammability of the block"),
                @Param(name = "fireSpreadSpeed", value = "The fire spread speed of the block")
        })
        public ExtendedPropertiesJS flammable(int flammability, int fireSpreadSpeed) {
            delegate.flammable(flammability, fireSpreadSpeed);
            return this;
        }

        @Info(value = "Sets the pathing type of the block", params = {
                @Param(name = "pathType", value = "The path type of the block")
        })
        public ExtendedPropertiesJS pathType(BlockPathTypes pathType) {
            delegate.pathType(pathType);
            return this;
        }

        @Info(value = "Sets the enchantment power of the block", params = {
                @Param(name = "power", value = "The enchantment power")
        })
        public ExtendedPropertiesJS enchantPower(float power) {
            delegate.enchantPower(power);
            return this;
        }

        @Info(value = "Sets the enchantment power of the block based off of its block state", params = {
                @Param(name = "function", value = "A BlockState to number function")
        })
        @Generics(value = BlockState.class)
        public ExtendedPropertiesJS enchantPowerFunction(ToDoubleFunction<BlockState> function) {
            delegate.enchantPower(function);
            return this;
        }
    }
}
