package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cf.forest;

import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.ServerRegistryHolderSet;
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
    public ForestBuilder entries(Context ctx, ServerRegistryHolderSet<ConfiguredFeature<?, ?>> entries) {
        this.entries = entries.convertWithValidation("entries", SourceLine.of(ctx));
        return this;
    }

    @Override
    public ForestConfig createFeatureConfiguration() {
        return new ForestConfig(
                entries
        );
    }
}
