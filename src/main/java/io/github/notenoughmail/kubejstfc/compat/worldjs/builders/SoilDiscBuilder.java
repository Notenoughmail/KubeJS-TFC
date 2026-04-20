package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.SoilDiscConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

@ReturnsSelf
public class SoilDiscBuilder extends ConfiguredFeatureBuilder.WithFeature<SoilDiscConfig> {

    public transient Map<Block, BlockState> states;
    public transient int minRadius, maxRadius, height;
    public transient float integrity;

    public SoilDiscBuilder(ResourceLocation id) {
        super(id, TFCFeatures.SOIL_DISC);
        states = Map.of();
        minRadius = 3;
        maxRadius = 5;
        height = 2;
        integrity = 1F;
    }

    @Info("The replacement states of the soil disc")
    public SoilDiscBuilder replacementStates(Map<Block, BlockState> states) {
        this.states = states;
        return this;
    }

    @Info("The radius of the disc")
    public SoilDiscBuilder radius(int min, int max) {
        minRadius = min;
        maxRadius = max;
        return this;
    }

    @Info("The height of the disc")
    public SoilDiscBuilder height(int h) {
        height = assertPositive(h, "height");
        return this;
    }

    @Info("The integrity, in the range [0, 1], of the disc")
    public SoilDiscBuilder integrity(float i) {
        integrity = assertUnit(i, "integrity");
        return this;
    }

    @Override
    public SoilDiscConfig createFeatureConfiguration() {
        return new SoilDiscConfig(
                states,
                minRadius,
                maxRadius,
                height,
                integrity
        );
    }
}
