package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
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
public abstract class RockSettingsMixin {

    @Final
    @Shadow(remap = false)
    private static Map<ResourceLocation, RockSettings> PRESETS;

    @Inject(method = "getDefaults()Ljava/util/Map;", at = @At("HEAD"))
    private static void kubejs_tfc$modifyDefaultLayers(CallbackInfoReturnable<Map<ResourceLocation, RockSettings>> cir) {
        int i = 0;
        for (ResourceLocation id : RockSettingsEventJS.queuedRemovals) {
            PRESETS.remove(id);
            i++;
        }
        int j = 0;
        for (Map.Entry<ResourceLocation, Consumer<RockSettingsJS>> entry : RockSettingsEventJS.queuedModifications.entrySet()) {
            RockSettingsJS rockSettings = new RockSettingsJS(PRESETS.remove(entry.getKey()));
            entry.getValue().accept(rockSettings);
            RockSettings.register(rockSettings.build());
            j++;
        }
        if (i > 0 || j > 0) {
            KubeJSTFC.LOGGER.info("Applied {} queued rock layer removals and {} queued rock layer modifications", i, j);
        }
    }
}