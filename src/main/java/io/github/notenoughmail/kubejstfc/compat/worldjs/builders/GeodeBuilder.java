package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.TFCGeodeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@ReturnsSelf
public class GeodeBuilder extends ConfiguredFeatureBuilder.WithFeature<TFCGeodeConfig> {

    public transient BlockState outer, middle;
    public transient SimpleWeightedRandomList<BlockState> inner;

    public GeodeBuilder(ResourceLocation id) {
        super(id, TFCFeatures.GEODE);
        outer = middle = Blocks.STONE.defaultBlockState();
        inner = SimpleWeightedRandomList.empty();
    }

    @Override
    protected TFCGeodeConfig createFeatureConfiguration() {
        return new TFCGeodeConfig(
                outer,
                middle,
                inner
        );
    }

    @Info("Set the block of outer layer of the geode")
    public GeodeBuilder outer(BlockState state) {
        outer = state;
        return this;
    }

    @Info("Set the middle layer of the geode")
    public GeodeBuilder middle(BlockState state) {
        middle = state;
        return this;
    }

    @Info("Set the inner layer of the geode")
    public GeodeBuilder inner(List<WeightedValue<BlockState>> states) {
        inner = WeightedValue.toVanilla(states);
        return this;
    }
}
