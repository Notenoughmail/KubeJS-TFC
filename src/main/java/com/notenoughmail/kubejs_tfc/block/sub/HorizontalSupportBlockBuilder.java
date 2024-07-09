package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.SupportBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class HorizontalSupportBlockBuilder extends ExtendedPropertiesMultipartShapedBlockBuilder {

    public transient final SupportBlockBuilder parent;

    public HorizontalSupportBlockBuilder(ResourceLocation i, SupportBlockBuilder parent) {
        super(i);
        this.parent = parent;
        itemBuilder = null;
        tagBlock(TFCTags.Blocks.SUPPORT_BEAM.location());
    }

    @Override
    public Block createObject() {
        return new HorizontalSupportBlock(createExtendedProperties());
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
        if (model.isEmpty()) {
            generator.blockModel(parent.newID("", "_horizontal"), m -> {
                m.parent("tfc:block/wood/support/horizontal");
                m.textures(textures);
            });
        } else {
            generator.blockModel(id, m -> m.parent(model));
        }
    }
}
