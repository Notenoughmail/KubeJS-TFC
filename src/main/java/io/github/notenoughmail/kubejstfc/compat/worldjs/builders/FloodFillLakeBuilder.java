package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.FloodFillLakeConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

@ReturnsSelf
public class FloodFillLakeBuilder extends ConfiguredFeatureBuilder.WithFeature<FloodFillLakeConfig> {

    public transient BlockState state;
    public transient List<Fluid> replaceFluids;
    public transient boolean overfill;

    public FloodFillLakeBuilder(ResourceLocation id) {
        super(id, TFCFeatures.FLOOD_FILL_LAKE);
        state = Blocks.WATER.defaultBlockState();
        replaceFluids = List.of();
    }

    @Info("The state to fill the lake with")
    public FloodFillLakeBuilder fill(BlockState state) {
        this.state = state;
        return this;
    }

    @Info("The fluids to replace when encountered by the filling algorithm")
    public FloodFillLakeBuilder replaceFluids(List<Fluid> fluids) {
        replaceFluids = fluids;
        return this;
    }

    @Info("If the lake should 'overfill'")
    public FloodFillLakeBuilder allowOverfill() {
        overfill = true;
        return this;
    }

    @Override
    public FloodFillLakeConfig createFeatureConfiguration() {
        return new FloodFillLakeConfig(
                state,
                replaceFluids,
                overfill
        );
    }
}
