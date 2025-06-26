package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.sub.SpreadingCaneBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpreadingBushBlockBuilder extends StationaryBerryBushBlockBuilder {

    public transient final SpreadingCaneBlockBuilder cane;
    public transient int maxHeight;
    public transient final Supplier<ClimateRange> climateRange;

    public SpreadingBushBlockBuilder(ResourceLocation i) {
        super(i);
        cane = new SpreadingCaneBlockBuilder(newID("", "_cane"), this);
        maxHeight = 3;
        climateRange = ClimateRange.MANAGER.register(id);
        texture("layer0", newID("item/", "").toString());
        renderType("cutout_mipped");
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, cane);
        tagBlock(TFCTags.Blocks.ANY_SPREADING_BUSH.location());
    }

    @Override
    protected ModelFunc initModels() {
        return (lc, stage, m) -> {
            m.parent("tfc:block/plant/berry_bush_" + stage); // The only difference from super
            m.texture(
                    "bush",
                    textures.has("#" + lc.ordinal() + "_" + stage) ?
                            textures.get("#" + lc.ordinal() + "_" + stage).getAsString() :
                            newID("block/", "_" + lc.getSerializedName()).toString()
            );
        };
    }

    @Info("Sets the properties of the cane block")
    @Generics(SpreadingCaneBlockBuilder.class)
    public SpreadingBushBlockBuilder cane(Consumer<SpreadingCaneBlockBuilder> cane) {
        cane.accept(this.cane);
        return this;
    }

    @Info("Sets the maximum height this bush can grow to, defaults to 3")
    public SpreadingBushBlockBuilder maxHeight(int i) {
        maxHeight = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new SpreadingBushBlock(createExtendedProperties(), productGetter(), lifecycles, cane, maxHeight, climateRange);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(cane);
    }

    @Override
    @Generics(BlockItemBuilder.class)
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
        ResourceUtils.lootTable(b -> {
            b.addPool(p -> {
                p.survivesExplosion();
                p.addItem(ResourceUtils.STICK_STACK);
            });
            if (itemBuilder != null) {
                b.addPool(p -> {
                    p.survivesExplosion();
                    p.addEntry(ResourceUtils.alternatives(
                            lootEntryBase()
                                    .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("stage", "2"))),
                            (LootTableEntry) lootEntryBase()
                                    .randomChance(0.5D)
                    ));
                });
            }
        }, generator, this);
    }

    private LootTableEntry lootEntryBase() {
        final JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:item");
        json.addProperty("name", itemBuilder.id.toString());
        return new LootTableEntry(json).addCondition(ResourceUtils.sharpToolsCondition());
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
            m.textures(textures);
        }
    }
}
