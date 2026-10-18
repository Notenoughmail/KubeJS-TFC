package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cf;

import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.FissureDecorationBuilder;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.feature.FissureConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@ReturnsSelf
public class FissureBuilder extends ConfiguredFeatureBuilder.WithFeature<FissureConfig> {

    @Nullable
    public transient BlockState wallState;
    public transient BlockState fluid;
    public transient int count, radius, minPieces, maxPieces, maxPieceLength;
    public transient VerticalAnchor anchor;
    @Nullable
    public transient FissureConfig.Decoration decoration;

    public FissureBuilder(ResourceLocation id) {
        super(id, TFCFeatures.FISSURE);
        fluid = Blocks.AIR.defaultBlockState();
        anchor = VerticalAnchor.aboveBottom(16);
        count = 5;
        radius = 12;
        minPieces = 10;
        maxPieces = 24;
        maxPieceLength = 6;
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
        count = Validations.assertPositive(c, "count");
        return this;
    }

    @Info("The size of the area fissures will be placed in")
    public FissureBuilder radius(int r) {
        radius = Validations.assertPositive(r, "radius");
        return this;
    }

    @Info("The number of pieces which make up each fissure")
    public FissureBuilder pieces(Context ctx, int minCount, int maxCount) {
        maxPieces = Validations.assertPositive(maxCount, "pieces.maxCount");
        minPieces = Validations.assertNotGreaterThan(
                Validations.assertPositive(minCount, "pieces.minCount"),
                maxCount,
                "pieces.minCount",
                "pieces.maxCount",
                ctx
        );
        return this;
    }

    @Info("The max length of a fissure piece")
    public FissureBuilder maxPieceLength(int m) {
        maxPieceLength = Validations.assertPositive(m, "maxPieceLength");
        return this;
    }

    @Info("Additional decoration properties")
    public FissureBuilder decoration(Context ctx, FissureDecorationBuilder decoration) {
        this.decoration = decoration.build(ctx);
        return this;
    }

    @Info("The position of the fissure origin")
    public FissureBuilder anchor(VerticalAnchor anchor) {
        this.anchor = anchor;
        return this;
    }

    @Override
    public FissureConfig createFeatureConfiguration() {
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
        );
    }
}
