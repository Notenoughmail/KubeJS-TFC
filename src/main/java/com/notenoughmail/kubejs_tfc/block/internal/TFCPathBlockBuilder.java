package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.soil.PathBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class TFCPathBlockBuilder extends BlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public TFCPathBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
    }

    @Override
    public Block createObject() {
        return new PathBlock(createProperties(), parent);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String base = newID("block/", "").toString();

        generator.blockModel(id, m -> {
            m.parent("tfc:block/grass_path");
            m.texture("dirt", base);
            m.texture("top", base + "_top");
            m.texture("side", base + "_side");
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();

        bs.variant("", v -> {
            v.model(model);
            v.model(model).y(90);
            v.model(model).y(180);
            v.model(model).y(270);
        });
    }
}
