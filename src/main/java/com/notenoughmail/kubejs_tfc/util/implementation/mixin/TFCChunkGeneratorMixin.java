package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import com.notenoughmail.kubejs_tfc.event.ModifyDefaultWorldGenSettingsEventJS;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.settings.Settings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TFCChunkGenerator.class, remap = false)
public abstract class TFCChunkGeneratorMixin {

    // TODO: Verify the lambda name is consistent, verify the #mapResult does not change the class & causes issues somewhere else
    @WrapOperation(method = "lambda$static$3", at = @At(value = "FIELD", target = "Lnet/dries007/tfc/world/settings/Settings;CODEC:Lcom/mojang/serialization/MapCodec;", opcode = Opcodes.GETSTATIC), remap = false)
    private static MapCodec<Settings> kubejs_tfc$Modify(Operation<MapCodec<Settings>> getField) {
        return getField.call().mapResult(ModifyDefaultWorldGenSettingsEventJS.SETTINGS_TRANSFORMER);
    }
}
