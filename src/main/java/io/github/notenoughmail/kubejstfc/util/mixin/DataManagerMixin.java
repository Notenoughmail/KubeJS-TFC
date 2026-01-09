package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.util.data.DataManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Insert & deduplicate console errors into the Kube console
 */
@Mixin(DataManager.class)
public abstract class DataManagerMixin {

    @WrapOperation(method = "updateReferences", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V"))
    private void kubejs_tfc$LogLooseReferences(Logger instance, String s, Object[] objects, Operation<Void> original) {
        if (TFCProperties.get().insertIntoConsole) {
            ConsoleJS.SERVER.error(s.replace("{}", "%s").formatted(objects));
            if (TFCProperties.get().deduplicateDataManagerWarnings) return;
        }
        original.call(instance, s, objects);
    }
}
