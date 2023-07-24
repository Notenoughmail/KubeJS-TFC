package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.util.implementation.event.RockSettingsEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@HideFromJS
public interface IRockSettingsMixin {

    void removeRegisteredLayer(ResourceLocation id);
    void modifyRegisteredLayer(ResourceLocation id, Consumer<RockSettingsEventJS.RockSettingsJS> settings);
}