package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.cave.CaveVegetationConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

@ReturnsSelf
public class CaveVegetationBuilder extends ConfiguredFeatureBuilder.WithFeature<CaveVegetationConfig> {

    public transient Map<Block, List<WeightedValue<BlockState>>> states = Map.of();

    public CaveVegetationBuilder(ResourceLocation id) {
        super(id, TFCFeatures.CAVE_VEGETATION);
    }

    @Info("The states to place")
    public CaveVegetationBuilder states(Map<Block, List<WeightedValue<BlockState>>> states) {
        this.states = states;
        return this;
    }

    @Override
    public CaveVegetationConfig createFeatureConfiguration() {
        return new CaveVegetationConfig(WorldgenPlugin.weightedTFC(states));
    }
}
