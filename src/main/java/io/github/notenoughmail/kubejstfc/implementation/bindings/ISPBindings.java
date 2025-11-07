package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum ISPBindings {
    INSTANCE;

    public static ItemStackProvider wrap(Context ctx, Object o) {
        o = Wrapper.unwrapped(o);
        if (o instanceof ItemStackProvider isp) {
            return isp;
        }
        return INSTANCE.of(ItemWrapper.wrap(ctx, o));
    }

    @Info("Creates an ItemStackProvider from the stack and list of modifiers")
    public ItemStackProvider of(ItemStack stack, List<ItemStackModifier> modifiers) {
        return ItemStackProvider.of(stack, modifiers);
    }

    @Info("Creates a mutable ItemStackModifier from the stack")
    public ItemStackProvider of(ItemStack stack) {
        return of(stack, new ArrayList<>());
    }

    @Info("Creates an empty ItemStackModifier")
    public ItemStackProvider empty() {
        return of(ItemStack.EMPTY);
    }

    @Info("Creates an empty ItemStackModifier with a `tfc:copy_input` modifier applied")
    public ItemStackProvider copyInputStack() {
        return empty().kubejs_tfc$copyInputStack();
    }

    @Info("Gets the crafting player, may be null")
    @Nullable
    public Player getCraftingPlayer() {
        return RecipeHelpers.getCraftingPlayer();
    }

    @Info("Sets the crafting input, used for stack modifiers which need access to the whole inventory. `#clearCraftingInput` must be called when this has been called")
    public void setCraftingInput(IItemHandler inventory) {
        setCraftingInput(inventory, 0, inventory.getSlots() - 1); // TODO: 2.0.0 | Verify the bounds
    }

    @Info("Sets the crafting input, used for stack modifiers which need access to the whole inventory. `#clearCraftingInput` must be called when this has been called")
    public void setCraftingInput(IItemHandler inventory, int startSlot, int endSlot) {
        RecipeHelpers.setCraftingInput(inventory, startSlot, endSlot);
    }

    @Info("Clears the crafting input. Must be called after usage of `#setCraftingInput`")
    public void clearCraftingInput() {
        RecipeHelpers.clearCraftingInput();
    }
}
