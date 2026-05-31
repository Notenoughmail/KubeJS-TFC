package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.placement.ClimatePlacement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

@ReturnsSelf
public record ClimatePlacementBuilder(
        @Info("The minimum temperature")
        float minTemp,
        @Info("The maximum temperature")
        float maxTemp,
        @Info("The minimum groundwater")
        float minGroundwater,
        @Info("The maximum groundwater")
        float maxGroundwater,
        @Info("The minimum rain variance value")
        float minRainVariance,
        @Info("The maximum rain variance value")
        float maxRainVariance,
        @Info("If the rain variance sign should be ignored")
        boolean absoluteRainVariance,
        @Info("If values should be evaluated fuzzily")
        boolean fuzzy,
        @Info("If values should be evaluated before river influence")
        boolean ignoreRivers,
        @Info("The minimum forest density, in the range [0,)")
        int minForestDensity,
        @Info("The maximum forest density, in the range [0,)")
        int maxForestDensity,
        @Info("The minimum y-level")
        int minElevation,
        @Info("The maximum y-level")
        int maxElevation,
        @Info("The permitted forest types to spawn in")
        List<ForestType> forestTypes
) {

    public static final ClimatePlacementBuilder DEFAULT = new ClimatePlacementBuilder(
            Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
            -1F, 1F,
            false, false, false,
            0, 4,
            -64, 320,
            List.of()
    );

    @HideFromJS
    public <T extends Throwable> void verify(@Nullable String methodName, Function<String, T> throwable) throws T {
        if (minForestDensity < 0)
            throw throwable.apply(err(methodName, "minForestDensity"));
        if (maxForestDensity < 0)
            throw throwable.apply(err(methodName, "maxForestDensity"));
    }

    private static String err(@Nullable String method, String arg) {
        final String name = method == null ?
                arg :
                method + "." + arg;
        return "'" + name + "' must be >= 0";
    }

    @HideFromJS
    public ClimatePlacement build() {
        return new ClimatePlacement(
                minTemp, maxTemp,
                minGroundwater, maxGroundwater,
                minRainVariance, maxRainVariance,
                absoluteRainVariance,
                minForestDensity, maxForestDensity,
                forestTypes,
                minElevation, maxElevation,
                fuzzy,
                ignoreRivers
        );
    }
}
