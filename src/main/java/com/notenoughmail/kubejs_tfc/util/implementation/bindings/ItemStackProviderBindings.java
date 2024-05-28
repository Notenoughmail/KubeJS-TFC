package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public enum ItemStackProviderBindings {
    INSTANCE;

    @Info(value = "Creates an item stack provider based on the provided item stack")
    public ItemStackProviderJS of(ItemStack itemStack) {
        return new ItemStackProviderJS(itemStack, new JsonArray(0));
    }

    @Info(value = "Creates an item stack provider based on the provided item stack and item stack modifiers", params = {
            @Param(name = "itemStack", value = "The item stack"),
            @Param(name = "modifiers", value = "The item stack modifiers to be applied to the item stack")
    })
    public ItemStackProviderJS of(ItemStack itemStack, Object modifiers) {
        return ItemStackProviderJS.of(itemStack, modifiers);
    }

    @Info(value = "Creates an empty item stack provider with the given item stack modifiers")
    public ItemStackProviderJS empty(Object modifiers) {
        return ItemStackProviderJS.of(ItemStack.EMPTY, modifiers);
    }

    @Info(value = "Creates an empty item stack provider")
    public ItemStackProviderJS empty() {
        return ItemStackProviderJS.EMPTY.copy();
    }

    @Info(value = "Creates an empty item stack provider with the given item stack modifiers and with the 'tfc:copy_input' modifier automatically applied")
    public ItemStackProviderJS copyInput(Object modifiers) {
        return empty(modifiers).copyInput();
    }

    @Info(value = "Creates an empty item stack provider with the 'tfc:copy_input' modifier automatically applied")
    public ItemStackProviderJS copyInput() {
        return empty().copyInput();
    }
}
