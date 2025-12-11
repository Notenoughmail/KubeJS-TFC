package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

@ReturnsSelf
public abstract class TreeBuilder<F extends Feature<FC>, FC extends FeatureConfiguration> extends ConfiguredFeatureBuilder<F, FC> {

    @Nullable
    public transient TrunkConfig trunk;
    @Nullable
    public transient TreeRootBuilder roots;
    public transient TreePlacementConfig treePlacement;

    public TreeBuilder(ResourceLocation id) {
        super(id);
        treePlacement = new TreePlacementConfig(5, 3, TreePlacementConfig.GroundType.NORMAL);
    }

    @Info("The trunk properties")
    public TreeBuilder<F, FC> trunk(TrunkConfig trunk) {
        this.trunk = trunk;
        return this;
    }

    @Info("The root system properties")
    public TreeBuilder<F, FC> roots(TreeRootBuilder roots) {
        this.roots = roots;
        return this;
    }

    @Info("The tree placement properties")
    public TreeBuilder<F, FC> treePlacement(TreePlacementConfig placement) {
        treePlacement = placement;
        return this;
    }
}
