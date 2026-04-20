package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.TreeRootBuilder;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ReturnsSelf
public abstract class TreeBuilder<FC extends FeatureConfiguration> extends ConfiguredFeatureBuilder.WithFeature<FC> {

    @Nullable
    public transient TrunkConfig trunk;
    @Nullable
    public transient TreeRootBuilder roots;
    public transient TreePlacementConfig treePlacement;

    public TreeBuilder(ResourceLocation id, Supplier<? extends Feature<FC>> feature) {
        super(id, feature);
        treePlacement = new TreePlacementConfig(5, 3, TreePlacementConfig.GroundType.NORMAL);
    }

    @Info("The trunk properties")
    public TreeBuilder<FC> trunk(TrunkConfig trunk) {
        this.trunk = trunk;
        return this;
    }

    @Info("The root system properties")
    public TreeBuilder<FC> roots(TreeRootBuilder roots) {
        this.roots = roots;
        return this;
    }

    @Info("The tree placement properties")
    public TreeBuilder<FC> treePlacement(TreePlacementConfig placement) {
        treePlacement = placement;
        return this;
    }
}
