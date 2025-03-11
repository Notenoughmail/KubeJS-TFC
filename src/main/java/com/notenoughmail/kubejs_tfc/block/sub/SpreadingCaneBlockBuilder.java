package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.SpreadingBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.StationaryBerryBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingCaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpreadingCaneBlockBuilder extends BlockBuilder {

    private final SpreadingBushBlockBuilder parent;
    public transient final Consumer<ModelGenerator>[][] models;

    public SpreadingCaneBlockBuilder(ResourceLocation i, SpreadingBushBlockBuilder parent) {
        super(i);
        this.parent = parent;
        models = new Consumer[4][3];
        allModels((lc, stage) -> m -> {
            m.parent("tfc:block/plant/berry_bush_side_" + stage);
            m.texture(
                    "cane",
                    (textures.has("#cane_" + lc.ordinal() + "_" + stage) ?
                            textures.get("#cane_" + lc.ordinal() + "_" + stage) :
                            newID("block/", "_cane_" + lc.getSerializedName())
                    ).toString()
            );
            m.texture(
                    "bush",
                    (textures.has("#bush_" + lc.ordinal() + "_" + stage) ?
                            textures.get("#bush_" + lc.ordinal() + "_" + stage) :
                            parent.textures.has("#" + lc.ordinal() + "_" + stage) ?
                                    parent.textures.get("#" + lc.ordinal() + "_" + stage) :
                                    newID("block/", "_bush_" + lc.getSerializedName())
                    ).toString()
            );
        });
        noItem();
        renderType("cutout_mipped");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
        tagBlock(TFCTags.Blocks.ANY_SPREADING_BUSH.location());
    }

    @Info("Sets the model for the given lifecycle and stage")
    @Generics({ ModelGenerator.class })
    public SpreadingCaneBlockBuilder model(Lifecycle lifecycle, int stage, Consumer<ModelGenerator> modelGenerator) {
        models[lifecycle.ordinal()][stage] = modelGenerator;
        return this;
    }

    @Info("Sets the model for all lifecycle and stage combinations via a callback")
    public SpreadingCaneBlockBuilder allModels(StationaryBerryBushBlockBuilder.BushModelsCreator modelsCreator) {
        for (Lifecycle lc : StationaryBerryBushBlockBuilder.LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final var m = modelsCreator.getFor(lc, i);
                if (m != null) {
                    models[lc.ordinal()][i] = m;
                }
            }
        }
        return this;
    }

    @Info("Sets the cane texture for the given lifecycle and stage")
    public SpreadingCaneBlockBuilder texture(Lifecycle lifecycle, int stage, String tex) {
        texture("#cane_" + lifecycle.ordinal() + "_" + stage, tex);
        return this;
    }

    @Info("Sets the cane and bush texture for the given lifecycle and stage")
    public SpreadingCaneBlockBuilder texture(Lifecycle lifecycle, int stage, String caneTexture, String bushTexture) {
        texture("#cane_" + lifecycle.ordinal() + "_" + stage, caneTexture);
        texture("#bush_" + lifecycle.ordinal() + "_" + stage, bushTexture);
        return this;
    }

    @Override
    @Generics(value = BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            var item = getOrCreateItemBuilder();
            item.blockBuilder = this;
            i.accept(item);
        }

        return this;
    }

    @Override
    public Block createObject() {
        return new SpreadingCaneBlock(parent.createExtendedProperties(), parent.productItem, parent.lifecycles, parent, parent.maxHeight, parent.climateRange);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (int i = 0 ; i < 4 ; i ++) {
            for (int j = 0 ; j < 3 ; j++) {
                generator.blockModel(parent.newID("", "_side_" + StationaryBerryBushBlockBuilder.lc[i] + "_" + j), models[i][j]);
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (String lifecycle : StationaryBerryBushBlockBuilder.lc) {
            for (int i = 0 ; i < 4 ; i++) {
                final String dir = ResourceUtils.cardinalDirections[i];
                final int finalI = i;
                for (int j = 0 ; j < 3 ; j++) {
                    final int finalJ = j; // Lambda stuff
                    bs.variant("lifecycle=" + lifecycle + ",facing=" + dir + ",stage=" + j, v ->
                        v.model(parent.newID("block/", "_side_" + lifecycle + "_" + finalJ).toString()).y(finalI * 90)
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
                p.addItem(ResourceUtils.STICK_STACK);
            });
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }
}
