package io.github.notenoughmail.kubejstfc.util.mixin;

import dev.latvian.mods.kubejs.script.ConsoleJS;
import io.github.notenoughmail.kubejstfc.util.Printer;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.util.SelfTests;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

/**
 * <b>Purpose:</b><p>
 * Insert & deduplicate console errors into the Kube console
 */
@Mixin(SelfTests.class)
public abstract class SelfTestsMixin {

    @Inject(method = "logErrors", at = @At("HEAD"), remap = false, cancellable = true)
    private static <T> void kubejs_tfc$LogErrors(String error, Collection<T> errors, Logger logger, CallbackInfoReturnable<Boolean> cir) {
        if (TFCProperties.get().insertIntoConsole && !errors.isEmpty()) {
            final StringBuilder message = new StringBuilder();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                message.append(Printer.stringify(t));
            });
            ConsoleJS.SERVER.error(message.toString());
            if (TFCProperties.get().deduplicateConsoleErrors) {
                cir.setReturnValue(!errors.isEmpty());
            }
        }
    }

    @Inject(method = "logWarnings", at = @At("HEAD"), remap = false, cancellable = true)
    private static <T> void kubejs_tfc$LogWarnings(String error, Collection<T> errors, Logger logger, CallbackInfoReturnable<Boolean> cir) {
        if (TFCProperties.get().insertIntoConsole && !errors.isEmpty()) {
            final StringBuilder message = new StringBuilder();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                message.append(Printer.stringify(t));
            });
            ConsoleJS.SERVER.warn(message.toString());
            if (TFCProperties.get().deduplicateConsoleErrors) {
                cir.setReturnValue(!error.isEmpty());
            }
        }
    }
}
