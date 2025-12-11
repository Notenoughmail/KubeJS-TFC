package io.github.notenoughmail.kubejstfc.worldgen.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.vein.DiscVeinConfig;
import net.dries007.tfc.world.feature.vein.DiscVeinFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class DiscVeinBuilder extends VeinBuilder<DiscVeinConfig, DiscVeinFeature> {

    public transient int size, height;

    public DiscVeinBuilder(ResourceLocation id) {
        super(id);
        size = height = 5;
    }

    @Info("The radius of the vein")
    public DiscVeinBuilder size(int s) {
        size = positive(s);
        return this;
    }

    @Info("The height of the vein")
    public DiscVeinBuilder height(int h) {
        height = positive(h);
        return this;
    }

    @Override
    public Supplier<DiscVeinFeature> feature() {
        return TFCFeatures.DISC_VEIN;
    }

    @Override
    public DiscVeinConfig createFeatureConfig() {
        return new DiscVeinConfig(
                baseConfig(),
                size,
                height
        );
    }
}
