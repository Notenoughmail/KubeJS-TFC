package io.github.notenoughmail.kubejstfc.implementation.worldgen.data;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.feature.FissureConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public record FissureDecorationBuilder(
        int rarity,
        int radius,
        int count,
        Map<Block, List<Weighted<BlockState>>> states
) {

    @HideFromJS
    public FissureConfig.Decoration builder() {
        return new FissureConfig.Decoration(
                Weighted.toTFC(states),
                rarity,
                radius,
                count
        );
    }
}
