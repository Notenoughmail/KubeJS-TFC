package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
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
        texture("wood", id.getNamespace() + ":block/" + id.getPath());
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
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("tfc:block/bladed_axle");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("", "tfc:block/empty");
    }
}
