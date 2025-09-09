package com.notenoughmail.kubejs_tfc.util.implementation;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class NamedRegistryMetal implements RegistryMetal {

    public static NamedRegistryMetal fromTFC(Metal.Default metal) {
        final Map<Metal.BlockType, ? extends Supplier<Block>> blocks = TFCBlocks.METALS.get(metal);
        final Map<Metal.ItemType, ? extends Supplier<Item>> items = TFCItems.METAL_ITEMS.get(metal);
        return new NamedRegistryMetal(
                metal,
                TerraFirmaCraft.MOD_ID,
                blocks::get,
                items::get,
                TFCFluids.METALS.get(metal).source()::get
        );
    }

    private final RegistryMetal metal;
    private final String mod;
    private final Function<Metal.BlockType, @Nullable Supplier<Block>> blocks;
    private final Function<Metal.ItemType, @Nullable Supplier<Item>> items;
    private final @Nullable Supplier<Fluid> fluid;

    public NamedRegistryMetal(RegistryMetal metal, String mod, Function<Metal.BlockType, @Nullable Supplier<Block>> blocks, Function<Metal.ItemType, @Nullable Supplier<Item>> items, @Nullable Supplier<Fluid> fluid) {
        this.metal = metal;
        this.mod = mod;
        this.blocks = blocks;
        this.items = items;
        this.fluid = fluid;
    }

    public String getMod() {
        return mod;
    }

    @Nullable
    public Supplier<Block> getBlock(Metal.BlockType type) {
        return blocks.apply(type);
    }

    @Nullable
    public Supplier<Item> getItem(Metal.ItemType type) {
        return items.apply(type);
    }

    @Nullable
    public Supplier<Fluid> getFluid() {
        return fluid;
    }

    @Override
    public Tier toolTier() {
        return metal.toolTier();
    }

    @Override
    public ArmorMaterial armorTier() {
        return metal.armorTier();
    }

    @Override
    public Metal.Tier metalTier() {
        return metal.metalTier();
    }

    @Override
    public Supplier<Block> getFullBlock() {
        return metal.getFullBlock();
    }

    @Override
    public MapColor mapColor() {
        return metal.mapColor();
    }

    @Override
    public Rarity getRarity() {
        return metal.getRarity();
    }

    @Override
    public String getSerializedName() {
        return metal.getSerializedName();
    }

    @Override
    public String toString() {
        return "NamedRegistryMetal/" + metal.toString();
    }
}
