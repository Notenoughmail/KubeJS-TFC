package io.github.notenoughmail.kubejstfc.util.mixin;

import io.github.notenoughmail.kubejstfc.implementation.extensions.IWrappedChunkGenerator;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.WrappedChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * <b>Purpose:</b><p>
 * See {@link StructureMixin} & {@link PlacementContextMixin}
 */
@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin implements IWrappedChunkGenerator {

    @Unique
    @Nullable
    private WrappedChunkGenerator kubejs_tfc$WrapperGen;

    @Override
    public void kubejs_tfc$SetWrapper(WrappedChunkGenerator gen) {
        kubejs_tfc$WrapperGen = gen;
    }

    @Override
    @Nullable
    public WrappedChunkGenerator kubejs_tfc$getWrapper() {
        return kubejs_tfc$WrapperGen;
    }
}
