package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.custom.ShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

// Very "shaped" this is
public class RawRockBlockBuilder extends ShapedBlockBuilder {

    public transient boolean naturallySupported;
    @Nullable
    public transient Component rockTypeTooltip;
    private final String model;
    private final String mirrorModel;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        naturallySupported = false;
        rockTypeTooltip = null;
        model = id.toString();
        mirrorModel = newID("", "mirrored").toString();
    }

    public RawRockBlockBuilder naturallySupported(boolean supported) {
        naturallySupported = supported;
        return this;
    }

    public RawRockBlockBuilder rockTypeTooltip(Component comp) {
        rockTypeTooltip = comp;
        return this;
    }

    @Override
    public RawRockBlock createObject() {
        return new RawRockBlock(createProperties(), naturallySupported, rockTypeTooltip);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("block/cube_all");
            m.texture("all", model);
        });
        generator.blockModel(newID("", "_mirrored"), m -> {
            m.parent("block/cube_all");
            m.texture("all", model);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.variant("axis=x", v -> {
            v.model(model).x(90).y(90);
            v.model(mirrorModel).x(90).y(90);
        });
        bs.variant("axis=y", v -> {
            v.model(model);
            v.model(mirrorModel);
            v.model(model).y(180);
            v.model(mirrorModel).y(180);
        });
        bs.variant("axis=z", v -> {
            v.model(model).x(90);
            v.model(mirrorModel).x(90);
            v.model(model).x(90).y(180);
            v.model(mirrorModel).x(90).y(180);
        });
    }
}
