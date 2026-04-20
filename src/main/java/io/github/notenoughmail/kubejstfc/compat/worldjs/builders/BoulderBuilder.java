package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.BoulderConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BoulderBuilder extends ConfiguredFeatureBuilder.WithFeature<BoulderConfig> {

    public transient Map<Block, List<BlockState>> states = Map.of();

    public BoulderBuilder(ResourceLocation id, Supplier<? extends Feature<BoulderConfig>> feature) {
        super(id, feature);
    }

    @Info("Set the state to place when in a specific rock")
    public BoulderBuilder states(Map<Block, List<BlockState>> states) {
        this.states = states;
        return this;
    }

    @Override
    protected BoulderConfig createFeatureConfiguration() {
        return new BoulderConfig(
                states
        );
    }
}
