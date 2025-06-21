package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ChunkData.class, remap = false)
public interface ChunkDataAccessor {

    @Accessor(value = "rainfallLayer", remap = false)
    LerpFloatLayer kubejs_tfc$Rain();

    @Accessor(value = "temperatureLayer", remap = false)
    LerpFloatLayer kubejs_tfc$Temp();
}
