package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.world.WrappedChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * <b>Purpose:</b><p>
 * In {@link ChunkGenerator#applyBiomeDecoration(WorldGenLevel, ChunkAccess, StructureManager) ChunkGenerator#applyBiomeDecoration}, the generator is passed to
 * {@link net.minecraft.world.level.levelgen.placement.PlacedFeature#placeWithBiomeCheck(WorldGenLevel, ChunkGenerator, RandomSource, BlockPos) PlacedFeature#placeWithBiomeCheck}.
 * <p>
 * This ensures the wrapper generator is used for feature placement modifiers which rely on {@link net.dries007.tfc.world.ChunkGeneratorExtension ChunkGeneratorExtensions}.
 */
@Mixin(PlacementContext.class)
public abstract class PlacementContextMixin {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static ChunkGenerator kubejs_tfc$UseWrapperGen(ChunkGenerator value) {
        return WrappedChunkGenerator.getWrapper(value);
    }
}
