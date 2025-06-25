package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.HorizontalSupportBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.internal.StandingAndWallBlockItemBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
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
    public transient String connection;
    public transient boolean defaultConnection;

    public SupportBlockBuilder(ResourceLocation i) {
        super(i);
        horizontal = new HorizontalSupportBlockBuilder(newID("", "_horizontal"), this);
        itemBuilder = new StandingAndWallBlockItemBuilder(id, this, horizontal);
        connection = newID("block/", "_connection").toString();
        defaultConnection = true;
        tag(TFCTags.Blocks.SUPPORT_BEAM.location());
        horizontal.textureAll(id.getNamespace() + ":block/" + id.getPath());
    }

    @Info("Sets the model used by this and the horizontal block for sideways connections")
    public SupportBlockBuilder connectionModel(String model) {
        this.connection = model;
        defaultConnection = false;
        return this;
    }

    @Info("Sets the properties of the horizontal support block")
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
        bs.part("", ResourceUtils.plainModel(this));
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
            m.textures(textures);
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("tfc:block/wood/support/vertical");
            m.textures(textures);
        });
        if (defaultConnection) {
            generator.blockModel(newID("", "_connection"), m -> {
                m.parent("tfc:block/wood/support/connection");
                m.textures(textures);
            });
        }
    }
}
