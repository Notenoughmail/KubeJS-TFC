package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.world.WrappedChunkGenerator;
import org.jetbrains.annotations.Nullable;

public interface IChunkGenWrapper {

    void kubejs_tfc$SetWrapper(WrappedChunkGenerator gen);

    @Nullable
    WrappedChunkGenerator kubejs_tfc$getWrapper();
}
