package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.ClutchBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

public class ClutchBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;

    public ClutchBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.CLUTCH, this);
        texture("overlay_end", "tfc:block/axle_casing_front");
        renderType("cutout");
    }

    @Override
    public Block createObject() {
        return new ClutchBlock(createExtendedProperties(), UtilsJS.cast(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .pushReaction(PushReaction.DESTROY)
                .blockEntity(TFCBlockEntities.CLUTCH);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("tfc:block/ore_column");
            m.textures(textures);
            m.texture("overlay", "tfc:block/axle_casing_unpowered");
        });
        generator.blockModel(newID("", "_powered"), m -> {
            m.parent("tfc:block/ore_column");
            m.textures(textures);
            m.texture("overlay", "tfc:block/axle_casing_powered");
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();
        final String powered = model + "_powered";
        bs.simpleVariant("axis=y,powered=false", model);
        bs.simpleVariant("axis=y,powered=true", powered);
        bs.variant("axis=z,powered=false", v -> v.model(model).x(90));
        bs.variant("axis=z,powered=true", v -> v.model(powered).x(90));
        bs.variant("axis=x,powered=false", v -> v.model(model).y(90).x(90));
        bs.variant("axis=x,powered=true", v -> v.model(powered).y(90).x(90));
    }
}
