package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.compat.worldjs.WorldgenPlugin;
import io.github.notenoughmail.kubejstfc.compat.worldjs.support.ClimatePlacementBuilder;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.worldjs.builders.base.PlacedFeatureBuilder;
import io.github.notenoughmail.worldjs.util.WeightedValue;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.wood.FallenLeavesBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.ForestConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@ReturnsSelf
public class ForestEntryBuilder extends ConfiguredFeatureBuilder.WithFeature<ForestConfig.Entry> {

    public transient ClimatePlacementBuilder climate;
    @Nullable
    public transient BlockState bushLog, bushLeaves, fallenLog, fallenLeaves;
    @Nullable
    public transient List<WeightedValue<BlockState>> groundcover;
    public transient Holder.Reference<ConfiguredFeature<?, ?>> normalTree, deadTree;
    @Nullable
    public transient Holder.Reference<ConfiguredFeature<?, ?>> oldGrowthTree, krummholz, soilDisc;
    public transient int oldGrowthChance, spoilerOldGrowthChance, fallenChance, deadChance;
    public transient boolean floating;

    public ForestEntryBuilder(ResourceLocation id) {
        super(id, TFCFeatures.FOREST_ENTRY);
        climate = ClimatePlacementBuilder.DEFAULT;
        oldGrowthChance = 6;
        spoilerOldGrowthChance = 200;
        fallenChance = 14;
        deadChance = 75;
    }

    @Info("The climate conditions this forest entry can spawn in")
    public ForestEntryBuilder climate(ClimatePlacementBuilder placement) {
        placement.verify("climate", this::exception);
        climate = placement;
        return  this;
    }

    @Info("The bush blocks")
    public ForestEntryBuilder bush(BlockState log, BlockState leaves) {
        bushLog = log;
        bushLeaves = leaves;
        return this;
    }

    @Info("The fallen tree blocks")
    public ForestEntryBuilder fallen(BlockState log, BlockState leaves) {
        if (!(log.hasProperty(TFCBlockStateProperties.NATURAL) || log.hasProperty(BlockStateProperties.AXIS))) {
            throw exception("Fallen log block must have axis and natural properties")
                    .customData("axis property", BlockStateProperties.AXIS)
                    .customData("natural property", TFCBlockStateProperties.NATURAL);
        }
        if (!leaves.hasProperty(FallenLeavesBlock.LAYERS)) {
            throw exception("Fallen leaves block must have layers property")
                    .customData("layer property", FallenLeavesBlock.LAYERS);
        }
        fallenLog = log;
        fallenLeaves = leaves;
        return this;
    }

    @Info("The groundcover blocks")
    public ForestEntryBuilder groundcover(List<WeightedValue<BlockState>> cover) {
        groundcover = cover;
        return this;
    }

    @Info("The tree features to place")
    public ForestEntryBuilder trees(Holder.Reference<ConfiguredFeature<?, ?>> normal, Holder.Reference<ConfiguredFeature<?, ?>> dead) {
        normalTree = normal;
        deadTree = dead;
        return this;
    }

    @Info("The old growth tree feature")
    public ForestEntryBuilder oldGrowthTree(Holder.Reference<ConfiguredFeature<? ,?>> old) {
        oldGrowthTree = old;
        return this;
    }

    @Info("The krummholz feature")
    public ForestEntryBuilder krummholz(Holder.Reference<ConfiguredFeature<? ,?>> k) {
        krummholz = k;
        return this;
    }

    @Info("The soil disc feature")
    public ForestEntryBuilder soilDisc(Holder.Reference<ConfiguredFeature<? ,?>> s) {
        soilDisc = s;
        return this;
    }

    @Info("The chance, 1/C, that any given tree will instead be an old growth tree")
    public ForestEntryBuilder oldGrowthChance(int chance) {
        oldGrowthChance = assertPositive(chance, "oldGrowthChance");
        return this;
    }

    @Info("The extra chance, 1/C, that any given tree will instead be an old growth tree")
    public ForestEntryBuilder spoilerOldGrowthChance(int chance) {
        spoilerOldGrowthChance = assertPositive(chance, "chance");
        return this;
    }

    @Info("The chance, 1/C, that any given tree will instead be a fallen tree")
    public ForestEntryBuilder fallenChance(int chance) {
        fallenChance = chance;
        return this;
    }

    @Info("The chance, 1/C, that any given tree will instead be a dead tree")
    public ForestEntryBuilder deadChance(int chance) {
        deadChance = chance;
        return this;
    }

    @Info("If trees are allowed to float when placing")
    public ForestEntryBuilder floating(boolean f) {
        floating = f;
        return this;
    }

    @Override
    protected ConfiguredFeatureBuilder<ForestConfig.Entry> placement(Context ctx, ResourceLocation id, Consumer<PlacedFeatureBuilder> builder) {
        throw exception("Forest entries cannot have placements!");
    }

    @Override
    public ForestConfig.Entry createFeatureConfiguration() {
        return new ForestConfig.Entry(
                climate.build(),
                Optional.ofNullable(bushLog),
                Optional.ofNullable(bushLeaves),
                Optional.ofNullable(fallenLog),
                Optional.ofNullable(fallenLeaves),
                Optional.ofNullable(groundcover)
                        .map(WorldgenPlugin::weightedTFC),
                notNull(normalTree, "normalTree"),
                notNull(deadTree, "deadTree"),
                Optional.ofNullable(oldGrowthTree),
                Optional.ofNullable(krummholz),
                Optional.ofNullable(soilDisc),
                oldGrowthChance,
                spoilerOldGrowthChance,
                fallenChance,
                deadChance,
                floating
        );
    }
}
