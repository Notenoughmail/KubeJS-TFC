package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cf.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.TreeRootBuilder;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.feature.tree.RootConfig;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ReturnsSelf
public abstract class TreeBuilder<FC extends FeatureConfiguration> extends ConfiguredFeatureBuilder.WithFeature<FC> {

    public static final TreePlacementConfig DEFAULT_PLACEMENT = new TreePlacementConfig(5, 3, TreePlacementConfig.GroundType.NORMAL);

    @Nullable
    public transient TrunkConfig trunk;
    @Nullable
    public transient RootConfig roots;
    public transient TreePlacementConfig treePlacement;

    public TreeBuilder(ResourceLocation id, Supplier<? extends Feature<FC>> feature) {
        super(id, feature);
        treePlacement = DEFAULT_PLACEMENT;
    }

    @Info("The trunk properties")
    public TreeBuilder<FC> trunk(Context ctx, TrunkConfig trunk) {
        this.trunk = validateTrunk(ctx, trunk);
        return this;
    }

    protected TrunkConfig validateTrunk(Context ctx, TrunkConfig trunk) {
        Validations.assertNotGreaterThan(
                trunk.minHeight(),
                trunk.maxHeight(),
                "trunk.minHeight",
                "trunk.maxHeight",
                ctx
        );
        return trunk;
    }

    @Info("The root system properties")
    public TreeBuilder<FC> roots(TreeRootBuilder roots) {
        this.roots = roots.build("roots", this::exception);
        return this;
    }

    @Info("The tree placement properties")
    public TreeBuilder<FC> treePlacement(TreePlacementConfig placement) {
        treePlacement = placement;
        return this;
    }
}
