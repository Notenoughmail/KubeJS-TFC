package io.github.notenoughmail.kubejstfc.worldgen.builders.vein;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.IndicatorBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.Weighted;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.world.feature.vein.IVeinConfig;
import net.dries007.tfc.world.feature.vein.VeinConfig;
import net.dries007.tfc.world.feature.vein.VeinFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@ReturnsSelf
public abstract class VeinBuilder<V extends IVeinConfig, VF extends VeinFeature<V, ?>> extends ConfiguredFeatureBuilder<VF, V> {

    public transient Map<Block, IWeighted<BlockState>> states;
    @Nullable
    public transient IndicatorBuilder indicator;
    public transient int rarity, minY, maxY;
    public transient float density;
    public transient boolean projectToSurface, projectOffset, nearLava;
    public transient OptionalLong seed;

    public VeinBuilder(ResourceLocation id) {
        super(id);
        states = Map.of();
        rarity = 1;
        density = 1F;
        minY = -64;
        maxY = 320;
        projectToSurface = true;
        projectOffset = true;
        seed = OptionalLong.empty();
    }

    @Info("The states to place")
    public VeinBuilder<V, VF> replacementStates(Map<Block, List<Weighted<BlockState>>> states) {
        this.states = Weighted.toTFC(states);
        return this;
    }

    @Info("How often the vein, will be approximately 1/R chunks")
    public VeinBuilder<V, VF> rarity(int r) {
        rarity = r;
        return this;
    }

    @Info("Within a vein, how much of the base rock should be replaced with ore, in the range [0, 1]")
    public VeinBuilder<V, VF> density(float d) {
        density = unit(d);
        return this;
    }

    @Info("The lowest y-level the vein can spawn at")
    public VeinBuilder<V, VF> minY(int y) {
        minY = y;
        return this;
    }

    @Info("The highest y-level the vein can spawn at")
    public VeinBuilder<V, VF> maxY(int y) {
        maxY = y;
        return this;
    }

    @Info("If during placement, the vein should project to the surface")
    public VeinBuilder<V, VF> projectToSurface(boolean p) {
        projectToSurface = p;
        return this;
    }

    @Info("If when projecting, there should be an offset applied")
    public VeinBuilder<V, VF> projectOffset(boolean p) {
        projectOffset = p;
        return this;
    }

    @Info("If the vein should only spawn near lava")
    public VeinBuilder<V, VF> nearLava(boolean n) {
        nearLava = n;
        return this;
    }

    @Info("The properties for this vein's indicators")
    public VeinBuilder<V, VF> indicator(IndicatorBuilder indicator) {
        this.indicator = indicator;
        return this;
    }

    @Info("The seed of the vein")
    public VeinBuilder<V, VF> seed(long seed) {
        this.seed = OptionalLong.of(seed);
        return this;
    }

    @HideFromJS
    protected VeinConfig baseConfig() {
        return new VeinConfig(
                states,
                Optional.ofNullable(indicator)
                        .map(IndicatorBuilder::build),
                rarity,
                density,
                minY,
                maxY,
                projectToSurface,
                projectOffset,
                seed.orElse(new XoroshiroRandomSource(RandomSupport.seedFromHashOf(id.toString())).nextLong()),
                nearLava
        );
    }
}
