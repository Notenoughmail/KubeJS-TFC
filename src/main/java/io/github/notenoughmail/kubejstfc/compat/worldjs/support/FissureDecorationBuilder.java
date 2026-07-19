package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.FissureConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public record FissureDecorationBuilder(
        int rarity,
        int radius,
        int count,
        Map<Block, List<WeightedValue<BlockState>>> states
) {

    @HideFromJS
    public FissureConfig.Decoration build() {
        if (rarity < 1)
            throw new IllegalArgumentException("Decoration rarity must not be < 1");
        if (radius < 1)
            throw new IllegalArgumentException("Decoration radius must not be < 1");
        if (count < 1)
            throw new IllegalArgumentException("Decoration count must not be <1");
        return new FissureConfig.Decoration(
                WorldgenPlugin.weightedTFC(states),
                rarity,
                radius,
                count
        );
    }
}
