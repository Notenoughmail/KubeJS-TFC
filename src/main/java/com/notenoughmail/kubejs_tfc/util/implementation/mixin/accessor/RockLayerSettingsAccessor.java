package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.world.settings.RockLayerSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RockLayerSettings.class, remap = false)
public interface RockLayerSettingsAccessor {

    @Accessor(value = "data", remap = false)
    RockLayerSettings.Data kubejs_tfc$Data();
}
