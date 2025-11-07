package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class TFCSaplingBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "cross" };

    public transient Supplier<Integer> growth;
    public transient TreeGrower treeGrower;
    public transient boolean sand;

    public TFCSaplingBlockBuilder(ResourceLocation i) {
        super(i);
        growth = () -> 8;
        treeGrower = Wood.OAK.tree();
        sand = false;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Makes it so the sapling can be placed on sand")
    public TFCSaplingBlockBuilder placeableOnSand() {
        sand = true;
        return this;
    }

    @Info("Sets the number of days it takes for the sapling to grow")
    public TFCSaplingBlockBuilder growthDays(int i) {
        growth = () -> i;
        return this;
    }

    @Info("Sets the number of days, via a supplier, it takes for the sapling to grow")
    public TFCSaplingBlockBuilder growthDaysSupplier(Supplier<Integer> days) {
        growth = days;
        return this;
    }

    @Info("Sets the tree feature of the sapling")
    public TFCSaplingBlockBuilder trees(
            String name,
            float secondaryChance,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> tree,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> secondaryTree,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> megaTree,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> secondaryMegaTree,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> flowers,
            @Nullable ResourceKey<ConfiguredFeature<?, ?>> secondaryFlowers
    ) {
        treeGrower = new TreeGrower(
                name,
                secondaryChance,
                Optional.ofNullable(megaTree),
                Optional.ofNullable(secondaryMegaTree),
                Optional.ofNullable(tree),
                Optional.ofNullable(secondaryTree),
                Optional.ofNullable(flowers),
                Optional.ofNullable(secondaryFlowers)
        );
        return this;
    }

    @Info("Sets the singular tree feature of the sapling")
    public TFCSaplingBlockBuilder tree(String name, ResourceKey<ConfiguredFeature<?, ?>> tree) {
        return trees(name, 0.0F, tree, null, null, null, null, null);
    }

    @Override
    public Block createObject() {
        return new TFCSaplingBlock(treeGrower, createExtendedProperties(), growth, sand);
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(TFCBlockEntities.TICK_COUNTER);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(ModelUtil.CROSS);
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, false, m);
    }
}
