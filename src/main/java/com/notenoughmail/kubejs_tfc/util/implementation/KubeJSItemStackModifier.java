package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
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
                @Param(name = "stack", value = "The current output stack, A no-op modifier would just return this stack"),
                @Param(name = "input", value = "The provided input stack. Do not modify this stack during the modifier")
        })
        ItemStack apply(ItemStack stack, ItemStack input);
    }
}
