package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// Include id so #toString() returns something helpful
public record KubeJSItemStackModifier(ResourceLocation id, ModifierApplicator applicator, boolean dependsOnInput) implements ItemStackModifier.SingleInstance<KubeJSItemStackModifier> {

    @Override
    public KubeJSItemStackModifier instance() {
        return this;
    }

    @Override
    public ItemStack apply(ItemStack stack, ItemStack input) {
        return applicator.apply(stack, input);
    }

    @FunctionalInterface
    public interface ModifierApplicator {
        @Info(value = "Applies the modifier to the stack", params = {
                @Param(name = "stack", value = "The current output stack, may be modified. A no-op modifier would just return this stack"),
                @Param(name = "input", value = "The provided input stack. Do not modify this stack")
        })
        ItemStack apply(ItemStack output, ItemStack input);
    }

    @FunctionalInterface
    public interface  SimpleApplicator extends ModifierApplicator {
        @Info(value = "Applies a modifier to the stack", params = {
                @Param(name = "stack", value = "The current output stack, may be modified. A no-op modifier would just return this stack")
        })
        ItemStack apply(ItemStack stack);

        @Override
        default ItemStack apply(ItemStack output, ItemStack input) {
            return apply(output);
        }
    }

    @FunctionalInterface
    public interface WithInventoryApplicator extends ModifierApplicator {
        @Info(value = "Applies a modifier to the stack", params = {
                @Param(name = "stack", value = "The current output stack, may be modified. A no-op modifier would just return this stack"),
                @Param(name = "input", value = "The provided input stack. Do not modify this stack"),
                @Param(name = "inventory", value = "An iterable view of the inventory's items")
        })
        ItemStack apply(ItemStack output, ItemStack input, Iterable<ItemStack> inventory);

        @Override
        default ItemStack apply(ItemStack output, ItemStack input) {
            return apply(output, input, RecipeHelpers.getCraftingInput());
        }
    }
}
