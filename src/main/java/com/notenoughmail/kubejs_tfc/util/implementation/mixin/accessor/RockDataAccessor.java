package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.world.chunkdata.ChunkRockDataCache;
import net.dries007.tfc.world.chunkdata.RockData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RockData.class, remap = false)
public interface RockDataAccessor {

    @Accessor(value = "cache", remap = false)
    ChunkRockDataCache kubejs_tfc$GetCache();
}
