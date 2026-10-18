package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.IfPresent;
import io.github.notenoughmail.worldjs.WorldJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

/**
 * <b>Purpose:</b><p>
 * Capture list of builders so that builders can be added to it without direct access to {@link BuilderBase#createAdditionalObjects(AdditionalObjectRegistry) #createAdditionalObejcts}
 */
@IfPresent(WorldJS.MODID)
@Mixin(ServerScriptManager.class)
public class ServerScriptManagerMixin {

    @WrapOperation(method = "loadAdditional", at = @At(value = "NEW", target = "()Ljava/util/ArrayList;"))
    private static ArrayList<BuilderBase<?>> kubejs_tfc$CaptureBuilders(Operation<ArrayList<BuilderBase<?>>> original) {
        final ArrayList<BuilderBase<?>> list = original.call();
        Assistant.captureBuilders(list);
        return list;
    }

    @Inject(method = "loadAdditional", at = @At("RETURN"))
    private static void kubejs_tfc$ClearCapturedBuilders(CallbackInfo ci) {
        Assistant.captureBuilders(null);
    }
}
