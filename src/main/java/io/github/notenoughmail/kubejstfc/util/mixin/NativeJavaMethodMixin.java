package io.github.notenoughmail.kubejstfc.util.mixin;

import dev.latvian.mods.rhino.CachedMethodInfo;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.MemberBox;
import dev.latvian.mods.rhino.NativeJavaMethod;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.RecordIndexHint;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

/**
 * <b>Purpose:</b><p>
 * Allow usage of the {@link RecordIndexHint} annotation to disambiguate methods with the same number of parameters but
 * records at different parameter indexes
 */
@Mixin(NativeJavaMethod.class)
public abstract class NativeJavaMethodMixin {

    @Inject(
            method = "findFunction",
            at = @At(value = "NEW", target = "()Ljava/lang/StringBuilder;"),
            cancellable = true
    )
    private static void kubejs_tfc$UseHintsToDisambiguateMethods(
            Context cx,
            MemberBox[] methodsOrCtors,
            Object[] args,
            CallbackInfoReturnable<Integer> cir
    ) {
        final Boolean[] potentialRecords = Arrays.stream(args)
                .map(Assistant::canBeHandledByDefaultRecordWrapper)
                .toArray(Boolean[]::new);
        final IntList hintedValid = new IntArrayList(methodsOrCtors.length);
        for (int i = 0 ; i < methodsOrCtors.length ; i++) {
            final MemberBox member = methodsOrCtors[i];
            if (kubejs_tfc$CheckRecordHint(member, args.length, potentialRecords)) {
                hintedValid.add(i);
            }
        }
        if (hintedValid.size() == 1) {
            cir.setReturnValue(hintedValid.getInt(0));
        }
    }

    // Only supports methods with one record arg, but that's fine for my use case
    @Unique
    private static boolean kubejs_tfc$CheckRecordHint(
            MemberBox box,
            int argsLength,
            Boolean[] potentialRecords
    ) {
        if (box.parameters().count() == argsLength && box.getInfo() instanceof CachedMethodInfo info) {
            final RecordIndexHint hint = info.getCached().getAnnotation(RecordIndexHint.class);
            if (hint != null) {
                for (int i = 0 ; i < argsLength ; i++) {
                    if (potentialRecords[i] != (i == hint.value())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
