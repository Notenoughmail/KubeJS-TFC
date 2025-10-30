package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.SupportBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class HorizontalSupportBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final SupportBlockBuilder parent;

    public HorizontalSupportBlockBuilder(ResourceLocation i, SupportBlockBuilder parent) {
        super(i);
        this.parent = parent;
        itemBuilder = null;
        tagBlock(TFCTags.Blocks.SUPPORT_BEAM.location());
    }

    @Override
    public Block createObject() {
        return new HorizontalSupportBlock(createExtendedProperties().noOcclusion());
    }

    @Override
    public void createAdditionalObjects() {}

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTableBasic(generator, this, parent);
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        bs.part("", ResourceUtils.plainModel(this));
        bs.part("north=true", p -> p.model(parent.connection).y(270));
        bs.part("east=true", parent.connection);
        bs.part("south=true", p -> p.model(parent.connection).y(90));
        bs.part("west=true", p -> p.model(parent.connection).y(180));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this,  m -> {
            m.parent("tfc:block/wood/support/horizontal");
            m.textures(textures);
        });
    }
}
