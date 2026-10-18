package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cg;

import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.misc.RockSettingsBuilder;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.builders.base.BiomeSourceBuilder;
import io.github.notenoughmail.worldjs.builders.base.ChunkGeneratorBuilder;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.biome.BiomeSourceExtension;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ReturnsSelf
public class TFCChunkGeneratorBuilder extends ChunkGeneratorBuilder<TFCChunkGenerator> {

    public transient BiomeSourceExtension ext;
    public transient Holder.Reference<NoiseGeneratorSettings> noiseSettings;
    public transient Settings settings;
    public transient final List<RockSettingsBuilder> rocks;

    public TFCChunkGeneratorBuilder(ResourceKey<LevelStem> id) {
        super(id);
        rocks = new ArrayList<>();
    }

    public TFCChunkGeneratorBuilder biomeSource(
            Context ctx,
            ResourceLocation type,
            Consumer<BiomeSourceBuilder<?>> biomeSourceBuilder
    ) {
        ext = Cast.to(WorldgenPlugin.BIOME_SOURCE_EXTENSION_TYPES.build(ctx, type, type, biomeSourceBuilder));
        return this;
    }

    public TFCChunkGeneratorBuilder noiseSettings(Holder.Reference<NoiseGeneratorSettings> settings) {
        noiseSettings = settings;
        return this;
    }

    public TFCChunkGeneratorBuilder settings(Context ctx, Consumer<SettingsBuilder> builder) {
        settings = SettingsBuilder.build(ctx, builder);
        return this;
    }

    @Override
    protected TFCChunkGenerator create() {
        return new TFCChunkGenerator(
                Validations.notNull(ext, "biomeSource", sourceLine),
                Validations.notNull(noiseSettings, "noiseSettings", sourceLine),
                Validations.notNull(settings, "settings", sourceLine)
        );
    }
}
