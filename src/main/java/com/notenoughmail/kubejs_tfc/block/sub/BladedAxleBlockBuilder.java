package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.util.UtilsJS;
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
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BLADED_AXLE, this);
        textureAll(parent.id.getNamespace() + ":block/" + parent.id.getPath());
    }

    @Override
    public Block createObject() {
        return new BladedAxleBlock(createExtendedProperties(), UtilsJS.cast(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .blockEntity(TFCBlockEntities.BLADED_AXLE);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return texture("wood", tex);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (model.isEmpty()) {
            generator.blockModel(id, m -> {
                m.parent("tfc:block/bladed_axle");
                m.textures(textures);
            });
        } else {
            hasModel(generator);
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", "tfc:block/empty");
    }
}
