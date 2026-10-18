package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cg;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.kubejstfc.worldgen.generator.WrappedChunkGenerator;
import io.github.notenoughmail.worldjs.builders.base.ChunkGeneratorBuilder;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.function.Consumer;

@ReturnsSelf
public class WrappedChunkGeneratorBuilder extends ChunkGeneratorBuilder<WrappedChunkGenerator> {

    public transient String key;
    public transient Settings settings;
    public transient ChunkGenerator wrappedGenerator;

    public WrappedChunkGeneratorBuilder(ResourceKey<LevelStem> id) {
        super(id);
    }

    public WrappedChunkGeneratorBuilder wrappedGenerator(
            Context ctx,
            ResourceLocation generatorType,
            Consumer<ChunkGeneratorBuilder<?>> generatorBuilder
    ) {
        wrappedGenerator = WorldgenPlugin.WRAPPED_CHUNK_GENERATOR_TYPES.build(ctx, generatorType, id, generatorBuilder);
        return this;
    }

    public WrappedChunkGeneratorBuilder key(String key) {
        this.key = key;
        return this;
    }

    public WrappedChunkGeneratorBuilder settings(Context ctx, Consumer<SettingsBuilder> builder) {
        settings = SettingsBuilder.build(ctx, builder);
        return this;
    }

    @Override
    protected WrappedChunkGenerator create() {
        return new WrappedChunkGenerator(
                Validations.notNull(wrappedGenerator, "wrappedGenerator", sourceLine),
                Validations.notNull(key, "key", sourceLine),
                Validations.notNull(settings, "settings", sourceLine)
        );
    }
}
