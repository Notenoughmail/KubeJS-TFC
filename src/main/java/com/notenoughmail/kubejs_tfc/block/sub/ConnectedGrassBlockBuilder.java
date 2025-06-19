package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ConnectedGrassBlockBuilder extends MultipartShapedBlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public static final List<ConnectedGrassBlockBuilder> thisList = new ArrayList<>();
    public transient BiConsumer<GrassModelPart, ModelGenerator> models;

    public ConnectedGrassBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        thisList.add(this);
        renderType("cutout_mipped");
        tagBlock(TFCTags.Blocks.GRASS.location());
        texture("texture", parent.textures.get("particle").getAsString());
        itemBuilder.texture("block", textures.get("texture").getAsString());
        models = (p, m) -> {
            m.parent(p.defaultParent);
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the grass block, accepts a `BiConsumer` of a `GrassModelPart` and a model generator.
            The generator is unique for each part.
            
            There are 5 parts: `BOTTOM`, `TOP`, `SNOWY_TOP`, `SIDE`, and `SNOWY_SIDE`. These have 4 boolean properties
            which can be used to logically determine the part currently in operation. The properties are `.bottom`,
            `.top`, `.side`, and `.snowy`.
            """)
    @Generics({ GrassModelPart.class, ModelGenerator.class })
    public ConnectedGrassBlockBuilder models(BiConsumer<GrassModelPart, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
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
    @Generics(BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Info("Makes the grass block use a unique texture for the dirt part of its texture, by default uses the texture of its parent dirt block")
    public ConnectedGrassBlockBuilder uniqueDirtTexture() {
        texture("texture", id.getNamespace() + ":block/" + id.getPath());
        return this;
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTableBasic(generator, this, parent);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent("tfc:item/grass_inv");
        m.textures(itemBuilder.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (GrassModelPart p : GrassModelPart.VALUES) {
            generator.blockModel(p.model(this), m -> models.accept(p, m));
        }
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String bottom = GrassModelPart.BOTTOM.modelEx(this);
        final String top = GrassModelPart.TOP.modelEx(this);
        final String snowyTop = GrassModelPart.SNOWY_TOP.modelEx(this);
        final String side = GrassModelPart.SIDE.modelEx(this);
        final String snowySide = GrassModelPart.SNOWY_SIDE.modelEx(this);

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
            final String dir = ResourceUtils.CARDINAL_DIRECTIONS[j].getSerializedName();
            bs.part(dir + "=true,snowy=false", p -> p.model(top).y(j * 90));
            bs.part(dir + "=true,snowy=true", p -> p.model(snowyTop).y(j * 90));
            bs.part(dir + "=false,snowy=false", p -> p.model(side).y(j * 90));
            bs.part(dir + "=false,snowy=true", p -> p.model(snowySide).y(j * 90));
        }
    }

    public enum GrassModelPart {
        BOTTOM(false, false, false, true),
        TOP(false, false, true, false),
        SNOWY_TOP(true, false, true, false),
        SIDE(false, true, false, false),
        SNOWY_SIDE(true, true, false, false);

        @HideFromJS
        public final String defaultParent;
        public final boolean snowy, side, top, bottom;

        GrassModelPart(boolean snowy, boolean side, boolean top, boolean bottom) {
            this.defaultParent = "tfc:block/grass_" + name().toLowerCase(Locale.ROOT);
            this.snowy = snowy;
            this.side = side;
            this.top = top;
            this.bottom = bottom;
        }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }

        @HideFromJS
        public String modelEx(BlockBuilder builder) {
            return builder.newID("block/", "_" + name().toLowerCase(Locale.ROOT)).toString();
        }

        public static final GrassModelPart[] VALUES = values();
    }
}
