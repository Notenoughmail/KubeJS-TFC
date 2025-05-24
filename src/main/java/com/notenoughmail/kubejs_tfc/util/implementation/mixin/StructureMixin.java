package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.world.WrappedChunkGenerator;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

/**
 * <b>Purpose:</b><p>
 * Chunk generators pass themselves to {@link Structure#generate(RegistryAccess, ChunkGenerator, BiomeSource, RandomState, StructureTemplateManager, long, ChunkPos, int, LevelHeightAccessor, Predicate) Structure#generate}
 * in {@link ChunkGenerator#createStructures(RegistryAccess, ChunkGeneratorStructureState, StructureManager, ChunkAccess, StructureTemplateManager) #createStructures} and to
 * {@link StructureStart#placeInChunk(WorldGenLevel, StructureManager, ChunkGenerator, RandomSource, BoundingBox, ChunkPos) StructureStart#placeInChunk}
 * in {@link ChunkGenerator#applyBiomeDecoration(WorldGenLevel, ChunkAccess, StructureManager) #applyBiomeDecoration}.
 * <p>
 * These ensure the wrapper generator is used for any structure placement which may rely on {@link net.dries007.tfc.world.ChunkGeneratorExtension ChunkGeneratorExtensions}.
 */
@Mixin(Structure.class)
public abstract class StructureMixin {

    @ModifyVariable(method = "generate", at = @At("HEAD"), argsOnly = true)
    private ChunkGenerator kubejs_tfc$UseWrapperGen(ChunkGenerator value) {
        return WrappedChunkGenerator.getWrapper(value);
    }

    @Mixin(StructureStart.class)
    public static abstract class StructureStartMixin {

        @ModifyVariable(method = "placeInChunk", at = @At("HEAD"), argsOnly = true)
        private ChunkGenerator kubejs_tfc$UseWrapperGen(ChunkGenerator value) {
            return WrappedChunkGenerator.getWrapper(value);
        }
    }
}
