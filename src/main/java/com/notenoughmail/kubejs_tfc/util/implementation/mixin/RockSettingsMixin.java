package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.IRockSettingsMixin;
import com.notenoughmail.kubejs_tfc.util.implementation.event.RockSettingsEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.RockSettingsEventJS.RockSettingsJS;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = RockSettings.class, remap = false)
public abstract class RockSettingsMixin implements IRockSettingsMixin {

    @Final
    @Shadow(remap = false)
    private static Map<ResourceLocation, RockSettings> PRESETS;

    @Override
    public void removeRegisteredLayer(ResourceLocation id) {
        PRESETS.remove(id);
    }

    @Override
    public void modifyRegisteredLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        RockSettingsJS rockSettings = new RockSettingsJS(PRESETS.remove(id));
        settings.accept(rockSettings);
        RockSettings.register(rockSettings.build());
    }

    @Inject(method = "getDefaults()Ljava/util/Map;", at = @At("HEAD"))
    private static void kubejs_tfc$getDefaults(CallbackInfoReturnable<Map<ResourceLocation, RockSettings>> cir) {
        RockSettingsEventJS.applyQueuedEdits();
    }
}