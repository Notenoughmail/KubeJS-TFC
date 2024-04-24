package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// TODO: Document
public class RegisterInteractionsEventJS extends EventJS {

    private static final List<BlockItemPlacement> customBlockItemPlacements = new ArrayList<>();
    public static void addBlockItemPlacement(Supplier<Item> item, Supplier<Block> block) {
        customBlockItemPlacements.add(new BlockItemPlacement(item, block));
    }

    public RegisterInteractionsEventJS() {
        customBlockItemPlacements.forEach(InteractionManager::register);
    }

    public void interaction(Ingredient ingredient, boolean targetBlocks, boolean targetAir, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, targetBlocks, targetAir, action);
    }

    public void interaction(Ingredient ingredient, boolean targetAir, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, targetAir, action);
    }

    public void interaction(Ingredient ingredient, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, action);
    }

    public void blockItemPlacement(Item item, Block block) {
        InteractionManager.register(new BlockItemPlacement(() -> item, () -> block));
    }
}
