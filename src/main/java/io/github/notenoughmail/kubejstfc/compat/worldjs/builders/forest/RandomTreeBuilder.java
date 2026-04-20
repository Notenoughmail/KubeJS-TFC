package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.RandomTreeConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

@ReturnsSelf
public class RandomTreeBuilder extends TreeBuilder<RandomTreeConfig> {

    public transient List<ResourceLocation> trees;

    public RandomTreeBuilder(ResourceLocation id) {
        super(id, TFCFeatures.RANDOM_TREE);
        trees = List.of();
    }

    @Info("The tree structures to place")
    public RandomTreeBuilder trees(List<ResourceLocation> trees) {
        this.trees = trees;
        return this;
    }

    @Override
    public RandomTreeConfig createFeatureConfiguration() {
        return new RandomTreeConfig(
                trees,
                Optional.ofNullable(trunk),
                treePlacement,
                Optional.ofNullable(roots)
                        .map(TreeRootBuilder::build)
        );
    }
}
