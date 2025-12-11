package io.github.notenoughmail.kubejstfc.worldgen.builders.vanilla;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.function.Supplier;

@ReturnsSelf
public class SimpleBlockBuilder extends ConfiguredFeatureBuilder<SimpleBlockFeature, SimpleBlockConfiguration> {

    public transient BlockStateProvider state;

    public SimpleBlockBuilder(ResourceLocation id) {
        super(id);
        state = BlockStateProvider.simple(Blocks.STONE);
    }

    @Info("The block state provider to use for placement")
    public SimpleBlockBuilder stateProvider(BlockStateProvider provider) {
        state = provider;
        return this;
    }

    @Info("The state to place")
    public SimpleBlockBuilder state(BlockState state) {
        this.state = BlockStateProvider.simple(state);
        return this;
    }

    @Override
    public Supplier<SimpleBlockFeature> feature() {
        return () -> Cast.to(Feature.SIMPLE_BLOCK);
    }

    @Override
    public SimpleBlockConfiguration createFeatureConfig() {
        return new SimpleBlockConfiguration(
                state
        );
    }
}
