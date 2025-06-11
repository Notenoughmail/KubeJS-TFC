package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.soil.PathBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TFCPathBlockBuilder extends BlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public TFCPathBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        texture("top", id.getNamespace() + ":block/" + id.getPath() + "_top");
        texture("side", id.getNamespace() + ":block/" + id.getPath() + "_side");
        texture("dirt", parent.textures.get("particle").getAsString());
    }

    @Info("Makes the path block use a unique texture for the dirt part of its texture, by default uses the texture of its parent dirt block")
    public TFCPathBlockBuilder uniqueDirtTexture() {
        texture("dirt", id.getNamespace() + ":block/" + id.getPath());
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("top", tex);
        texture("side", tex);
        texture("dirt", tex);
        return this;
    }

    @Override
    public Block createObject() {
        return new PathBlock(createProperties(), parent);
    }

    @Override
    @Generics(BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTableBasic(generator, this, parent);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
           m.parent("tfc:block/grass_path");
           m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String m = ResourceUtils.plainModel(this);
        bs.variant("", v -> {
            v.model(m);
            v.model(m).y(90);
            v.model(m).y(180);
            v.model(m).y(270);
        });
    }
}
