package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.registry.RegistryWood;
import net.dries007.tfc.world.feature.tree.TFCTreeGrower;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class NamedRegistryWood implements RegistryWood {

    private final String mod;
    private final RegistryWood parent;

    public NamedRegistryWood(String mod, RegistryWood parent) {
        this.mod = mod;
        this.parent = parent;
    }

    @Info(value = "The mod this wood originates from")
    public String getMod() {
        return mod;
    }

    @Info(value = "The MapColor of the wood")
    @Override
    public MapColor woodColor() {
        return parent.woodColor();
    }

    @Info(value = "The MapColor of the wood's bark")
    @Override
    public MapColor barkColor() {
        return parent.barkColor();
    }

    @Info(value = "The TFCTreeGrower of the wood")
    @Override
    public TFCTreeGrower tree() {
        return parent.tree();
    }

    @Info(value = "The number of days it takes for the wood's sapling to grow")
    @Override
    public int daysToGrow() {
        return parent.daysToGrow();
    }

    @Info(value = "The vertical coordinate (from 0-255) on the foliage_fall colormap for this wood type's leaves.")
    @Override
    public int autumnIndex() {
        return parent.autumnIndex();
    }

    @Info(value = "A block of this wood, of the provided type")
    @Override
    public Supplier<Block> getBlock(Wood.BlockType type) {
        return parent.getBlock(type);
    }

    @Info(value = "The BlockSetType this wood uses")
    @Override
    public BlockSetType getBlockSet() {
        return parent.getBlockSet();
    }

    @Info(value = "The WoodType the wood uses")
    @Override
    public WoodType getVanillaWoodType() {
        return parent.getVanillaWoodType();
    }

    @Override
    public String getSerializedName() {
        return parent.getSerializedName();
    }
}
