package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.vein.ClusterVeinConfig;
import net.minecraft.resources.ResourceLocation;

@ReturnsSelf
public class ClusterVeinBuilder extends VeinBuilder<ClusterVeinConfig> {

    public transient int size;

    public ClusterVeinBuilder(ResourceLocation id) {
        super(id, TFCFeatures.CLUSTER_VEIN);
        size = 10;
    }

    @Info("The size of the vein")
    public ClusterVeinBuilder size(int s) {
        size = assertPositive(s, "size");
        return this;
    }

    @Override
    public ClusterVeinConfig createFeatureConfiguration() {
        return new ClusterVeinConfig(
                baseConfig(),
                size
        );
    }
}
