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
        return new FissureConfig.Decoration(
                WorldgenPlugin.weightedTFC(states),
                rarity,
                radius,
                count
        );
    }
}
