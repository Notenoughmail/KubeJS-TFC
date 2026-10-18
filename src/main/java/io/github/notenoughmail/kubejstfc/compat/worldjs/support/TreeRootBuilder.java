package io.github.notenoughmail.kubejstfc.compat.worldjs.support;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.util.Validations;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.tree.RootConfig;
import net.dries007.tfc.world.stateprovider.SpecialRootPlacer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record TreeRootBuilder(
        @Info("The root blocks to place, mapped from the block they replace")
        Map<Block, List<WeightedValue<BlockState>>> blocks,
        @Info("The maximum horizontal distance the roots will place")
        int width,
        @Info("The maximum vertical distance the roots will place")
        int height,
        @Info("The number of times a root block will attempt to place")
        int tries,
        @Info("The chance a root position is skewed downward")
        @Nullable Float skewChance,
        @Info("If the roots are required for the tree to place")
        boolean required
) {

    @HideFromJS
    public RootConfig build(String name, Function<String, KubeRuntimeException> exception) {
        if (blocks.isEmpty())
            throw exception.apply("'%s.blocks' must not be empty!".formatted(name));
        if (skewChance != null)
            Validations.assertUnit(skewChance, "%s.skewChance".formatted(name));
        return new RootConfig(
                WorldgenPlugin.weightedTFC(blocks),
                Validations.assertPositive(width, "%s.width".formatted(name)),
                Validations.assertPositive(height, "%s.height".formatted(name)),
                Validations.assertPositive(tries, "%s.tries".formatted(name)),
                Optional.ofNullable(skewChance)
                        .map(SpecialRootPlacer::new),
                required
        );
    }
}
