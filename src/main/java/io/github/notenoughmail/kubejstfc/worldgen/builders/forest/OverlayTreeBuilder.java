package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.worldgen.support.TreeRootBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.OverlayTreeConfig;
import net.dries007.tfc.world.feature.tree.OverlayTreeFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class OverlayTreeBuilder extends TreeBuilder<OverlayTreeFeature, OverlayTreeConfig> {

    public transient ResourceLocation base, overlay;
    public transient float integrity;

    public OverlayTreeBuilder(ResourceLocation id) {
        super(id);
        integrity = 0.5F;
        base = KubeJSTFC.tfc("white_cedar/base");
        overlay = KubeJSTFC.tfc("white_cedar/overlay");
    }

    @Info("The base and overlay structures")
    public OverlayTreeBuilder structures(ResourceLocation base, ResourceLocation overlay) {
        this.base = base;
        this.overlay = overlay;
        return this;
    }

    @Info("The integrity, in the range [0, 1], of the overlay structure")
    public OverlayTreeBuilder overlayIntegrity(float o) {
        integrity = unit(o);
        return this;
    }

    @Override
    public Supplier<OverlayTreeFeature> feature() {
        return TFCFeatures.OVERLAY_TREE;
    }

    @Override
    public OverlayTreeConfig createFeatureConfig() {
        return new OverlayTreeConfig(
                base,
                overlay,
                opt(trunk),
                integrity,
                treePlacement,
                opt(roots).map(TreeRootBuilder::build)
        );
    }
}
