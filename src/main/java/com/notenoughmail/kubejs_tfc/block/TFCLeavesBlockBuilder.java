package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ILeafBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.FallenLeavesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TFCLeavesBlockBuilder extends ExtendedPropertiesBlockBuilder implements ILeafBuilder {

    public transient int autumnIndex;
    @Nullable
    public transient Supplier<Block> twig;
    public transient DelayedBuilder.NullCapable<FallenLeavesBlockBuilder> fallenLeaves;
    public transient boolean seasonalColors;

    public TFCLeavesBlockBuilder(ResourceLocation i) {
        super(i);
        fallenLeaves = new DelayedBuilder.NullCapable<>(r -> new FallenLeavesBlockBuilder(r, this), () -> newID("", "_fallen"));
        BuilderRefs.leafColors.add(this);
        seasonalColors = true;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("all", tex);
    }

    @Info("Sets the vertical coordinate, in the range [0, 255], on TFC's `foliage_fall` colormap for the leaves")
    public TFCLeavesBlockBuilder autumnIndex(int index) {
        autumnIndex = index;
        return this;
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    @Generics(FallenLeavesBlockBuilder.class)
    public TFCLeavesBlockBuilder fallenLeaves(@Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        return fallenLeaves(this.fallenLeaves.fallbackId(), fallenLeaves);
    }

    @Info("Sets the properties of the fallen leaves block. May be null to not have fallen leaves")
    @Generics(FallenLeavesBlockBuilder.class)
    public TFCLeavesBlockBuilder fallenLeaves(ResourceLocation id, @Nullable Consumer<FallenLeavesBlockBuilder> fallenLeaves) {
        if (fallenLeaves == null) {
            this.fallenLeaves.markNull();
        } else {
            fallenLeaves.accept(this.fallenLeaves.get(id));
        }
        return this;
    }

    @Info("Sets the twig block to be placed when a natural leaf is broken")
    public TFCLeavesBlockBuilder twig(ResourceLocation twigId) {
        twig = Lazy.of(() -> RegistryInfo.BLOCK.getValue(twigId));
        return this;
    }

    @Info("Determines if the tint of the leaves should change seasonally")
    public TFCLeavesBlockBuilder seasonalColors(boolean seasonalColors) {
        this.seasonalColors = seasonalColors;
        return this;
    }

    @Override
    public Block createObject() {
        return new TFCLeavesBlock(createExtendedProperties().randomTicks().noOcclusion(), autumnIndex, fallenLeaves.get(), twig);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent(ResourceUtils.plainModel(this));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("block/leaves");
            m.textures(textures);
        });
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        fallenLeaves.ifNotMarkedNull(b -> {
            RegistryInfo.BLOCK.addBuilder(b);
            b.createAdditionalObjects();
        });
    }

    @Override
    public boolean seasonalColors() {
        return seasonalColors;
    }

    @Override
    public int autumnIndex() {
        return autumnIndex;
    }
}
