package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.placement.ClimatePlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ReturnsSelf
public class ClimatePlacementBuilder {

    public static ClimatePlacement make(Consumer<ClimatePlacementBuilder> builder) {
        final ClimatePlacementBuilder b = new ClimatePlacementBuilder();
        builder.accept(b);
        return new ClimatePlacement(
                b.minT, b.maxT,
                b.minGW, b.maxGW,
                b.minRV, b.maxRV,
                b.absoluteRainVariance,
                b.minF, b.maxF,
                b.forestTypes,
                b.minE, b.maxE,
                b.fuzzy,
                b.ignoreRivers
        );
    }

    private float
    minT = Float.NEGATIVE_INFINITY,
    maxT = Float.POSITIVE_INFINITY,
    minGW = Float.NEGATIVE_INFINITY,
    maxGW = Float.POSITIVE_INFINITY,
    minRV = -1F,
    maxRV = 1F;

    private boolean
    absoluteRainVariance = false,
    fuzzy = false,
    ignoreRivers = false;

    private int
    minF = 0,
    maxF = 4,
    minE = -64,
    maxE = 320;

    private final List<ForestType> forestTypes = new ArrayList<>();

    @Info("Set the minimum temperature")
    public ClimatePlacementBuilder minTemp(float f) {
        minT = f;
        return this;
    }

    @Info("Set the maximum temperature")
    public ClimatePlacementBuilder maxTemp(float f) {
        maxT = f;
        return this;
    }

    @Info("Set the minimum ground water")
    public ClimatePlacementBuilder minGroundWater(float f) {
        minGW = f;
        return this;
    }

    @Info("Set the maximum ground water")
    public ClimatePlacementBuilder maxGroundWater(float f) {
        maxGW = f;
        return this;
    }

    @Info("Set the minimum river variance")
    public ClimatePlacementBuilder minRiverVariance(float f) {
        minRV = f;
        return this;
    }

    @Info("Set the maximum river variance")
    public ClimatePlacementBuilder maxRiverVariance(float f) {
        maxRV = f;
         return this;
    }

    @Info("If the sign of the rain variance value should be ignored")
    public ClimatePlacementBuilder absoluteRainVariance(boolean absolute) {
        absoluteRainVariance = absolute;
        return this;
    }

    @Info("If the values should be evaluated fuzzily")
    public ClimatePlacementBuilder fuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
        return this;
    }

    @Info("If river contributions should be ignored")
    public ClimatePlacementBuilder ignoreRivers(boolean ignore) {
        ignoreRivers = ignore;
        return this;
    }

    @Info("Set the minimum forest density")
    public ClimatePlacementBuilder minForest(int i) {
        minF = i;
        return this;
    }

    @Info("Set the maximum forest density")
    public ClimatePlacementBuilder maxForest(int i) {
        maxF = i;
        return this;
    }

    @Info("Set the minimum elevation")
    public ClimatePlacementBuilder minElevation(int i) {
        minE = i;
        return this;
    }

    @Info("Set the maximum elevation")
    public ClimatePlacementBuilder maxElevation(int i) {
        maxE = i;
        return this;
    }

    @Info("Add a valid forest type")
    public ClimatePlacementBuilder withForestType(ForestType forestType) {
        forestTypes.add(forestType);
        return this;
    }

    @Info("Add multiple valid forest types")
    public ClimatePlacementBuilder withForestTypes(List<ForestType> forestTypes) {
        this.forestTypes.addAll(forestTypes);
        return this;
    }
}
