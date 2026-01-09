package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import io.github.notenoughmail.kubejstfc.events.startup.KubeDefaultWorldSettingsEvent;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.settings.Settings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Allow event driven modification of the {@link Settings} of TFC's world type
 */
@Mixin(TFCChunkGenerator.class)
public abstract class TFCChunkGeneratorMixin {

    @WrapOperation(method = "lambda$static$3", at = @At(value = "FIELD", target = "Lnet/dries007/tfc/world/settings/Settings;CODEC:Lcom/mojang/serialization/MapCodec;", opcode = Opcodes.GETSTATIC))
    private static MapCodec<Settings> kubejs_tfc$Modify(Operation<MapCodec<Settings>> getField) {
        return getField.call().mapResult(KubeDefaultWorldSettingsEvent.SETTINGS_TRANSFORMER);
    }
}
