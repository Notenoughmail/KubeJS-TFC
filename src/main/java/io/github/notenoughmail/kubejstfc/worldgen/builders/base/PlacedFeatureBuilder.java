package io.github.notenoughmail.kubejstfc.worldgen.builders.base;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.support.ClimatePlacementBuilder;
import net.dries007.tfc.world.placement.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ReturnsSelf
public class PlacedFeatureBuilder extends BuilderBase<PlacedFeature> {

    public transient ResourceLocation configuredFeature;

    public PlacedFeatureBuilder(ResourceLocation id) {
        super(id);
        configuredFeature = id;
    }

    @Override
    public PlacedFeature createObject() {
        return new PlacedFeature(
                ConfiguredFeatureBuilder.configured(configuredFeature),
                modifiers
        );
    }

    private final List<PlacementModifier> modifiers = new ArrayList<>();

    @Info("Add a placement modifier")
    public PlacedFeatureBuilder add(PlacementModifier modifier) {
        modifiers.add(modifier);
        return this;
    }

    @Info("Add a placement modifier from its json representation")
    public PlacedFeatureBuilder jsonPlacement(JsonObject json) {
        return add(PlacementModifier.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst());
    }

    @Info("Add a `tfc:biome` modifier")
    public PlacedFeatureBuilder tfcBiome() {
        return add(new BiomePlacement());
    }

    @Info("Add a `tfc:underground` modifier")
    public PlacedFeatureBuilder underground() {
        return add(new UndergroundPlacement());
    }

    @Info("Add a `tfc:volcano` modifier")
    public PlacedFeatureBuilder volcano(boolean center, float distance) {
        return add(new VolcanoPlacement(center, distance));
    }

    @Info("Add a `tfc:shallow_water` modifier")
    public PlacedFeatureBuilder shallowWater(int minDepth, int maxDepth) {
        return add(new ShallowWaterPlacement(minDepth, maxDepth));
    }

    @Info("Add a `tfc:no_solid_neighbors` modifier")
    public PlacedFeatureBuilder noSolidNeighbors() {
        return add(new NoSolidNeighborsPlacement());
    }

    @Info("Add a `tfc:near_fluid` modifier")
    public PlacedFeatureBuilder nearFluid(int radius, @Nullable List<Fluid> fluids) {
        return add(new NearFluidPlacement(radius, fluids));
    }

    @Info("Add a `tfc:tuff_cone` modifier")
    public PlacedFeatureBuilder tuffCone(boolean center, float distance) {
        return add(new TuffRingPlacement(center, distance));
    }

    @Info("Add a `tfc:tuya` modifier")
    public PlacedFeatureBuilder tuya(boolean center, float distance) {
        return add(new TuyaPlacement(center, distance));
    }

    @Info("Add a `tfc:intertidal` modifier")
    public PlacedFeatureBuilder intertidal(int minElevation, int maxElevation) {
        return add(new IntertidalPlacement(minElevation, maxElevation));
    }

    @Info("Add a `tfc:flat_enough` modifier")
    public PlacedFeatureBuilder flatEnough(float flatness, int radius, int maxDepth) {
        return add(new FlatEnoughPlacement(flatness, radius, maxDepth));
    }

    @Info("Add a `tfc:climate` modifier")
    public PlacedFeatureBuilder climate(Consumer<ClimatePlacementBuilder> climate) {
        return add(ClimatePlacementBuilder.make(climate));
    }

    @Info("Add a `tfc:in_square` modifier")
    public PlacedFeatureBuilder inSquare() {
        return add(InSquarePlacement.spread());
    }

    @Info("Add a `minecraft:rarity_filter` modifier")
    public PlacedFeatureBuilder rarityFilter(int chance) {
        return add(RarityFilter.onAverageOnceEvery(chance));
    }

    @Info("Add a `minecraft:biome` modifier")
    public PlacedFeatureBuilder mcBiome() {
        return add(BiomeFilter.biome());
    }

    @Info("Add a `minecraft:count` modifier")
    public PlacedFeatureBuilder count(IntProvider count) {
        return add(CountPlacement.of(count));
    }

    @Info("Add a `minecraft:fixed_placement` modifier")
    public PlacedFeatureBuilder fixed(BlockPos... positions) {
        return add(FixedPlacement.of(positions));
    }

    @Info("Add a `minecraft:heightmap` modifier")
    public PlacedFeatureBuilder heightmap(Heightmap.Types heightmap) {
        return add(HeightmapPlacement.onHeightmap(heightmap));
    }

    @Info("Add a `minecraft:noise_based_count` modifier")
    public PlacedFeatureBuilder noiseBasedCount(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
        return add(NoiseBasedCountPlacement.of(noiseToCountRatio, noiseFactor, noiseOffset));
    }

    @Info("Add a `minecraft:noise_threshold_count` modifier")
    public PlacedFeatureBuilder noiseThresholdCount(double noiseLevel, int belowNoise, int aboveNoise) {
        return add(NoiseThresholdCountPlacement.of(noiseLevel, belowNoise, aboveNoise));
    }

    @Info("Add a `minecraft:surface_relative_threshold_filter` modifier")
    public PlacedFeatureBuilder surfaceRelativeThreshold(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
        return add(SurfaceRelativeThresholdFilter.of(heightmap, minInclusive, maxInclusive));
    }

    @Info("Add a `minecraft:surface_water_depth_filter` modifier")
    public PlacedFeatureBuilder surfaceWaterDepth(int maxWaterDepth) {
        return add(SurfaceWaterDepthFilter.forMaxDepth(maxWaterDepth));
    }
}
