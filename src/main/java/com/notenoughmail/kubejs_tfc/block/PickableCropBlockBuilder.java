package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PickableCropBlockBuilder extends AbstractCropBlockBuilder {

    @Nullable
    public transient Supplier<Supplier<? extends Item>> fruit;
    public transient Supplier<Supplier<? extends Item>> matureFruit;

    public PickableCropBlockBuilder(ResourceLocation i) {
        super(i);
        matureFruit = () -> () -> Items.APPLE;
        type = Type.PICKABLE;
    }

    @Info(value = "Sets the item to be given to the player when they pick the block")
    public PickableCropBlockBuilder fruit(ResourceLocation fruit) {
        this.fruit = () -> () -> RegistryInfo.ITEM.getValue(fruit);
        return this;
    }

    @Info(value = "Sets the item to be given to the player when the pick the block and the crop is mature, defaults to 'minecraft:apple'")
    public PickableCropBlockBuilder matureFruit(ResourceLocation matureFruit) {
        this.matureFruit = () -> () -> RegistryInfo.ITEM.getValue(matureFruit);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtils.pickableCrop(createExtendedProperties(), stages, dead, seeds, nutrient, climateRange, fruit, matureFruit);
    }
}
