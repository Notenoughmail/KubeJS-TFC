package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.HorizontalSupportBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.internal.StandingAndWallBlockItemBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.VerticalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SupportBlockBuilder extends MultipartShapedBlockBuilder implements ISupportExtendedProperties {

    public transient final HorizontalSupportBlockBuilder horizontal;
    public transient Consumer<ExtendedPropertiesJS> props;
    public transient final String connection;

    public SupportBlockBuilder(ResourceLocation i) {
        super(i);
        horizontal = new HorizontalSupportBlockBuilder(newID("", "_horizontal"), this);
        itemBuilder = new StandingAndWallBlockItemBuilder(id, this, horizontal);
        props = p -> {};
        connection = newID("block/", "_connection").toString();
        tagBlock(TFCTags.Blocks.SUPPORT_BEAM.location());
    }

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
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = extendedPropsJS();
        props.accept(propsJs);
        return propsJs.delegate();
    }

    @Override
    public SupportBlockBuilder extendedProperties(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
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
            m.parent("tfc:block/wood/support/vertical");
            m.texture("texture", newID("block/", "").toString());
            m.texture("particle", newID("block/", "").toString());
        });
        generator.blockModel(newID("", "_connection"), m -> {
            m.parent("tfc:block/wood/support/connection");
            m.texture("texture", newID("block/", "").toString());
            m.texture("particle", newID("block/", "").toString());
        });
    }
}
