package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.Weighted;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.TFCGeodeConfig;
import net.dries007.tfc.world.feature.TFCGeodeFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

@ReturnsSelf
public class GeodeBuilder extends ConfiguredFeatureBuilder<TFCGeodeFeature, TFCGeodeConfig> {

    public transient BlockState outer, middle;
    public transient SimpleWeightedRandomList<BlockState> inner;

    public GeodeBuilder(ResourceLocation id) {
        super(id);
        outer = middle = Blocks.STONE.defaultBlockState();
        inner = SimpleWeightedRandomList.empty();
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
    public GeodeBuilder inner(List<Weighted<BlockState>> states) {
        inner = Weighted.toVanilla(states);
        return this;
    }

    @Override
    public Supplier<TFCGeodeFeature> feature() {
        return TFCFeatures.GEODE;
    }

    @Override
    public TFCGeodeConfig createFeatureConfig() {
        return new TFCGeodeConfig(
                outer,
                middle,
                inner
        );
    }
}
