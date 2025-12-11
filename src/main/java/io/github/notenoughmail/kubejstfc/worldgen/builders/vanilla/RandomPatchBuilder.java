package io.github.notenoughmail.kubejstfc.worldgen.builders.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

@ReturnsSelf
public class RandomPatchBuilder extends ConfiguredFeatureBuilder<RandomPatchFeature, RandomPatchConfiguration> {

    public transient int tries, xzSpread, ySpread;
    public transient Holder<PlacedFeature> feature;

    public RandomPatchBuilder(ResourceLocation id) {
        super(id);
        tries = 128;
        xzSpread = 7;
        ySpread = 3;
        feature = placed(KubeJSTFC.tfc("plant/cattail"));
    }

    @Info("The number of attempts to place the internal feature")
    public RandomPatchBuilder tries(int t) {
        tries = positive(t);
        return this;
    }

    @Info("The horizontal and vertical spread")
    public RandomPatchBuilder spread(int horizontal, int vertical) {
        xzSpread = horizontal;
        ySpread = vertical;
        return this;
    }

    @Info("The internal feature to place")
    public RandomPatchBuilder feature(Holder.Reference<PlacedFeature> feature) {
        this.feature = feature;
        return this;
    }

    @Override
    public Supplier<RandomPatchFeature> feature() {
        return () -> Cast.to(Feature.RANDOM_PATCH);
    }

    @Override
    public RandomPatchConfiguration createFeatureConfig() {
        return new RandomPatchConfiguration(
                tries,
                xzSpread,
                ySpread,
                feature
        );
    }
}
