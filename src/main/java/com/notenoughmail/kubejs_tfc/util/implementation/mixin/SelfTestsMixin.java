package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.util.SelfTests;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.StringWriter;
import java.util.Collection;

@Mixin(value = SelfTests.class, remap = false)
public abstract class SelfTestsMixin {

    @Inject(method = "logErrors", at = @At("HEAD"), remap = false)
    private static <T> void kubejs_tfc$LogErrors(String error, Collection<T> errors, Logger logger, CallbackInfoReturnable<Boolean> cir) {
        if (KubeJSTFC.insertIntoConsole && !errors.isEmpty()) {
            final StringWriter message = new StringWriter();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                message.append(RegistryUtils.stringify(t));
            });
            ConsoleJS.SERVER.error(message.toString());
        }
    }
}
