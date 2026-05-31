package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.ThinSpikeBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.cave.ThinSpikeConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

@ReturnsSelf
public class ThinSpikeBuilder extends ConfiguredFeatureBuilder.WithFeature<ThinSpikeConfig> {

    public transient BlockState state;
    public transient int radius, tries, minHeight, maxHeight;
    boolean allowedUnderwater;

    public ThinSpikeBuilder(ResourceLocation id) {
        super(id, TFCFeatures.THIN_SPIKE);
        radius = tries = minHeight = 1;
        maxHeight = 10;
        state = TFCBlocks.ICICLE.holder().get().defaultBlockState();
    }

    @Info("Set the state to place")
    public ThinSpikeBuilder state(BlockState state) {
        if (!state.hasProperty(ThinSpikeBlock.TIP)) {
            throw exception("Block must have tip property")
                    .customData("required property", ThinSpikeBlock.TIP)
                    .customData("given block(state)", state);
        }
        this.state = state;
        return this;
    }

    @Info("Set the radius of spike placement")
    public ThinSpikeBuilder radius(int r) {
        radius = r;
        return this;
    }

    @Info("How many times to attempt to place spikes")
    public ThinSpikeBuilder tries(int t) {
        tries = t;
        return this;
    }

    @Info("The minimum and maximum heights of individual spikes")
    public ThinSpikeBuilder height(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }

    @Info("If the spike should be allowed to be placed in water")
    public ThinSpikeBuilder allowedUnderwater(boolean b) {
        allowedUnderwater = b;
        return this;
    }

    @Override
    protected ThinSpikeConfig createFeatureConfiguration() {
        return new ThinSpikeConfig(
                state,
                radius,
                tries,
                minHeight,
                maxHeight,
                allowedUnderwater
        );
    }
}
