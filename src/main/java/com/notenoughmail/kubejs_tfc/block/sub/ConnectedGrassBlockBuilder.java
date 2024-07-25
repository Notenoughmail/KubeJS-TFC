package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ConnectedGrassBlockBuilder extends MultipartShapedBlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public static final List<ConnectedGrassBlockBuilder> thisList = new ArrayList<>();

    public ConnectedGrassBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        thisList.add(this);
        renderType("cutout_mipped");
        tagBlock(TFCTags.Blocks.GRASS.location());
        texture("texture", parent.textures.get("particle").getAsString());
        itemBuilder.texture("block", textures.get("texture").getAsString());
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        texture("texture", tex);
        if (itemBuilder != null) {
            itemBuilder.texture("block", tex);
        }
        return this;
    }

    @Override
    public Block createObject() {
        return new ConnectedGrassBlock(createProperties().randomTicks(), parent, parent.path, parent.farmland);
    }

    @Override
    @Generics(value = BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Info(value = "Makes the grass block use a unique texture for the dirt part of its texture, by default uses the texture of its parent dirt block")
    public ConnectedGrassBlockBuilder uniqueDirtTexture() {
        texture("texture", id.getNamespace() + ":block/" + id.getPath());
        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(parent.get()));
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent("tfc:item/grass_inv");
        m.textures(itemBuilder.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(newID("", "_bottom"), m -> {
            m.parent("tfc:block/grass_bottom");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_side"), m -> {
            m.parent("tfc:block/grass_side");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_snowy_side"), m -> {
            m.parent("tfc:block/grass_snowy_side");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_snowy_top"), m -> {
            m.parent("tfc:block/grass_snowy_top");
            m.textures(textures);
        });
        generator.blockModel(newID("", "_top"), m -> {
            m.parent("tfc:block/grass_top");
            m.textures(textures);
        });
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String bottom = newID("block/", "_bottom").toString();
        final String top = newID("block/", "_top").toString();
        final String snowyTop = newID("block/", "_snowy_top").toString();
        final String side = newID("block/", "_side").toString();
        final String snowySide = newID("block/", "_snowy_side").toString();

        bs.part("", p -> p.model(bottom).x(90));
        bs.part("snowy=false", p -> {
            p.model(top).x(270);
            p.model(top).x(270).y(90);
            p.model(top).x(270).y(180);
            p.model(top).x(270).y(270);
        });
        bs.part("snowy=true", p -> {
            p.model(snowyTop).x(270);
            p.model(snowyTop).x(270).y(90);
            p.model(snowyTop).x(270).y(180);
            p.model(snowyTop).x(270).y(270);
        });

        for (int i = 0 ; i < 4 ; i++) {
            final int j = i;
            final String dir = ModelUtils.cardinalDirections[j];
            bs.part(dir + "=true,snowy=false", p -> p.model(top).y(j * 90));
            bs.part(dir + "=true,snowy=true", p -> p.model(snowyTop).y(j * 90));
            bs.part(dir + "=false,snowy=false", p -> p.model(side).y(j * 90));
            bs.part(dir + "=false,snowy=true", p -> p.model(snowySide).y(j * 90));
        }
    }
}
