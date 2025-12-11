package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.notenoughmail.kubejstfc.worldgen.generator.WrappedChunkGenerator;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <b>Purpose:</b><p>
 * {@link ChunkMap} special cases the {@link NoiseGeneratorSettings} passed to its {@link net.minecraft.world.level.levelgen.RandomState RandomState}
 * for noise based generators.
 * <p>
 * This mixin replicates that behavior with wrapped noise generators
 */
@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;dummy()Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;"))
    private NoiseGeneratorSettings kubejs_tfc$UseWrappedNoiseSettings(Operation<NoiseGeneratorSettings> original, @Local(argsOnly = true) ChunkGenerator generator) {
        if (generator instanceof WrappedChunkGenerator w && w.getWrapped() instanceof NoiseBasedChunkGenerator noise) {
            return noise.generatorSettings().value();
        }
        return original.call();
    }
}
