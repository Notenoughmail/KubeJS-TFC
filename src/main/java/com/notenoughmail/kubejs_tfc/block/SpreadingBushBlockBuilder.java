package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.SpreadingCaneBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

// TODO: Loot table
public class SpreadingBushBlockBuilder extends StationaryBerryBushBlockBuilder {

    public transient final SpreadingCaneBlockBuilder child;
    public transient int maxHeight;
    public transient final Supplier<ClimateRange> climateRange;

    public SpreadingBushBlockBuilder(ResourceLocation i) {
        super(i);
        child = new SpreadingCaneBlockBuilder(newID("", "_cane"), this);
        maxHeight = 3;
        climateRange = ClimateRange.MANAGER.register(id);
        texture("layer0", newID("item/", "").toString());
    }

    public SpreadingBushBlockBuilder maxHeight(int i) {
        maxHeight = i;
        return this;
    }

    @Override
    public Block createObject() {
        return new SpreadingBushBlock(createExtendedProperties(), productItem, lifecycles, child, maxHeight, climateRange);
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        RegistryInfo.BLOCK.addBuilder(child);
        RegistryUtils.addBerryBush(child);
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
