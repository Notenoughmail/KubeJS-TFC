package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.world.Seed;
import net.dries007.tfc.world.placement.StratovolcanoPlacement;
import net.dries007.tfc.world.volcano.VolcanoVariant;
import net.dries007.tfc.world.volcano.VolcanoVariants;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record StratovolcanoBuilder(
        @Info("If the feature should place exclusively near the center of the volcanic features")
        boolean center,
        @Info("If the feature should place exclusively near the offset center of volcanic features, if the variant has an offset center")
        boolean useOffsetCenter,
        @Info("The variant to place in")
        String variant,
        @Info("The minimum easing value")
        float minEasing,
        @Info("The maximum easing value")
        float maxEasing,
        @Info("The minimum hash value")
        float hashMin,
        @Info("The maximum hash value")
        float hashMax
) {

    public static final StratovolcanoBuilder DEFAULT = new StratovolcanoBuilder(
            false, false,
            "all",
            0F, 1F,
            0F, 1F
    );

    // Will dynamically update to any changes
    private static final Set<String> VOLCANO_VARIANTS = Stream.concat(
            Assistant.forAllMethods(
                    VolcanoVariants.class,
                    Seed.of(0L),
                    VolcanoVariant.class
            ).map(VolcanoVariant::name),
            Stream.of("all")
    ).collect(Collectors.toSet());

    @HideFromJS
    public StratovolcanoPlacement build() throws IllegalArgumentException {
        if (!VOLCANO_VARIANTS.contains(variant))
            throw new IllegalArgumentException("Unknown 'variant' %s. Known variants: %s".formatted(variant, VOLCANO_VARIANTS));
        if (minEasing < 0 || maxEasing < 0 || minEasing > 1 || maxEasing > 1)
            throw new IllegalArgumentException("'minEasing' and 'maxEasing' must be in the range [0, 1]");
        if (minEasing > maxEasing)
            throw new IllegalArgumentException("'maxEasing' must be greater than 'minEasing'");
        if (hashMin < 0 || hashMax < 0 || hashMin > 1 || hashMax > 1)
            throw new IllegalArgumentException("'hashMin' and 'hashMax' must be in the range [0, 1]");
        if (hashMin > hashMax)
            throw new IllegalArgumentException("'hashMax' must be greater than 'hashMin'");
        return new StratovolcanoPlacement(
                center,
                useOffsetCenter,
                variant,
                minEasing,
                maxEasing,
                hashMin,
                hashMax
        );
    }
}
