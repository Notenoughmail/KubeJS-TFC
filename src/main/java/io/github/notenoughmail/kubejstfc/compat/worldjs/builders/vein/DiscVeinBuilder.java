package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.world.feature.vein.DiscVeinConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

@ReturnsSelf
public class DiscVeinBuilder extends VeinBuilder<DiscVeinConfig> {

    public transient int size, height;

    public DiscVeinBuilder(ResourceLocation id, Supplier<? extends Feature<DiscVeinConfig>> feature) {
        super(id, feature);
        size = height = 5;
    }

    @Info("The radius of the vein")
    public DiscVeinBuilder size(int s) {
        size = assertPositive(s, "size");
        return this;
    }

    @Info("The height of the vein")
    public DiscVeinBuilder height(int h) {
        height = assertPositive(h, "height");
        return this;
    }

    @Override
    public DiscVeinConfig createFeatureConfiguration() {
        return new DiscVeinConfig(
                baseConfig(),
                size,
                height
        );
    }
}
