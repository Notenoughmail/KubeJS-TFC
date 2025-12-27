package io.github.notenoughmail.kubejstfc.worldgen.builders.base;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ReturnsSelf
public abstract class ConfiguredFeatureBuilder<F extends Feature<FC>, FC extends FeatureConfiguration> extends BuilderBase<ConfiguredFeature<FC, F>> {

    protected static RegistryAccess.Frozen access() {
        return RegistryAccessContainer.current.access();
    }

    public static <T> Holder<T> ref(ResourceKey<? extends Registry<T>> key, ResourceLocation id) {
        return Holder.Reference.createStandAlone(access().lookupOrThrow(key), ResourceKey.create(key, id));
    }

    public static <T> HolderSet<T> empty(ResourceKey<? extends Registry<T>> key, ResourceLocation id) {
        return HolderSet.emptyNamed(access().lookupOrThrow(key), TagKey.create(key, id));
    }

    public static Holder<PlacedFeature> placed(ResourceLocation id) {
        return ref(Registries.PLACED_FEATURE, id);
    }

    public static Holder<ConfiguredFeature<?, ?>> configured(ResourceLocation id) {
        return ref(Registries.CONFIGURED_FEATURE, id);
    }

    protected static <T> Optional<T> opt(@Nullable T t) {
        return Optional.ofNullable(t);
    }

    protected static int positive(int i) {
        return i > 0 ? i : 1;
    }

    protected static int clamp(int i, int min, int max) {
        return Math.clamp(i, min, max);
    }

    protected static float unit(float f) {
        return Math.clamp(f, 0F, 1F);
    }

    @Nullable
    public transient PlacedFeatureBuilder placement;

    public ConfiguredFeatureBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("Adds and modifies a placed feature associated with this configured feature")
    public ConfiguredFeatureBuilder<F, FC> withPlacement(Consumer<PlacedFeatureBuilder> builder) {
        placement = Util.make(new PlacedFeatureBuilder(id), builder);
        return this;
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        if (placement != null) {
            registry.add(Registries.PLACED_FEATURE, placement);
        }
    }

    @HideFromJS
    protected KubeRuntimeException exception(String message) {
        return new KubeRuntimeException(message)
                .source(sourceLine)
                .customData("configured feature", id);
    }

    @HideFromJS
    public abstract Supplier<F> feature();

    @HideFromJS
    public abstract FC createFeatureConfig();

    @Override
    public ConfiguredFeature<FC, F> createObject() {
        return new ConfiguredFeature<>(
                feature().get(),
                createFeatureConfig()
        );
    }
}
