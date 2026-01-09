package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import io.github.notenoughmail.kubejstfc.util.Printer;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.util.SelfTests;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

/**
 * <b>Purpose:</b><p>
 * Insert & deduplicate console errors into the Kube console
 */
@Mixin(SelfTests.class)
public abstract class SelfTestsMixin {

    @WrapMethod(method = "logErrors")
    private static <T> boolean kubejs_tfc$LogErrors(String error, Collection<T> errors, Logger logger, Operation<Boolean> original) {
        if (TFCProperties.get().insertIntoConsole && !errors.isEmpty()) {
            final StringBuilder message = new StringBuilder();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                message.append(Printer.stringify(t));
            });
            ConsoleJS.SERVER.error(message.toString());
            if (TFCProperties.get().deduplicateSelfTestWarnings) return true;
        }
        return original.call(error, errors, logger);
    }

    @WrapMethod(method = "logWarnings")
    private static <T> boolean kubejs_tfc$LogWarnings(String error, Collection<T> errors, Logger logger, Operation<Boolean> original) {
        if (TFCProperties.get().insertIntoConsole && !errors.isEmpty()) {
            final StringBuilder message = new StringBuilder();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                message.append(Printer.stringify(t));
            });
            ConsoleJS.SERVER.error(message.toString());
            if (TFCProperties.get().deduplicateSelfTestWarnings) return true;
        }
        return original.call(error, errors, logger);
    }
}
