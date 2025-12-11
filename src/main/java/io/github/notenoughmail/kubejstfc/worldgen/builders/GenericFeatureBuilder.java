package io.github.notenoughmail.kubejstfc.worldgen.builders;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

@ReturnsSelf
public class GenericFeatureBuilder<F extends Feature<FC>, FC extends FeatureConfiguration> extends ConfiguredFeatureBuilder<F, FC> {

    public transient ResourceLocation type;
    public transient JsonObject config;

    public GenericFeatureBuilder(ResourceLocation id) {
        super(id);
        type = KubeJSTFC.mc("no_op");
        config = new JsonObject();
    }

    public GenericFeatureBuilder<F, FC> of(ResourceLocation type, JsonObject config) {
        this.type = type;
        this.config = config;
        return this;
    }

    @Override
    public Supplier<F> feature() {
        throw new UnsupportedOperationException("Generic feature builders cannot supply their feature!");
    }

    @Override
    public FC createFeatureConfig() {
        throw new UnsupportedOperationException("Generic feature builders cannot create their config!");
    }

    @Override
    public ConfiguredFeature<FC, F> createObject() {
        return Cast.to(ConfiguredFeature.DIRECT_CODEC.decode(
                RegistryAccessContainer.current.json(),
                Assistant.json(j -> {
                    j.addProperty("type", type.toString());
                    j.add("config", config);
                })
        ).getOrThrow().getFirst());
    }
}
