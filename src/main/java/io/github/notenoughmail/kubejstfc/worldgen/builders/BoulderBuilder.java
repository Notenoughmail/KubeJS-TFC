package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.BoulderConfig;
import net.dries007.tfc.world.feature.BouldersFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ReturnsSelf
public class BoulderBuilder<F extends BouldersFeature> extends ConfiguredFeatureBuilder<F, BoulderConfig> {

    private final Supplier<F> feature;
    public transient Map<Block, List<BlockState>> states = Map.of();

    public BoulderBuilder(ResourceLocation id, Supplier<F> feature) {
        super(id);
        this.feature = feature;
    }

    @Info("Set the states to place when in a specific rock")
    public BoulderBuilder<F> states(Map<Block, List<BlockState>> states) {
        this.states = states;
        return this;
    }

    @Override
    public Supplier<F> feature() {
        return feature;
    }

    @Override
    public BoulderConfig createFeatureConfig() {
        return new BoulderConfig(states);
    }
}
