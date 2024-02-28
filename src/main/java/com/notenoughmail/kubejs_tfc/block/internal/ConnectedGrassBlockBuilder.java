package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ConnectedGrassBlockBuilder extends MultipartShapedBlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public static final List<ConnectedGrassBlockBuilder> thisList = new ArrayList<>();

    public ConnectedGrassBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        thisList.add(this);
        renderType("cutout_mipped");
    }

    @Override
    public Block createObject() {
        return new ConnectedGrassBlock(createProperties().randomTicks(), parent, parent.path, parent.farmland);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent("tfc:item/grass_inv");
        m.texture("block", newID("block/", "").toString());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String baseTex = newID("block/", "").toString();

        generator.blockModel(newID("", "_bottom"), m -> {
            m.parent("tfc:block/grass_bottom");
            m.texture("texture", baseTex);
        });
        generator.blockModel(newID("", "_side"), m -> {
            m.parent("tfc:block/grass_side");
            m.texture("texture", baseTex);
        });
        generator.blockModel(newID("", "_snowy_side"), m -> {
            m.parent("tfc:block/grass_snowy_side");
            m.texture("texture", baseTex);
        });
        generator.blockModel(newID("", "_snowy_top"), m -> {
            m.parent("tfc:block/grass_snowy_top");
            m.texture("texture", baseTex);
        });
        generator.blockModel(newID("", "_top"), m -> {
            m.parent("tfc:block/grass_top");
            m.texture("texture", baseTex);
        });
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        super.generateAssetJsons(generator);
    }

    // This just doesn't work correctly. TODO: 1.1.0 | Fix
    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String bottom = newID("block/", "_bottom").toString();
        final String top = newID("block/", "_top").toString();
        final String snowyTop = newID("block/", "_snowy_top").toString();
        final String side = newID("block/", "_side").toString();
        final String snowySide = newID("block/", "_snowy_side").toString();

        bs.part("", p -> p.model(bottom).x(90));
        bs.part("snowy=false", p -> {
            p.model(top).x(270).y(90);
            p.model(top).x(270);
            p.model(top).x(270).y(180);
            p.model(top).x(270).y(270);
        });
        bs.part("snowy=true", p -> {
            p.model(snowyTop).x(270).y(90);
            p.model(snowyTop).x(270);
            p.model(snowyTop).x(270).y(180);
            p.model(snowyTop).x(270).y(270);
        });

        bs.part("north=true,snowy=false", top);
        bs.part("east=true,snowy=false", p -> p.model(top).y(90));
        bs.part("south=true,snowy=false", p -> p.model(top).y(180));
        bs.part("west=true,snowy=false", p -> p.model(top).y(270));
        bs.part("north=true,snowy=true", snowyTop);
        bs.part("east=true,snowy=true", p -> p.model(snowyTop).y(90));
        bs.part("south=true,snowy=true", p -> p.model(snowyTop).y(180));
        bs.part("west=true,snowy=true", p -> p.model(snowyTop).y(270));

        bs.part("north=false,snowy=false", side);
        bs.part("east=false,snowy=false", p -> p.model(side).y(90));
        bs.part("south=false,snowy=false", p -> p.model(side).y(180));
        bs.part("west=false,snowy=false", p -> p.model(side).y(270));
        bs.part("north=false,snowy=true", snowySide);
        bs.part("east=false,snowy=true", p -> p.model(snowySide).y(90));
        bs.part("south=false,snowy=false", p -> p.model(snowySide).y(180));
        bs.part("west=false,snowy=false", p -> p.model(snowySide).y(270));
    }
}
