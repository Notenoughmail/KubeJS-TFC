package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.StackedTreeConfig;
import net.dries007.tfc.world.feature.tree.StackedTreeFeature;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Supplier;

@ReturnsSelf
public class StackedTreeBuilder extends TreeBuilder<StackedTreeFeature, StackedTreeConfig> {

    public transient List<StackedTreeConfig.Layer> layers;

    public StackedTreeBuilder(ResourceLocation id) {
        super(id);
        layers = List.of();
        trunk = new TrunkConfig(Blocks.AIR.defaultBlockState(), 0, 2, false);
    }

    @Info("The layers that make up a tree")
    public StackedTreeBuilder layers(List<StackedTreeConfig.Layer> layers) {
        this.layers = layers;
        return this;
    }

    @Override
    public Supplier<StackedTreeFeature> feature() {
        return TFCFeatures.STACKED_TREE;
    }

    @Override
    public StackedTreeConfig createFeatureConfig() {
        assert trunk != null;
        return new StackedTreeConfig(
                layers,
                trunk,
                treePlacement,
                opt(roots).map(TreeRootBuilder::build)
        );
    }
}
