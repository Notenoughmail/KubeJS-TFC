package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.util.climate.OverworldClimateModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = OverworldClimateModel.class, remap = false)
public interface OverworldClimateModelAccessor {

    @Accessor(value = "climateSeed", remap = false)
    void kubejs_tfc$SetClimateSeed(long val);

    @Accessor(value = "temperatureScale", remap = false)
    void kubejs_tfc$SetTemperatureScale(float val);

    @Invoker(value = "updateNoise", remap = false)
    void kubejs_tfc$UpdateNoise();
}
