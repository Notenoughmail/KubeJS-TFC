package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.AxleBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.BladedAxleBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

public class BladedAxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;

    public BladedAxleBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.BLADED_AXLE, this);
        ModelUtil.defaultTexture(this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(AxleBlockBuilder.TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new BladedAxleBlock(createExtendedProperties(), Cast.to(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .blockEntity(TFCBlockEntities.BLADED_AXLE);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(KubeJSTFC.tfc("block/bladed_axle"));
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", ModelUtil.TFC_EMPTY);
    }
}
