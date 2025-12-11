package io.github.notenoughmail.kubejstfc.implementation.extensions;

import io.github.notenoughmail.kubejstfc.worldgen.generator.WrappedChunkGenerator;
import org.jetbrains.annotations.Nullable;

public interface IWrappedChunkGenerator {

    default void kubejs_tfc$SetWrapper(WrappedChunkGenerator gen) {}

    @Nullable
    default WrappedChunkGenerator kubejs_tfc$getWrapper() {
        return null;
    }
}
