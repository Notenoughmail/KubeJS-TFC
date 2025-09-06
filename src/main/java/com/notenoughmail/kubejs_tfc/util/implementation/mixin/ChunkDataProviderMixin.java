package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.KubeChunkDataGenerator;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataGenerator;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * Permits the contract of getting {@link ChunkData} that has been promoted to {@code PARTIAL} from {@link ChunkDataProvider#get(ChunkAccess)}
 */
@Mixin(value = ChunkDataProvider.class, remap = false)
public class ChunkDataProviderMixin {

    @WrapOperation(method = "get(Lnet/minecraft/world/level/chunk/ChunkAccess;)Lnet/dries007/tfc/world/chunkdata/ChunkData;", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/world/chunkdata/ChunkDataGenerator;generate(Lnet/dries007/tfc/world/chunkdata/ChunkData;)V"), remap = false)
    private void kubejs_tfc$GenerateDataWithContext(ChunkDataGenerator instance, ChunkData chunkData, Operation<Void> original, @Local(argsOnly = true)ChunkAccess chunkAccess) {
        if (instance instanceof KubeChunkDataGenerator kube) {
            kube.generatePartialIfNot(chunkData, chunkAccess);
        } else {
            original.call(instance, chunkData);
        }
    }
}
