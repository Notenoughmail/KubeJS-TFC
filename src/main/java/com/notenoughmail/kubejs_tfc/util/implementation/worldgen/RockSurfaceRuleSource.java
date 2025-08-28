package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockDataAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.SurfaceRulesContextAccessor;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.Codecs;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.Locale;
import java.util.function.Function;

// Tested by Pyrite on Discord and appears to be working
// A sequence rule source should be able to be used w/ this to do surface blocks
public record RockSurfaceRuleSource(RockType type, BlockState fallbackState, SurfaceRules.SurfaceRule fallbackRule) implements SurfaceRules.RuleSource {

    private RockSurfaceRuleSource(RockType type, BlockState fallbackState) {
        this(type, fallbackState, (x, y, z) -> fallbackState);
    }

    public static final KeyDispatchDataCodec<RockSurfaceRuleSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(inst -> inst.group(
            RockType.CODEC.optionalFieldOf("rock_block", RockType.RAW).forGetter(RockSurfaceRuleSource::type),
            Codecs.BLOCK_STATE.fieldOf("fallback_state").forGetter(RockSurfaceRuleSource::fallbackState)
    ).apply(inst, RockSurfaceRuleSource::new)));

    @Override
    public KeyDispatchDataCodec<RockSurfaceRuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {

        final SurfaceRulesContextAccessor access = (SurfaceRulesContextAccessor) (Object) context;
        assert access != null;
        if (access.kubejs_tfc$GetWorldCtx() instanceof ChunkGenAwareWorldGenerationContext aware && aware.chunkGenerator instanceof ChunkGeneratorExtension ext) {
            if (ext.chunkDataProvider().generator() instanceof KubeChunkDataGenerator gen) {
                final ChunkData data = ext.chunkDataProvider().get(access.kubejs_tfc$GetChunk());
                gen.generateFullIfNot(data, access.kubejs_tfc$GetChunk()); // Guarantee the RockRule has the surface y available. WORLD_SURFACE_WG and OCEAN_FLOOR_WG are available here
            }
            final RockData rocks = ext.chunkDataProvider().get(access.kubejs_tfc$GetChunk()).getRockData();
            if (((RockDataAccessor) rocks).kubejs_tfc$GetCache() == null) {
                rocks.useCache(access.kubejs_tfc$GetChunk().getPos());
            }
            return new RockRule(rocks, type);
        }
        return fallbackRule;
    }

    private record RockRule(RockData data, RockType type) implements SurfaceRules.SurfaceRule {

        @Override
        public BlockState tryApply(int x, int y, int z) {
            return type.get(data.getRock(x, y, z)).defaultBlockState();
        }
    }

    public enum RockType implements StringRepresentable {
        RAW(RockSettings::raw),
        HARDENED(RockSettings::hardened),
        GRAVEL(RockSettings::gravel),
        COBBLE(RockSettings::cobble),
        SAND(RockSettings::sand),
        SANDSTONE(RockSettings::sandstone)
        ;

        public static final Codec<RockType> CODEC = StringRepresentable.fromEnum(RockType::values);

        private final String name;
        private final Function<RockSettings, Block> transformer;

        RockType(Function<RockSettings, Block> transformer) {
            name = name().toLowerCase(Locale.ROOT);
            this.transformer = transformer;
        }

        public Block get(RockSettings settings) {
            return transformer.apply(settings);
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
