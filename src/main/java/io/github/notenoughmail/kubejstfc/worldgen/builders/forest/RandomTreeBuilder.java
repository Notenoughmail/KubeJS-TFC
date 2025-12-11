package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.RandomTreeConfig;
import net.dries007.tfc.world.feature.tree.RandomTreeFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

@ReturnsSelf
public class RandomTreeBuilder extends TreeBuilder<RandomTreeFeature, RandomTreeConfig> {

    public transient List<ResourceLocation> trees;

    public RandomTreeBuilder(ResourceLocation id) {
        super(id);
        trees = List.of();
    }

    @Info("The trees to place")
    public RandomTreeBuilder trees(List<ResourceLocation> trees) {
        this.trees = trees;
        return this;
    }

    @Override
    public Supplier<RandomTreeFeature> feature() {
        return TFCFeatures.RANDOM_TREE;
    }

    @Override
    public RandomTreeConfig createFeatureConfig() {
        return new RandomTreeConfig(
                trees,
                opt(trunk),
                treePlacement,
                opt(roots).map(TreeRootBuilder::build)
        );
    }
}
