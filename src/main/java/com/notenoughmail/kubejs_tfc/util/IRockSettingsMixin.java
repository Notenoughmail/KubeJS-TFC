package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.util.implementation.event.RockSettingsEventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface IRockSettingsMixin {

    void removeRegisteredLayer(ResourceLocation id);
    void modifyRegisteredLayer(ResourceLocation id, Consumer<RockSettingsEventJS.RockSettingsJS> settings);
}