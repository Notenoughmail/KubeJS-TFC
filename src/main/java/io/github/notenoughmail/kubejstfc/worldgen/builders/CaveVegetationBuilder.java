package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.Weighted;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.cave.CaveVegetationConfig;
import net.dries007.tfc.world.feature.cave.CaveVegetationFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ReturnsSelf
public class CaveVegetationBuilder extends ConfiguredFeatureBuilder<CaveVegetationFeature, CaveVegetationConfig> {

    public transient Map<Block, List<Weighted<BlockState>>> states = Map.of();

    public CaveVegetationBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("The states to place")
    public CaveVegetationBuilder states(Map<Block, List<Weighted<BlockState>>> states) {
        this.states = states;
        return this;
    }

    @Override
    public Supplier<CaveVegetationFeature> feature() {
        return TFCFeatures.CAVE_VEGETATION;
    }

    @Override
    public CaveVegetationConfig createFeatureConfig() {
        return new CaveVegetationConfig(Weighted.toTFC(states));
    }
}
