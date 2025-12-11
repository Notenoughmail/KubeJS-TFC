package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.PlacedFeatureBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.ClimatePlacementBuilder;
import io.github.notenoughmail.kubejstfc.worldgen.support.Weighted;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.wood.FallenLeavesBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.ForestConfig;
import net.dries007.tfc.world.feature.tree.ForestFeature;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ReturnsSelf
public class ForestEntryBuilder extends ConfiguredFeatureBuilder<ForestFeature.Entry, ForestConfig.Entry> {

    public transient Consumer<ClimatePlacementBuilder> climate;
    @Nullable
    public transient BlockState bushLog, bushLeaves, fallenLog, fallenLeaves;
    @Nullable
    public transient List<Weighted<BlockState>> groundcover;
    public transient Holder<ConfiguredFeature<?, ?>> normalTree, deadTree;
    @Nullable
    public transient Holder<ConfiguredFeature<?, ?>> oldGrowthTree, krummholz, soilDisc;
    public transient int oldGrowthChance, spoilerOldGrowthChance, fallenChance, deadChance;
    public transient boolean floating;

    public ForestEntryBuilder(ResourceLocation id) {
        super(id);
        climate = c -> {};
        normalTree = configured(KubeJSTFC.tfc("tree/oak"));
        deadTree = configured(KubeJSTFC.tfc("tree/oak_dead"));
        oldGrowthChance = 6;
        spoilerOldGrowthChance = 200;
        fallenChance = 14;
        deadChance = 75;
    }

    @Info("The climate conditions this tree entry can spawn in")
    public ForestEntryBuilder climate(Consumer<ClimatePlacementBuilder> c) {
        climate = c;
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
    public ForestEntryBuilder groundcover(List<Weighted<BlockState>> g) {
        groundcover = g;
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
        oldGrowthChance = positive(chance);
        return this;
    }

    @Info("The extra chance, 1/C, that any given tree will instead be an old growth tree")
    public ForestEntryBuilder spoilerOldGrowthChance(int chance) {
        spoilerOldGrowthChance = positive(chance);
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

    @Info("DO NOT USE! Forest entries cannot be placed!")
    @Override
    public ForestEntryBuilder withPlacement(Consumer<PlacedFeatureBuilder> builder) {
        throw exception("Forest entries cannot have placements!");
    }

    @Override
    public Supplier<ForestFeature.Entry> feature() {
        return TFCFeatures.FOREST_ENTRY;
    }

    @Override
    public ForestConfig.Entry createFeatureConfig() {
        return new ForestConfig.Entry(
                ClimatePlacementBuilder.make(climate),
                opt(bushLog),
                opt(bushLeaves),
                opt(fallenLog),
                opt(fallenLeaves),
                opt(groundcover)
                        .map(Weighted::toTFC),
                normalTree,
                deadTree,
                opt(oldGrowthTree),
                opt(krummholz),
                opt(soilDisc),
                oldGrowthChance,
                spoilerOldGrowthChance,
                fallenChance,
                deadChance,
                floating
        );
    }
}
