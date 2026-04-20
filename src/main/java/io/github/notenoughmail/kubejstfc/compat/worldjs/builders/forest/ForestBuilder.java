package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.ForestConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

@ReturnsSelf
public class ForestBuilder extends ConfiguredFeatureBuilder.WithFeature<ForestConfig> {

    public transient HolderSet<ConfiguredFeature<?, ?>> entries;

    public ForestBuilder(ResourceLocation id) {
        super(id, TFCFeatures.FOREST);
        entries = HolderSet.empty();
    }

    @Info("The forest entries to place")
    public ForestBuilder entries(HolderSet<ConfiguredFeature<?, ?>> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    public ForestConfig createFeatureConfiguration() {
        return new ForestConfig(
                entries
        );
    }
}
