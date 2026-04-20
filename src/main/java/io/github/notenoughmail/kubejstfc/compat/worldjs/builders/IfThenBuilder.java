package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.IfThenConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@ReturnsSelf
public class IfThenBuilder extends ConfiguredFeatureBuilder.WithFeature<IfThenConfig> {

    public transient Holder.Reference<PlacedFeature> ifFeature, thenFeature;

    public IfThenBuilder(ResourceLocation id) {
        super(id, TFCFeatures.IF_THEN);
    }

    @Info("The features to place")
    public IfThenBuilder features(Holder.Reference<PlacedFeature> ifFeature, Holder.Reference<PlacedFeature> thenFeature) {
        this.ifFeature = ifFeature;
        this.thenFeature = thenFeature;
        return this;
    }

    @Override
    public IfThenConfig createFeatureConfiguration() {
        return new IfThenConfig(
                notNull(ifFeature, "ifFeature"),
                notNull(thenFeature, "thenFeature")
        );
    }
}
