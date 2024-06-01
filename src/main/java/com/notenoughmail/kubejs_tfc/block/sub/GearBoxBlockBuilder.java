package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.GearBoxBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class GearBoxBlockBuilder extends ExtendedPropertiesMultipartShapedBlockBuilder {

    public transient final AxleBlockBuilder parent;

    public GearBoxBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.GEAR_BOX, this);
        texture("all", id.getNamespace() + ":block/" + id.getPath());
        renderType("cutout");
    }

    @Override
    public Block createObject() {
        return new GearBoxBlock(createExtendedProperties(), UtilsJS.cast(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.GEAR_BOX);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("tfc:block/ore");
            m.textures(textures);
            m.texture("overlay", "tfc:block/axle_casing_front");
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(newID("", "_port"), m -> {
            m.parent("tfc:block/gear_box_port");
            m.textures(textures);
            m.texture("overlay", "tfc:block/axle_casing_front");
        });
        generator.blockModel(newID("", "_face"), m -> {
            m.parent("tfc:block/gear_box_face");
            m.textures(textures);
            m.texture("overlay", "tfc:block/axle_casing_round");
        });
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String port = newID("block/", "_port").toString();
        final String face = newID("block/", "_face").toString();
        bs.part("north=true", port);
        bs.part("north=false", face);
        bs.part("south=true", v -> v.model(port).y(180));
        bs.part("south=false", v -> v.model(face).y(180));
        bs.part("east=true", v -> v.model(port).y(90));
        bs.part("east=false", v -> v.model(face).y(90));
        bs.part("west=true", v -> v.model(port).y(270));
        bs.part("west=false", v -> v.model(face).y(270));
        bs.part("down=true", v -> v.model(port).x(90));
        bs.part("down=false", v -> v.model(face).x(90));
        bs.part("up=true", v -> v.model(port).x(270));
        bs.part("up=false", v -> v.model(face).x(270));
    }
}
