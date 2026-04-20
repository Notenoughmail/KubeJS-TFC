package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.vein.Indicator;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record IndicatorBuilder(
        int depth,
        int rarity,
        int undergroundRarity,
        int undergroundCount,
        List<WeightedValue<BlockState>> states
) {

    @HideFromJS
    public Indicator build() {
        return new Indicator(
                depth <= 0 ? 1 : depth,
                rarity,
                undergroundRarity <= 0 ? 1 : undergroundRarity,
                undergroundCount,
                WorldgenPlugin.weightedTFC(states)
        );
    }
}
