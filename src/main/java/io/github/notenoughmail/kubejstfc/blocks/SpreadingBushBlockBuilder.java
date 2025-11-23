package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.SpreadingCaneBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ReturnsSelf
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
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, cane);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.fullTable(null, t -> {
            LootUtil.pool(t, p -> {
                LootUtil.survivesExplosion(p);
                p.add(LootItem.lootTableItem(Items.STICK));
            });
            if (itemBuilder != null) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(LootUtil.alternatives(
                            LootItem.lootTableItem(itemBuilder.get())
                                    .when(LootUtil.sharpTools())
                                    .when(LootUtil.withState(get(), s -> s.hasProperty(SpreadingBushBlock.STAGE, 2))),
                            LootItem.lootTableItem(itemBuilder.get())
                                    .when(LootUtil.sharpTools())
                                    .when(LootUtil.chance(0.5F))
                    ));
                });
            }
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }
}
