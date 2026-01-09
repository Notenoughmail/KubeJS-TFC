package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.TFCDirtBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@ReturnsSelf
@SuppressWarnings("unused")
public class ConnectedGrassBlockBuilder extends BlockBuilder {

    private static final ResourceLocation GRASS_INV = KubeJSTFC.tfc("item/grass_inv");

    public transient final TFCDirtBlockBuilder parent;

    public transient BiConsumer<GrassModelPart, ModelGenerator> models;

    public ConnectedGrassBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        renderType(BlockRenderType.CUTOUT_MIPPED);
        Assistant.singleTag(this, TFCTags.Blocks.GRASS);
        models = (p, m) -> {
            m.parent(p.defaultParent);
            m.textures(textures);
            if (p.bottom) {
                m.texture("texture", parent.baseTexture);
            }
        };
        BuilderRefs.grassBlockColor.add(this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        textures.put("texture", tex);
        if (itemBuilder != null) {
            itemBuilder.textures.put("block", tex);
        }
        return this;
    }

    @Info("""
            Sets the model generation of the grass block, accepts a `BiConsumer` of a `GrassModelPart` and a model generator.
            The generator is unique for each part.
            
            There are five parts: `BOTTOM`, `TOP`, `SNOWY_TOP`, `SIDE`, and `SNOWY_SIDE`. These have four boolean properties
            which can be used determine the model currently being generated. The properties are `.bottom`, `.top`,
            `.side`, and `.snowy`.
            """)
    public ConnectedGrassBlockBuilder models(BiConsumer<GrassModelPart, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return new ConnectedGrassBlock(createProperties().randomTicks(), parent, parent.path, parent.farmland);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        if (parent.itemBuilder != null) {
            return LootUtil.fallback(parent.itemBuilder::get, this);
        } else {
            return LootUtil.skipIfEmpty(drops);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (GrassModelPart p : GrassModelPart.VALUES) {
            generator.blockModel(p.model(this), m -> models.accept(p, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> {
            g.parent(GRASS_INV);
            g.textures(itemBuilder.textures);
        });
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        final ResourceLocation bottom = GrassModelPart.BOTTOM.modelEx(this);
        final ResourceLocation top = GrassModelPart.TOP.modelEx(this);
        final ResourceLocation snowyTop = GrassModelPart.SNOWY_TOP.modelEx(this);
        final ResourceLocation side = GrassModelPart.SIDE.modelEx(this);
        final ResourceLocation snowySide = GrassModelPart.SNOWY_SIDE.modelEx(this);

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
            final String dir = Assistant.COMPASS_DIRECTIONS[j].getSerializedName();
            bs.part(dir + "=true,snowy=false", p -> p.model(top).y(j * 90));
            bs.part(dir + "=true,snowy=true", p -> p.model(snowyTop).y(j * 90));
            bs.part(dir + "=false,snowy=false", p -> p.model(side).y(j * 90));
            bs.part(dir + "=false,snowy=true", p -> p.model(snowySide).y(j * 90));
        }
    }

    public enum GrassModelPart implements ISupplyModels {
        BOTTOM(false, false, false, true),
        TOP(false, false, true, false),
        SNOWY_TOP(true, false, true, false),
        SIDE(false, true, false, false),
        SNOWY_SIDE(true, true, false, false);

        @HideFromJS
        public final ResourceLocation defaultParent;
        private final String str;
        public final boolean snowy, side, top, bottom;

        GrassModelPart(boolean snowy, boolean side, boolean top, boolean bottom) {
            str = makeStr();
            this.defaultParent = KubeJSTFC.tfc("block/grass_" + str);
            this.snowy = snowy;
            this.side = side;
            this.top = top;
            this.bottom = bottom;
        }

        public static final GrassModelPart[] VALUES = values();

        @Override
        public String str() {
            return str;
        }
    }
}
