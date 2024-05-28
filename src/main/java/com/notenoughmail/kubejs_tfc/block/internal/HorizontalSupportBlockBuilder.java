package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.ISupportExtendedProperties;
import com.notenoughmail.kubejs_tfc.block.SupportBlockBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class HorizontalSupportBlockBuilder extends MultipartShapedBlockBuilder  implements ISupportExtendedProperties {

    public transient Consumer<ExtendedPropertiesJS> props;
    public transient final SupportBlockBuilder parent;

    public HorizontalSupportBlockBuilder(ResourceLocation i, SupportBlockBuilder parent) {
        super(i);
        props = p -> {};
        this.parent = parent;
        itemBuilder = null;
        tagBlock(TFCTags.Blocks.SUPPORT_BEAM.location());
    }

    @Override
    public Block createObject() {
        return new HorizontalSupportBlock(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = extendedPropsJS();
        props.accept(propsJs);
        return propsJs.delegate();
    }

    @Override
    public HorizontalSupportBlockBuilder extendedProperties(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    @Override
    public void createAdditionalObjects() {}

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        final LootBuilder lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else if (parent.get().asItem() != Items.AIR) {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(parent.get()));
            });
        }

        generator.json(parent.newID("loot_table/blocks/", ""), lootBuilder.toJson());
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        bs.part("", parent.newID("block/", "_horizontal").toString());
        bs.part("north=true", p -> p.model(parent.connection).y(270));
        bs.part("east=true", parent.connection);
        bs.part("south=true", p -> p.model(parent.connection).y(90));
        bs.part("west=true", p -> p.model(parent.connection).y(180));
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {}

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(parent.newID("", "_horizontal"), m -> {
            m.parent("tfc:block/wood/support/horizontal");
            m.texture("texture", parent.newID("block/", "").toString());
            m.texture("particle", parent.newID("block/", "").toString());
        });
    }
}
