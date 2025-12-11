package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.IfThenConfig;
import net.dries007.tfc.world.feature.IfThenFeature;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class IfThenBuilder extends ConfiguredFeatureBuilder<IfThenFeature, IfThenConfig> {

    public transient ResourceLocation ifFeature, thenFeature;

    public IfThenBuilder(ResourceLocation id) {
        super(id);
        ifFeature = thenFeature = KubeJSTFC.tfc("clam");
    }

    @Info("The features to place")
    public IfThenBuilder features(ResourceLocation ifFeature, ResourceLocation thenFeature) {
        this.ifFeature = ifFeature;
        this.thenFeature = thenFeature;
        return this;
    }

    @Override
    public Supplier<IfThenFeature> feature() {
        return TFCFeatures.IF_THEN;
    }

    @Override
    public IfThenConfig createFeatureConfig() {
        return new IfThenConfig(
                placed(ifFeature),
                placed(thenFeature)
        );
    }
}
