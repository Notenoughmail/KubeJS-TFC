package io.github.notenoughmail.kubejstfc.worldgen.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.FissureDecorationBuilder;
import net.dries007.tfc.world.feature.FissureConfig;
import net.dries007.tfc.world.feature.FissureFeature;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@ReturnsSelf
public class FissureBuilder extends ConfiguredFeatureBuilder<FissureFeature, FissureConfig> {

    @Nullable
    public transient BlockState wallState;
    public transient BlockState fluid;
    public transient int count, radius, minPieces, maxPieces, maxPieceLength;
    public transient VerticalAnchor anchor;
    @Nullable
    public transient FissureDecorationBuilder decoration;

    public FissureBuilder(ResourceLocation id) {
        super(id);
        fluid = Blocks.AIR.defaultBlockState();
        anchor = VerticalAnchor.aboveBottom(16);
        count = 5;
    }

    @Info("The wall state of the fissure")
    public FissureBuilder wallState(BlockState state) {
        wallState = state;
        return this;
    }

    @Info("The fluid within the fissure")
    public FissureBuilder fluid(BlockState state) {
        fluid = state;
        return this;
    }

    @Info("How many fissures to place")
    public FissureBuilder count(int c) {
        count = positive(c);
        return this;
    }

    @Info("The size of the area fissures will be placed in")
    public FissureBuilder radius(int r) {
        radius = positive(r);
        return this;
    }

    @Info("The number of pieces which make up each fissure")
    public FissureBuilder pieces(int minCount, int maxCount) {
        minPieces = positive(minCount);
        maxPieces = positive(maxCount);
        return this;
    }

    @Info("The max length of a fissure piece")
    public FissureBuilder maxPieceLength(int m) {
        maxPieceLength = positive(m);
        return this;
    }

    @Info("Additional decoration properties")
    public FissureBuilder decoration(FissureDecorationBuilder decoration) {
        this.decoration = decoration;
        return this;
    }

    @Info("Places the origin of the fissures above the bottom of the world")
    public FissureBuilder aboveBottom(int y) {
        anchor = VerticalAnchor.aboveBottom(y);
        return this;
    }

    @Info("Places the origin of the fissures below the top of the world")
    public FissureBuilder belowTop(int y) {
        anchor = VerticalAnchor.belowTop(y);
        return this;
    }

    @Info("Places the origin of the fissures at an absolute y-level")
    public FissureBuilder absolute(int y) {
        anchor = VerticalAnchor.absolute(y);
        return this;
    }

    @Override
    public Supplier<FissureFeature> feature() {
        return TFCFeatures.FISSURE;
    }

    @Override
    public FissureConfig createFeatureConfig() {
        return new FissureConfig(
                Optional.ofNullable(wallState),
                fluid,
                count,
                radius,
                anchor,
                minPieces,
                maxPieces,
                maxPieceLength,
                Optional.ofNullable(decoration)
                        .map(FissureDecorationBuilder::build)
        );
    }
}
