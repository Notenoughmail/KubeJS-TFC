package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.IExtensibleEnum;

public enum ItemStackModifierJS implements ItemStackModifier.SingleInstance<ItemStackModifierJS>, IExtensibleEnum {
    ;

    private final ModifierApplicator applicator;
    private final boolean dependsOnInput;

    ItemStackModifierJS(ModifierApplicator applicator, boolean dependsOnInput) {
        this.applicator = applicator;
        this.dependsOnInput = dependsOnInput;
    }

    @Override
    public ItemStackModifierJS instance() {
        return this;
    }

    @Override
    public ItemStack apply(ItemStack stack, ItemStack input) {
        return applicator.apply(stack, input);
    }

    @Override
    public boolean dependsOnInput() {
        return dependsOnInput;
    }

    public static ItemStackModifierJS create(String name, ModifierApplicator applicator, boolean dependsOnInput) {
        throw new IllegalStateException("Enum not extended");
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
