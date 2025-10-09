package io.github.notenoughmail.kubejstfc.implementation.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockDataAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.ChunkGenAwareWorldGenerationContext;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.KubeChunkDataGenerator;
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

public record RockSurfaceRuleSource(RockType type, BlockState fallbackState, SurfaceRules.SurfaceRule fallbackRule) implements SurfaceRules.RuleSource {

    private RockSurfaceRuleSource(RockType type, BlockState fallbackState) {
        this(type, fallbackState, (x, y, z) -> fallbackState);
    }

    public static KeyDispatchDataCodec<RockSurfaceRuleSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(inst -> inst.group(
            RockType.CODEC.optionalFieldOf("rock_block", RockType.RAW).forGetter(RockSurfaceRuleSource::type),
            Codecs.BLOCK_STATE.fieldOf("fallback_state").forGetter(RockSurfaceRuleSource::fallbackState)
    ).apply(inst, RockSurfaceRuleSource::new)));

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        if (context.context instanceof ChunkGenAwareWorldGenerationContext aware && aware.chunkGenerator instanceof ChunkGeneratorExtension ext) {
            final ChunkData data = ext.chunkDataGenerator().generate(context.chunk);
            if (ext.chunkDataGenerator() instanceof KubeChunkDataGenerator gen) {
                gen.generateFullIfNot(data, context.chunk); // Guarentee the RockRule has the surface y available. WORLD_SURFACE_WG and OCEAN_FLOOR_WG are available here
            }
            final RockData rocks = data.getRockData();
            if (((RockDataAccessor) (Object) rocks).kubejs_tfc$GetCache() == null) {
                rocks.useCache(context.chunk.getPos());
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
