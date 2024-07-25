package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.EncasedAxleBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class EncasedAxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public EncasedAxleBlockBuilder(ResourceLocation i) {
        super(i);
        texture("overlay", "tfc:block/axle_casing");
        texture("overlay_end", "tfc:block/axle_casing_front");
        renderType("cutout");
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("side", tex);
        texture("end", tex);
        return super.textureAll(tex);
    }

    @Override
    public Block createObject() {
        return new EncasedAxleBlock(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.ENCASED_AXLE);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("tfc:block/ore_column");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();
        bs.simpleVariant("axis=y", model);
        bs.variant("axis=z", v -> v.model(model).x(90));
        bs.variant("axis=x", v -> v.model(model).x(90).y(90));
    }
}
