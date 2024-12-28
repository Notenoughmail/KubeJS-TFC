package com.notenoughmail.kubejs_tfc.addons.firmalife.block;

import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blocks.CheeseWheelBlock;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class CheeseWheelBlockBuilder extends ExtendedPropertiesShapedBlockBuilder {

    public final transient ItemBuilder sliceItem;
    private static final String[] ages = new String[] {"fresh", "aged", "vintage"};
    private static final String[] insideTextures = new String[3];

    public CheeseWheelBlockBuilder(ResourceLocation i) {
        super(i);
        soundType(SoundType.WART_BLOCK);
        hardness(2f);
        sliceItem = new BasicItemJS.Builder(newID("", "_slice"));
        renderType("cutout");
        RegistryUtils.hackBlockEntity(FLBlockEntities.TICK_COUNTER, this);
        textureAll(newID("block/", "").toString());
        insideTextures[0] = insideTextures[1] = insideTextures[2] = null;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("surface", tex);
        texture("particle", tex);
        texture("down", tex);
        insideTextures[0] = insideTextures[1] = insideTextures[2] = tex;
        return this;
    }

    public CheeseWheelBlockBuilder freshInsideTexture(String tex) {
        insideTextures[0] = tex;
        return this;
    }

    public CheeseWheelBlockBuilder agedInsideTexture(String tex) {
        insideTextures[1] = tex;
        return this;
    }

    public CheeseWheelBlockBuilder vintageInsideTexture(String tex) {
        insideTextures[2] = tex;
        return this;
    }

    @Info(value = "Modifies the block's slice item")
    @Generics(value = ItemBuilder.class)
    public CheeseWheelBlockBuilder sliceItem(Consumer<ItemBuilder> slice) {
        slice.accept(sliceItem);
        return this;
    }

    @Override
    @Generics(value = BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null; // Do not set lootTable to EMPTY
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Override
    public Block createObject() {
        return new CheeseWheelBlock(createExtendedProperties(), sliceItem);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.ITEM.addBuilder(sliceItem);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int age = 0 ; age < 3 ; age++) {
            int finalAge = age;
            for (int i = 1 ; i < 5 ; i++) {
                final String parent ="firmalife:block/cheese_" + i; // makes the lambda not complain when up here
                generator.blockModel(newID("", "_" + ages[age] +"_" + i), m -> {
                    m.parent(parent);
                    m.textures(textures);
                    m.texture("inside", insideTextures[finalAge] == null ?
                            newID("block/", "_surface_" + finalAge).toString() :
                            insideTextures[finalAge]
                    );
                });
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String blockModelLoc = model.isEmpty() ? (id.getNamespace() + ":block/" + id.getPath()) : model;
        for (int i = 1 ; i < 5 ; i++) {
            for (String age : ages) {
                bs.simpleVariant("age=" + age + ",count=" + i, blockModelLoc + "_" + age + "_" + i);
            }
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent(newID("block/", "_fresh_4").toString());
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {

        final LootBuilder lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(sliceItem.get(), 4));
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(FLBlockEntities.TICK_COUNTER);
    }
}
