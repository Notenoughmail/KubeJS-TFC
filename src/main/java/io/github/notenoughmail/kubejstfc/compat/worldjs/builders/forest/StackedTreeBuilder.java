package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.StackedTreeConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

@ReturnsSelf
public class StackedTreeBuilder extends TreeBuilder<StackedTreeConfig> {

    public transient List<StackedTreeConfig.Layer> layers;

    public StackedTreeBuilder(ResourceLocation id) {
        super(id, TFCFeatures.STACKED_TREE);
        layers = List.of();
        trunk = new TrunkConfig(Blocks.AIR.defaultBlockState(), 0, 2, false);
    }

    @Info("The layers that make up a tree")
    public StackedTreeBuilder layers(List<StackedTreeConfig.Layer> layers) {
        this.layers = layers;
        return this;
    }

    @Override
    public StackedTreeConfig createFeatureConfiguration() {
        assert trunk != null;
        return new StackedTreeConfig(
                layers,
                notNull(trunk, "trunk"),
                treePlacement,
                Optional.ofNullable(roots)
                        .map(TreeRootBuilder::build)
        );
    }
}
