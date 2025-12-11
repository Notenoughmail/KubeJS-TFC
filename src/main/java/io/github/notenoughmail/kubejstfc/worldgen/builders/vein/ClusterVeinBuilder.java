package io.github.notenoughmail.kubejstfc.worldgen.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.vein.ClusterVeinConfig;
import net.dries007.tfc.world.feature.vein.ClusterVeinFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class ClusterVeinBuilder extends VeinBuilder<ClusterVeinConfig, ClusterVeinFeature> {

    public transient int size;

    public ClusterVeinBuilder(ResourceLocation id) {
        super(id);
        size = 10;
    }

    @Info("The size of the vein")
    public ClusterVeinBuilder size(int s) {
        size = positive(s);
        return this;
    }

    @Override
    public Supplier<ClusterVeinFeature> feature() {
        return TFCFeatures.CLUSTER_VEIN;
    }

    @Override
    public ClusterVeinConfig createFeatureConfig() {
        return new ClusterVeinConfig(
                baseConfig(),
                size
        );
    }
}
