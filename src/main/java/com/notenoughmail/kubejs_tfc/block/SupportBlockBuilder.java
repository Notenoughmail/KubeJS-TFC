package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.HorizontalSupportBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.internal.StandingAndWallBlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.VerticalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SupportBlockBuilder extends ExtendedPropertiesMultipartShapedBlockBuilder {

    public transient final HorizontalSupportBlockBuilder horizontal;
    public transient final String connection;

    public SupportBlockBuilder(ResourceLocation i) {
        super(i);
        horizontal = new HorizontalSupportBlockBuilder(newID("", "_horizontal"), this);
        itemBuilder = new StandingAndWallBlockItemBuilder(id, this, horizontal);
        connection = newID("block/", "_connection").toString();
        tag(TFCTags.Blocks.SUPPORT_BEAM.location());
    }

    @Info(value = "Sets the properties of the horizontal support block")
    @Generics(HorizontalSupportBlockBuilder.class)
    public SupportBlockBuilder horizontal(Consumer<HorizontalSupportBlockBuilder> horizontalSupport) {
        horizontalSupport.accept(horizontal);
        return this;
    }

    @Override
    public Block createObject() {
        return new VerticalSupportBlock(createExtendedProperties());
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(horizontal);
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        bs.part("", newID("block/", "_vertical").toString());
        bs.part("north=true", p -> p.model(connection).y(270));
        bs.part("east=true", connection);
        bs.part("south=true", p -> p.model(connection).y(90));
        bs.part("west=true", p -> p.model(connection).y(180));
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("tfc:block/wood/support/inventory");
            m.texture("texture", newID("block/", "").toString());
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(newID("", "_vertical"), m -> {
            if (model.isEmpty()) {
                m.parent("tfc:block/wood/support/vertical");
                m.textures(textures);
            } else {
                m.parent(model);
            }
        });
        generator.blockModel(newID("", "_connection"), m -> {
            m.parent("tfc:block/wood/support/connection");
            m.textures(textures);
        });
    }
}
