package io.github.notenoughmail.kubejstfc.implementation.worldgen.data;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.placement.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ReturnsSelf
public class FeaturePlacements {

    public static PlacedFeature make(Holder<ConfiguredFeature<?, ?>> configuredFeature, Consumer<FeaturePlacements> placement) {
        return new PlacedFeature(
                configuredFeature,
                Util.make(new FeaturePlacements(), placement).modifiers
        );
    }

    private final List<PlacementModifier> modifiers = new ArrayList<>();

    @Info("Add a placement modifier")
    public FeaturePlacements add(PlacementModifier modifier) {
        modifiers.add(modifier);
        return this;
    }

    @Info("Add a `tfc:biome` modifier")
    public FeaturePlacements tfcBiome() {
        return add(new BiomePlacement());
    }

    @Info("Add a `tfc:underground` modifier")
    public FeaturePlacements underground() {
        return add(new UndergroundPlacement());
    }

    @Info("Add a `tfc:volcano` modifier")
    public FeaturePlacements volcano(boolean center, float distance) {
        return add(new VolcanoPlacement(center, distance));
    }

    @Info("Add a `tfc:shallow_water` modifier")
    public FeaturePlacements shallowWater(int minDepth, int maxDepth) {
        return add(new ShallowWaterPlacement(minDepth, maxDepth));
    }

    @Info("Add a `tfc:no_solid_neighbors` modifier")
    public FeaturePlacements noSolidNeighbors() {
        return add(new NoSolidNeighborsPlacement());
    }

    @Info("Add a `tfc:near_fluid` modifier")
    public FeaturePlacements nearFluid(int radius, @Nullable List<Fluid> fluids) {
        return add(new NearFluidPlacement(radius, fluids));
    }

    @Info("Add a `tfc:tuff_cone` modifier")
    public FeaturePlacements tuffCone(boolean center, float distance) {
        return add(new TuffRingPlacement(center, distance));
    }

    @Info("Add a `tfc:tuya` modifier")
    public FeaturePlacements tuya(boolean center, float distance) {
        return add(new TuyaPlacement(center, distance));
    }

    @Info("Add a `tfc:intertidal` modifier")
    public FeaturePlacements intertidal(int minElevation, int maxElevation) {
        return add(new IntertidalPlacement(minElevation, maxElevation));
    }

    @Info("Add a `tfc:flat_enough` modifier")
    public FeaturePlacements flatEnough(float flatness, int radius, int maxDepth) {
        return add(new FlatEnoughPlacement(flatness, radius, maxDepth));
    }

    @Info("Add a `tfc:climate` modifier")
    public FeaturePlacements climate(Consumer<ClimatePlacementBuilder> climate) {
        return add(ClimatePlacementBuilder.make(climate));
    }

    @Info("Add a `tfc:in_square` modifier")
    public FeaturePlacements inSquare() {
        return add(InSquarePlacement.spread());
    }

    @Info("Add a `minecraft:rarity_filter` modifier")
    public FeaturePlacements rarityFilter(int chance) {
        return add(RarityFilter.onAverageOnceEvery(chance));
    }

    @Info("Add a `minecraft:biome` modifier")
    public FeaturePlacements mcBiome() {
        return add(BiomeFilter.biome());
    }

    @Info("Add a `minecraft:count` modifier")
    public FeaturePlacements count(IntProvider count) {
        return add(CountPlacement.of(count));
    }

    @Info("Add a `minecraft:fixed_placement` modifier")
    public FeaturePlacements fixed(BlockPos... positions) {
        return add(FixedPlacement.of(positions));
    }

    @Info("Add a `minecraft:heightmap` modifier")
    public FeaturePlacements heightmap(Heightmap.Types heightmap) {
        return add(HeightmapPlacement.onHeightmap(heightmap));
    }

    @Info("Add a `minecraft:noise_based_count` modifier")
    public FeaturePlacements noiseBasedCount(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
        return add(NoiseBasedCountPlacement.of(noiseToCountRatio, noiseFactor, noiseOffset));
    }

    @Info("Add a `minecraft:noise_threshold_count` modifier")
    public FeaturePlacements noiseThresholdCount(double noiseLevel, int belowNoise, int aboveNoise) {
        return add(NoiseThresholdCountPlacement.of(noiseLevel, belowNoise, aboveNoise));
    }

    @Info("Add a `minecraft:surface_relative_threshold_filter` modifier")
    public FeaturePlacements surfaceRelativeThreshold(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
        return add(SurfaceRelativeThresholdFilter.of(heightmap, minInclusive, maxInclusive));
    }

    @Info("Add a `minecraft:surface_water_depth_filter` modifier")
    public FeaturePlacements surfaceWaterDepth(int maxWaterDepth) {
        return add(SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth));
    }
}
