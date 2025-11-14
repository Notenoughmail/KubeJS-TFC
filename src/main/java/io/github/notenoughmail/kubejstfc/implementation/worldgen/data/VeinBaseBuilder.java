package io.github.notenoughmail.kubejstfc.implementation.worldgen.data;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.feature.vein.Indicator;
import net.dries007.tfc.world.feature.vein.VeinConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record VeinBaseBuilder(
        Map<Block, List<Weighted<BlockState>>> states,
        int rarity,
        float density,
        int minY,
        int maxY,
        boolean projectToSurface,
        boolean projectOffset,
        long seed,
        boolean nearLava,
        @Nullable IndicatorBuilder indicator
) {

    @HideFromJS
    public VeinConfig build() {
        return new VeinConfig(
                Weighted.toTFC(states),
                Optional.ofNullable(indicator)
                        .map(IndicatorBuilder::build),
                rarity,
                density,
                minY,
                maxY,
                projectToSurface,
                projectOffset,
                seed,
                nearLava
        );
    }

    public record IndicatorBuilder(
            int depth,
            int rarity,
            int undergroundRarity,
            int undergroundCount,
            List<Weighted<BlockState>> states
    ) {

        @HideFromJS
        public Indicator build() {
            return new Indicator(
                    depth,
                    rarity,
                    undergroundRarity,
                    undergroundCount,
                    Weighted.toTFC(states)
            );
        }
    }
}
