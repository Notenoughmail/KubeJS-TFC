package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.ForestConfig;
import net.dries007.tfc.world.feature.tree.ForestFeature;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

@ReturnsSelf
public class ForestBuilder extends ConfiguredFeatureBuilder<ForestFeature, ForestConfig> {

    public transient HolderSet<ConfiguredFeature<?, ?>> entries;

    public ForestBuilder(ResourceLocation id) {
        super(id);
        entries = empty(Registries.CONFIGURED_FEATURE, KubeJSTFC.tfc("forest_trees"));
    }

    @Info("The forest entries to place")
    public ForestBuilder entries(HolderSet<ConfiguredFeature<?, ?>> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    public Supplier<ForestFeature> feature() {
        return TFCFeatures.FOREST;
    }

    @Override
    public ForestConfig createFeatureConfig() {
        return new ForestConfig(
                entries
        );
    }
}
