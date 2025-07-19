package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SurfaceRules.Context.class)
public interface SurfaceRulesContextAccessor {

    @Accessor("context")
    WorldGenerationContext kubejs_tfc$GetWorldCtx();

    @Accessor("chunk")
    ChunkAccess kubejs_tfc$GetChunk();
}
