package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.vein.PipeVeinConfig;
import net.minecraft.resources.ResourceLocation;

@ReturnsSelf
public class PipeVeinBuilder extends VeinBuilder<PipeVeinConfig> {

    public transient int height, radius, minSkew, maxSkew, minSlant, maxSlant;
    public transient float sign;

    public PipeVeinBuilder(ResourceLocation id) {
        super(id, TFCFeatures.PIPE_VEIN);
        height = 10;
        radius = 3;
        minSkew = minSlant = -1;
    }

    @Info("The height of the pipe")
    public PipeVeinBuilder height(int h) {
        height = h;
        return this;
    }

    @Info("The radius of the pipe")
    public PipeVeinBuilder radius(int r) {
        radius = assertPositive(r, "radius");
        return this;
    }

    @Info("The skew range")
    public PipeVeinBuilder skew(int min, int max) {
        minSkew = min;
        maxSkew = max;
        return this;
    }

    @Info("The slant range")
    public PipeVeinBuilder slant(int min, int max) {
        minSlant = min;
        maxSlant = max;
        return this;
    }

    @Info("The sign of the slant")
    public PipeVeinBuilder sign(float s) {
        sign = assertUnit(s, "sign");
        return this;
    }

    @Override
    public PipeVeinConfig createFeatureConfiguration() {
        return new PipeVeinConfig(
                baseConfig(),
                height,
                radius,
                minSkew,
                maxSkew,
                minSlant,
                maxSlant,
                sign
        );
    }
}
