package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.vein.Indicator;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record IndicatorBuilder(
        @Info("The maximum depth below the surface a vein will place indicators")
        int depth,
        @Info("The rarity to place indicators, as a fraction of horizontal locations the vein places ore blocks")
        int rarity,
        @Info("The rarity to place indicators underground when the vein is too deep to place on the surface, as a fraction of horizontal locations the vein places ore blocks")
        int undergroundRarity,
        @Info("The number of times to attempt to place an underground indicator in a given location")
        int undergroundCount,
        @Info("The indicator states to place")
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
