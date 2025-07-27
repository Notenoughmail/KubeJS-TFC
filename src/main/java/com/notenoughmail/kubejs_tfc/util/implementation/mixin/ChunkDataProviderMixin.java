package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.KubeChunkDataGenerator;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataGenerator;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

/**
 * <b>Purpose:</b><p>
 * Permits the contract of getting {@link ChunkData} that has been promoted to {@code PARTIAL} from {@link ChunkDataProvider#get(ChunkAccess)}
 */
@Mixin(value = ChunkDataProvider.class, remap = false)
public class ChunkDataProviderMixin {

    @Shadow
    @Final
    private ChunkDataGenerator generator;

    @WrapOperation(method = "get(Lnet/minecraft/world/level/chunk/ChunkAccess;)Lnet/dries007/tfc/world/chunkdata/ChunkData;", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/world/chunkdata/ChunkDataGenerator;generate(Lnet/dries007/tfc/world/chunkdata/ChunkData;)V"), remap = false)
    private void kubejs_tfc$GenerateDataWithContext(ChunkDataGenerator instance, ChunkData chunkData, Operation<Void> original, @Local(argsOnly = true)ChunkAccess chunkAccess) {
        if (instance instanceof KubeChunkDataGenerator kube) {
            kube.generatePartialIfNot(chunkData, chunkAccess);
        } else {
            original.call(instance, chunkData);
        }
    }

    // TODO: 1.3.3 | This is merely a bandaid, find the root problem and fix *that*
    // Dirty, ugly hack that should NOT be needed, but something is going wrong somewhere and I have no idea where that might be
    @WrapOperation(method = "promotePartial", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private <V> V kubejs_tfc$FixPromotingToNullData(Map<ProtoChunk, ChunkData> instance, Object chunk, Operation<ChunkData> original) {
        ChunkData data = original.call(instance, chunk);
        if (data == null && chunk instanceof ChunkAccess access && generator instanceof KubeChunkDataGenerator kube) {
            data = new ChunkData(generator, access.getPos());
            kube.generateFullIfNot(data, access);
            KubeJSTFC.warningLog("For some reason a ProtoChunk with no ChunkData attempted to be promoted to a LevelChunk at {}", access.getPos());
        }
        return UtilsJS.cast(data);
    }
}
