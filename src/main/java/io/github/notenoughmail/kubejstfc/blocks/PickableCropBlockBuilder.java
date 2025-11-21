package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PickableCropBlockBuilder extends AbstractCropBlockBuilder.WithProduct {

    @Nullable
    public transient Supplier<Item> fruit;
    @HideFromJS
    @Nullable
    public Supplier<Supplier<? extends Item>> f() {
        return fruit == null ? null : () -> fruit;
    }
    public transient Supplier<Item> matureFruit;
    @HideFromJS
    public Supplier<Supplier<? extends Item>> mf() {
        return () -> matureFruit;
    }

    public PickableCropBlockBuilder(ResourceLocation i) {
        super(i, Type.PICKABLE);
        matureFruit = () -> Items.APPLE;
    }

    @Info("Sets the item to be given to the player when they pick the block")
    public PickableCropBlockBuilder fruit(Holder<Item> fruit) {
        this.fruit = Assistant.holderAsSupplier(fruit);
        return this;
    }

    @Info("Sets the item to be given to the player when the pick the block and the crop is mature, defaults to 'minecraft:apple'")
    public PickableCropBlockBuilder matureFruit(Holder<Item> matureFruit) {
        this.matureFruit = Assistant.holderAsSupplier(matureFruit);
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtil.pickableCrop(this);
    }
}
