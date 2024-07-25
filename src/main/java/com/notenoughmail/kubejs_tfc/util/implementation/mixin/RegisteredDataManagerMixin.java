package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.util.RegisteredDataManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RegisteredDataManager.class, remap = false)
public abstract class RegisteredDataManagerMixin {

    @WrapOperation(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "org/slf4j/Logger.error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    private void kubejs_tfc$Apply0(Logger instance, String string, Object object0, Object object1, Operation<Void> original) {
        if (KubeJSTFC.insertIntoConsole) {
            ConsoleJS.SERVER.error(string.replace("{}", "%s").formatted(object0, object1));
        }
        original.call(instance, string, object0, object1);
    }

    @WrapOperation(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "org/slf4j/Logger.error(Ljava/lang/String;[Ljava/lang/Object;)V"), remap = false)
    private void kubejs_tfc$Apply1(Logger instance, String string, Object[] objects, Operation<Void> original) {
        if (KubeJSTFC.insertIntoConsole) {
            ConsoleJS.SERVER.error(string.replace("{}", "%s").formatted(objects));
        }
        original.call(instance, string, objects);
    }
}
