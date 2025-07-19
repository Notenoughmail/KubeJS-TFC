package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.SurfaceRulesContextAccessor;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.RockData;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;

// TODO: 1.3.2 | Test... somehow
// A sequence rule source should be able to be used w/ this to do surface blocks
public record RockSurfaceRuleSource(BlockState fallbackState, SurfaceRules.SurfaceRule fallbackRule) implements SurfaceRules.RuleSource {

    private RockSurfaceRuleSource(BlockState fallbackState) {
        this(fallbackState, (pX, pY, pZ) -> fallbackState);
    }

    public static final KeyDispatchDataCodec<RockSurfaceRuleSource> CODEC = KeyDispatchDataCodec.of(BlockState.CODEC.xmap(RockSurfaceRuleSource::new, RockSurfaceRuleSource::fallbackState).fieldOf("fallback_state"));

    @Override
    public KeyDispatchDataCodec<RockSurfaceRuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {

        final SurfaceRulesContextAccessor access = (SurfaceRulesContextAccessor) (Object) context;
        assert access != null;
        if (access.kubejs_tfc$GetWorldCtx() instanceof ChunkGenAwareWorldGenerationContext aware && aware.chunkGenerator instanceof ChunkGeneratorExtension ext) {
            final RockData rocks = ext.chunkDataProvider().get(access.kubejs_tfc$GetChunk()).getRockData();
            return new RockRule(rocks);
        }
        return fallbackRule;
    }

    private record RockRule(RockData data) implements SurfaceRules.SurfaceRule {

        @Override
        public BlockState tryApply(int x, int y, int z) {
            return data.getRock(x, y, z).raw().defaultBlockState();
        }
    }
}
