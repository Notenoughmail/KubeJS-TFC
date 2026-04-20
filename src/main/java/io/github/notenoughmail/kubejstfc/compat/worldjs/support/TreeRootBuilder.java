package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.tree.RootConfig;
import net.dries007.tfc.world.stateprovider.SpecialRootPlacer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record TreeRootBuilder(Map<Block, List<WeightedValue<BlockState>>> blocks, int width, int height, int tries, @Nullable Float skewChance, boolean required) {

    @HideFromJS
    public RootConfig build() {
        return new RootConfig(
                WorldgenPlugin.weightedTFC(blocks),
                width,
                height,
                tries,
                Optional.ofNullable(skewChance)
                        .map(SpecialRootPlacer::new),
                required
        );
    }
}
