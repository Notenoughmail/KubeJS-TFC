package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.EventHandler;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This is required because while {@link dev.latvian.mods.kubejs.KubeJSPlugin#generateDataJsons(DataJsonGenerator) KubeJSPlugin#generateDataJsons}
 * works for most cases, it does not for metal definitions and worldgen features.
 * This is due to its cached jsons not being made until after recipes and tags are processed.
 * Thus, alloy recipes cannot use metals made through the 'tfc.data' event and worldgen features made through the
 * 'tfc.worlgen.data' event cannot be added to the correct tags to actually place them in world.
 */
@Mixin(value = ServerScriptManager.class, remap = false)
public abstract class ServerScripManagerMixin {

    @Unique
    private MultiPackResourceManager kubeJS_TFC$WrappedManager;
    @Unique
    private VirtualKubeJSDataPack kubeJS_TFC$VirtualDataPack;

    @Inject(method = "reloadScriptManager", at = @At(value = "HEAD"), remap = false)
    private void captureWrappedManager(ResourceManager resourceManager, CallbackInfo ci) {
        if (resourceManager instanceof MultiPackResourceManager multiManager) {
            kubeJS_TFC$WrappedManager = multiManager;
        }
    }

    @ModifyVariable(method = "wrapResourceManager", at = @At(value = "STORE", args = "class=dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack"), remap = false, ordinal = 1)
    private VirtualKubeJSDataPack captureVirtualDataPack(VirtualKubeJSDataPack pack) {
        kubeJS_TFC$VirtualDataPack = pack;
        KubeJSTFC.LOGGER.debug("Successfully captured the virtual data pack");
        return pack;
    }

    @Inject(method = "wrapResourceManager", at = @At(target = "Ldev/latvian/mods/kubejs/util/ConsoleJS;setLineNumber(Z)V", shift = At.Shift.BEFORE, value = "INVOKE", ordinal = 1), remap = false)
    private void postDataEvents(CloseableResourceManager original, CallbackInfoReturnable<MultiPackResourceManager> cir) {
        EventHandler.postDataEvents(kubeJS_TFC$VirtualDataPack, kubeJS_TFC$WrappedManager);
    }
}
