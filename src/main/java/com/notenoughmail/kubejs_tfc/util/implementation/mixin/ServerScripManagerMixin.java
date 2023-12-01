package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
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
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Redirect(method = "wrapResourceManager", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/server/ServerScriptManager;reload(Lnet/minecraft/server/packs/resources/ResourceManager;)V"))
    private void captureMultiManager(ServerScriptManager instance, ResourceManager resourceManager) {
        ((ServerScriptManager) (Object) this).reload(resourceManager);
        if (resourceManager instanceof MultiPackResourceManager multi) {
            kubeJS_TFC$WrappedManager = multi;
            KubeJSTFC.LOGGER.debug("Successfully captured the resource manager");
        }
    }

    @ModifyVariable(method = "wrapResourceManager", at = @At(value = "STORE", args = "class=dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack"), remap = false, ordinal = 1)
    private VirtualKubeJSDataPack captureVirtualDataPack(VirtualKubeJSDataPack pack) {
        kubeJS_TFC$VirtualDataPack = pack;
        KubeJSTFC.LOGGER.debug("Successfully captured the virtual data pack");
        return pack;
    }

    @Inject(method = "wrapResourceManager", at = @At(target = "Ldev/latvian/mods/kubejs/util/ConsoleJS;info(Ljava/lang/Object;)V", shift = At.Shift.BEFORE, value = "INVOKE"), remap = false)
    private void postDataEvents(CloseableResourceManager original, CallbackInfoReturnable<MultiPackResourceManager> cir) {
        EventHandlers.postDataEvents(kubeJS_TFC$VirtualDataPack, kubeJS_TFC$WrappedManager);
    }
}
