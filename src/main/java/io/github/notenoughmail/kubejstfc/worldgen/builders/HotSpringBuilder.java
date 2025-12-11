package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.FissureDecorationBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.Weighted;
import net.dries007.tfc.world.feature.HotSpringConfig;
import net.dries007.tfc.world.feature.HotSpringFeature;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ReturnsSelf
public class HotSpringBuilder extends ConfiguredFeatureBuilder<HotSpringFeature, HotSpringConfig> {

    @Nullable
    public transient BlockState wallState;
    public transient BlockState fluidState;
    public transient int radius;
    @Nullable
    public transient FissureDecorationBuilder decoration;
    public transient boolean allowUnderwater;
    @Nullable
    public transient Map<Block, List<Weighted<BlockState>>> replaceOnFluidContact;

    public HotSpringBuilder(ResourceLocation id) {
        super(id);
        fluidState = Blocks.AIR.defaultBlockState();
        radius = 14;
    }

    @Info("The walls of the hot spring")
    public HotSpringBuilder wallState(BlockState state) {
        wallState = state;
        return this;
    }

    @Info("The fluid to fill the hot spring with")
    public HotSpringBuilder fluidState(BlockState state) {
        fluidState = state;
        return this;
    }

    @Info("The radius, in the range [1, 16], of the hot spring")
    public HotSpringBuilder radius(int r) {
        radius = clamp(r, 1, 16);
        return this;
    }

    @Info("Additional decoration properties")
    public HotSpringBuilder decoration(FissureDecorationBuilder decoration) {
        this.decoration = decoration;
        return this;
    }

    @Info("If the hot spring can generate underwater")
    public HotSpringBuilder allowedUnderwater(boolean a) {
        allowUnderwater = a;
        return this;
    }

    @Info("Block to replace, and what to replace them with, when contacting fluid")
    public HotSpringBuilder replaceOnFluidContact(Map<Block, List<Weighted<BlockState>>> r) {
        replaceOnFluidContact = r;
        return this;
    }

    @Override
    public Supplier<HotSpringFeature> feature() {
        return TFCFeatures.HOT_SPRING;
    }

    @Override
    public HotSpringConfig createFeatureConfig() {
        return new HotSpringConfig(
                opt(wallState),
                fluidState,
                radius,
                opt(decoration)
                        .map(FissureDecorationBuilder::build),
                allowUnderwater,
                opt(replaceOnFluidContact)
                        .map(Weighted::toTFC)
        );
    }
}
