package com.notenoughmail.kubejs_tfc.block.internal;

import com.notenoughmail.kubejs_tfc.block.SpreadingBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.StationaryBerryBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingCaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpreadingCaneBlockBuilder extends BlockBuilder {

    private final SpreadingBushBlockBuilder parent;

    public SpreadingCaneBlockBuilder(ResourceLocation i, SpreadingBushBlockBuilder parent) {
        super(i);
        this.parent = parent;
        noItem();
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
    public Block createObject() {
        return new SpreadingCaneBlock(parent.createExtendedProperties(), parent.productItem, parent.lifecycles, parent, parent.maxHeight, parent.climateRange);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (String lifecycle : StationaryBerryBushBlockBuilder.lc) {
            final String texBase = newID("block/", "_" + lifecycle + "_").toString();
            for (int i = 0 ; i < 4 ; i++) {
                final int finalI = i; // Lambda garbage
                generator.blockModel(newID("", "_side_" + lifecycle + "_" + i), m -> {
                    m.parent("tfc:block/plant/berry_bush_" + finalI);
                    m.texture("cane", texBase + "cane");
                    m.texture("bush", texBase + "bush");
                });
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (String lifecycle : StationaryBerryBushBlockBuilder.lc) {
            for (int i = 0 ; i < 4 ; i++) {
                final String dir = ModelUtils.cardinalDirections[i];
                final int finalI = i;
                for (int j = 0 ; j < 3 ; j++) {
                    final int finalJ = j; // Lambda stuff
                    bs.variant("lifecycle=" + lifecycle + ",facing=" + dir + ",stage=" + j, v ->
                        v.model(newID("block/", "_side_" + lifecycle + "_" + finalJ).toString()).y(finalI * 90)
                    );
                }
            }
        }
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
                p.addItem(DataUtils.STICK_STACK);
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }
}
