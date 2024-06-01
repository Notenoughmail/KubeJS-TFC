package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.function.*;

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

        // Bouncers for the bouncers
        public ExtendedPropertiesJS noCollision() { delegate.noCollission(); return this; }
        public ExtendedPropertiesJS noOcclusion() { delegate.noOcclusion(); return this; }
        public ExtendedPropertiesJS friction(float friction) { delegate.friction(friction); return this; }
        public ExtendedPropertiesJS speedFactor(float speedFactor) { delegate.speedFactor(speedFactor); return this; }
        public ExtendedPropertiesJS jumpFactor(float jumpFactor) { delegate.jumpFactor(jumpFactor); return this; }
        public ExtendedPropertiesJS sound(SoundType sound) { delegate.sound(sound); return this; }
        @Generics(BlockState.class)
        public ExtendedPropertiesJS lightLevel(ToIntFunction<BlockState> lightLevel) { delegate.lightLevel(lightLevel); return this; }
        public ExtendedPropertiesJS strength(float destroyTime, float explosionResistance) { delegate.strength(destroyTime, explosionResistance); return this; }
        public ExtendedPropertiesJS instabreak() { delegate.instabreak(); return this; }
        public ExtendedPropertiesJS strength(float strength) { delegate.strength(strength); return this; }
        public ExtendedPropertiesJS randomTicks() { delegate.randomTicks(); return this; }
        public ExtendedPropertiesJS dynamicShape() { delegate.dynamicShape(); return this; }
        public ExtendedPropertiesJS noLootTable() { delegate.noLootTable(); return this; }
        public ExtendedPropertiesJS dropsLike(BlockSupplier block) { delegate.dropsLike(block); return this; }
        public ExtendedPropertiesJS air() { delegate.air(); return this; }
        @Generics(EntityType.class)
        public ExtendedPropertiesJS isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) { delegate.isValidSpawn(isValidSpawn); return this; }
        public ExtendedPropertiesJS isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) { delegate.isRedstoneConductor(isRedstoneConductor); return this; }
        public ExtendedPropertiesJS isSuffocating(BlockBehaviour.StatePredicate isSuffocating) { delegate.isSuffocating(isSuffocating); return this; }
        public ExtendedPropertiesJS isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) { delegate.isViewBlocking(isViewBlocking); return this; }
        public ExtendedPropertiesJS hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess) { delegate.hasPostProcess(hasPostProcess); return this; }
        public ExtendedPropertiesJS emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering) { delegate.emissiveRendering(emissiveRendering); return this; }
        public ExtendedPropertiesJS requiresCorrectToolForDrops() { delegate.requiresCorrectToolForDrops(); return this; }
        public ExtendedPropertiesJS mapColor(MapColor color) { delegate.mapColor(color); return this; }
        public ExtendedPropertiesJS mapColor(MapColorFunction mapColor) { delegate.mapColor(mapColor); return this; }
        public ExtendedPropertiesJS mapColor(DyeColor color) { delegate.mapColor(color); return this; }
        public ExtendedPropertiesJS destroyTime(float destroyTime) { delegate.destroyTime(destroyTime); return this; }
        public ExtendedPropertiesJS explosionResistance(float explosionResistance) { delegate.explosionResistance(explosionResistance); return this; }
        public ExtendedPropertiesJS ignitedByLava() { delegate.ignitedByLava(); return this; }
        public ExtendedPropertiesJS forceSolidOn() { delegate.forceSolidOn(); return this; }
        public ExtendedPropertiesJS forceSolidOff() { delegate.forceSolidOff(); return this; }
        public ExtendedPropertiesJS pushReaction(PushReaction reaction) { delegate.pushReaction(reaction); return this; }
        public ExtendedPropertiesJS offsetType(BlockBehaviour.OffsetType type) { delegate.offsetType(type); return this; }
        public ExtendedPropertiesJS noParticlesOnBreak() { delegate.noParticlesOnBreak(); return this; }
        public ExtendedPropertiesJS instrument(NoteBlockInstrument inst) { delegate.instrument(inst); return this; }
        public ExtendedPropertiesJS defaultInstrument() { return instrument(NoteBlockInstrument.HARP); }
        public ExtendedPropertiesJS replaceable() { delegate.replaceable(); return this; }
    }

    @FunctionalInterface
    interface MapColorFunction extends Function<BlockState, MapColor> {
        MapColor apply(BlockState state);
    }

    @FunctionalInterface
    interface BlockSupplier extends Supplier<Block> {
        Block get();
    }
}
