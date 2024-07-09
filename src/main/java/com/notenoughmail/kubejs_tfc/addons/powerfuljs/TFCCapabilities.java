package com.notenoughmail.kubejs_tfc.addons.powerfuljs;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.dries007.tfc.common.capabilities.egg.EggCapability;
import net.dries007.tfc.common.capabilities.egg.IEgg;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.forge.Forging;
import net.dries007.tfc.common.capabilities.forge.ForgingCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

// TODO: 1.2.0 | This...
@SuppressWarnings("unused")
public interface TFCCapabilities {

    static HeatBlockBuilder customHeatBlockEntity() {
        return new HeatBlockBuilder();
    }

    Capability<IEgg> egg = EggCapability.CAPABILITY;
    Capability<IFood> food = FoodCapability.CAPABILITY;
    Capability<Forging> forging = ForgingCapability.CAPABILITY;
    Capability<IHeatBlock> blockHeat = HeatCapability.BLOCK_CAPABILITY;
    Capability<IHeat> itemHeat = HeatCapability.CAPABILITY;

    @FunctionalInterface
    interface TemperatureConsumer {
        void accept(BlockEntity be, float temperature);
    }

    class HeatBlockBuilder extends CapabilityBuilderForge<BlockEntity, IHeatBlock> {

        private ToFloatFunction<BlockEntity> getTemperature;
        private TemperatureConsumer setTemperature;
        private TemperatureConsumer setTemperatureIfWarmer;

        public HeatBlockBuilder getTemperature(ToFloatFunction<BlockEntity> getTemperature) {
            this.getTemperature = getTemperature;
            return this;
        }

        public HeatBlockBuilder setTemperature(TemperatureConsumer setTemperature) {
            this.setTemperature = setTemperature;
            return this;
        }

        public HeatBlockBuilder setTemperatureIfWarmer(TemperatureConsumer setTemperatureIfWarmer) {
            this.setTemperatureIfWarmer = setTemperatureIfWarmer;
            return this;
        }

        @Override
        public IHeatBlock getCapability(BlockEntity instance) {
            return new IHeatBlock() {
                @Override
                public float getTemperature() {
                    return getTemperature == null ? 0 : getTemperature.apply(instance);
                }

                @Override
                public void setTemperature(float temperature) {
                    if (setTemperature != null) {
                        setTemperature.accept(instance, temperature);
                    }
                }

                @Override
                public void setTemperatureIfWarmer(float temperature) {
                    if (setTemperatureIfWarmer == null) {
                        IHeatBlock.super.setTemperatureIfWarmer(temperature);
                    } else {
                        setTemperatureIfWarmer.accept(instance, temperature);
                    }
                }
            };
        }

        @Override
        public Capability<IHeatBlock> getCapabilityKey() {
            return HeatCapability.BLOCK_CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return KubeJSTFC.identifier("custom_heat");
        }
    }
}
