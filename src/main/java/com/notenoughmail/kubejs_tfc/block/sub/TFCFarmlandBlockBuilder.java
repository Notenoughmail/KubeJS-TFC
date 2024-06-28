package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCDirtBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCFarmlandBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final TFCDirtBlockBuilder parent;

    public TFCFarmlandBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        tag(TFCTags.Blocks.FARMLAND.location());
        texture("dirt", parent.textures.get("particle").getAsString()); // Parent does not yet exist when #textureAll is called in super constructor
    }

    @Info(value = "Makes the farmland block use a unique texture for the dirt part of its texture, by default uses the texture of its parent dirt block")
    public TFCFarmlandBlockBuilder uniqueDirtTexture() {
        texture("dirt", id.getNamespace() + ":block/" + id.getPath());
        return this;
    }

    @Override
    public Block createObject() {
        return new FarmlandBlock(createExtendedProperties(), parent);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        texture("top", tex);
        texture("dirt", tex);
        return this;
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
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.FARMLAND);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (model.isEmpty()) {
            generator.blockModel(id, m -> {
                m.parent("block/template_farmland");
                m.textures(textures);
            });
        } else {
            hasModel(generator);
        }
    }
}
