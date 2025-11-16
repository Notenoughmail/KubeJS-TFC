package io.github.notenoughmail.kubejstfc.blocks;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.SpreadingCaneBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SpreadingBushBlockBuilder extends StationaryBerryBushBlockBuilder {

    public transient final SpreadingCaneBlockBuilder cane;
    public transient int maxHeight;

    public SpreadingBushBlockBuilder(ResourceLocation i) {
        super(i);
        cane = new SpreadingCaneBlockBuilder(id.withSuffix("_cane"), this);
        maxHeight = 3;
        renderType(BlockRenderType.CUTOUT_MIPPED);
        Assistant.singleTag(this, TFCTags.Blocks.SPREADING_BUSHES);
    }

    @Override
    protected ModelFunc initModels() {
        return (lc, stage, m) -> {
            m.parent(KubeJSTFC.tfc("block/plant/berry_bush_" + stage));// The only difference from super
            m.textures(textures);
        };
    }

    @Info("Sets the properties of the cane block")
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
