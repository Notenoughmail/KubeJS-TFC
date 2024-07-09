package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.mojang.serialization.JsonOps;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.ModifyDefaultWorldGenSettingsEventJS;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.biome.BiomeSourceExtension;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TFCChunkGenerator.class, remap = false)
public abstract class TFCChunkGeneratorMixin {

    @Shadow(remap = false)
    private Settings settings;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void kubejs_tfc$ModifyDefaultSettings(BiomeSourceExtension arg0, Holder<NoiseGeneratorSettings> arg1, Settings arg2, CallbackInfo ci) {
        if (!WorldGenUtils.worldgenHasBeenTransformed && EventHandlers.defaultSettings.hasListeners()) {
            EventHandlers.defaultSettings.post(new ModifyDefaultWorldGenSettingsEventJS((TFCChunkGenerator) (Object) this));
            KubeJSTFC.warningLog("Modified worldgen settings: {}", () -> Settings.CODEC.encoder().encodeStart(JsonOps.INSTANCE, settings).get().left().get());
            WorldGenUtils.worldgenHasBeenTransformed = true;
        }
    }
}
