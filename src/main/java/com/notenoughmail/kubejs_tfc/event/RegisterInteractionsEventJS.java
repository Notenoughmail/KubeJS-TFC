package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RegisterInteractionsEventJS extends EventJS {

    private static final List<BlockItemPlacement> customBlockItemPlacements = new ArrayList<>();
    public static void addBlockItemPlacement(Supplier<Item> item, Supplier<Block> block) {
        customBlockItemPlacements.add(new BlockItemPlacement(item, block));
    }

    public RegisterInteractionsEventJS() {
        customBlockItemPlacements.forEach(InteractionManager::register);
    }

    @Info(value = "Registers an interaction", params = {
            @Param(name = "ingredient", value = "The items this interactions applies to"),
            @Param(name = "targetBlocks", value = "If blocks should be targeted in the interaction"),
            @Param(name = "targetAir", value = "If the interaction should register when clicking in the air"),
            @Param(name = "action", value = "A callback for the action to perform, requires an `InteractionResult` be returned")
    })
    public void interaction(Ingredient ingredient, boolean targetBlocks, boolean targetAir, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, targetBlocks, targetAir, action);
    }

    @Info(value = "Registers an interaction, targeting blocks by default", params = {
            @Param(name = "ingredient", value = "The items this interactions applies to"),
            @Param(name = "targetAir", value = "If the interaction should register when clicking in the air"),
            @Param(name = "action", value = "A callback for the action to perform, requires an `InteractionResult` be returned")
    })
    public void interaction(Ingredient ingredient, boolean targetAir, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, targetAir, action);
    }

    @Info(value = "Registers an interaction, targeting blocks, but not air, by default", params = {
            @Param(name = "ingredient", value = "The items this interactions applies to"),
            @Param(name = "action", value = "A callback for the action to perform, requires an `InteractionResult` be returned")
    })
    public void interaction(Ingredient ingredient, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, action);
    }

    @Info(value = "Registers a default block placement interaction", params = {
            @Param(name = "item", value = "The item"),
            @Param(name = "block", value = "The block to be placed")
    })
    public void blockItemPlacement(Item item, Block block) {
        InteractionManager.register(new BlockItemPlacement(() -> item, () -> block));
    }
}
