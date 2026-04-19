package com.notenoughmail.kubejs_tfc.addons.firmalife.block;

import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import com.eerussianguy.firmalife.common.blocks.CheeseWheelBlock;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
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
public class CheeseWheelBlockBuilder extends ExtendedPropertiesMultipartShapedBlockBuilder {

    public transient final DelayedBuilder<ItemBuilder> sliceItem;
    private static final String[] ages = new String[] {"fresh", "aged", "vintage"};
    public transient final String[] insideTextures = new String[3];
    public transient String rackModel;

    public CheeseWheelBlockBuilder(ResourceLocation i) {
        super(i);
        soundType(SoundType.WART_BLOCK);
        hardness(2f);
        sliceItem = new DelayedBuilder<>(BasicItemJS.Builder::new, () -> newID("", "_slice"));
        renderType("cutout");
        RegistryUtils.hackBlockEntity(FLBlockEntities.TICK_COUNTER, this);
        rackModel = "tfc:block/barrel_rack";
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("surface", tex);
        texture("down", tex);
        return this;
    }

    @Info("Sets the inside texture for the fresh state")
    public CheeseWheelBlockBuilder freshInsideTexture(String tex) {
        insideTextures[0] = tex;
        return this;
    }

    @Info("Sets the inside texture for the aged state")
    public CheeseWheelBlockBuilder agedInsideTexture(String tex) {
        insideTextures[1] = tex;
        return this;
    }

    @Info("Sets the inside texture for the vintage state")
    public CheeseWheelBlockBuilder vintageInsideTexture(String tex) {
        insideTextures[2] = tex;
        return this;
    }

    @Info("Sets the model to use for the rack")
    public CheeseWheelBlockBuilder barrelRackModel(String model) {
        rackModel = model;
        return this;
    }

    @Info("Modifies the block's slice item")
    @Generics(ItemBuilder.class)
    public CheeseWheelBlockBuilder sliceItem(Consumer<ItemBuilder> slice) {
        return sliceItem(sliceItem.fallbackId(), slice);
    }

    @Info("Modifies the block's slice item")
    @Generics(ItemBuilder.class)
    public CheeseWheelBlockBuilder sliceItem(ResourceLocation id, Consumer<ItemBuilder> slice) {
        slice.accept(sliceItem.get(id));
        return this;
    }

    @Override
    @Generics(BlockItemBuilder.class)
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
        return new CheeseWheelBlock(createExtendedProperties(), sliceItem.get());
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.ITEM.addBuilder(sliceItem.get());
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
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        bs.part("rack=true", rackModel);
        final String modelLoc = ResourceUtils.plainModel(this);
        for (int i = 1 ; i < 5 ; i++) {
            for (String age : ages) {
                bs.part("age=" + age + ",count=" + i, modelLoc + "_" + age + "_" + i);
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
                p.addItem(new ItemStack(sliceItem.get().get(), 4));
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
