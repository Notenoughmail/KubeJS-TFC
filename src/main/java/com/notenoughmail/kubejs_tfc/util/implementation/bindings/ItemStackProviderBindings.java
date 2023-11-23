package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.ItemStack;

// TODO: JSDocs
public enum ItemStackProviderBindings {
    @HideFromJS
    INSTANCE;

    public ItemStackProviderJS of(ItemStack itemStack) {
        return new ItemStackProviderJS(itemStack, new JsonArray(0));
    }

    public ItemStackProviderJS of(ItemStack itemStack, Object modifiers) {
        return ItemStackProviderJS.of(itemStack, modifiers);
    }

    public ItemStackProviderJS empty(Object modifiers) {
        return ItemStackProviderJS.of(ItemStack.EMPTY, modifiers);
    }

    public ItemStackProviderJS empty() {
        return ItemStackProviderJS.EMPTY.copy();
    }

    public ItemStackProviderJS copyInput(Object modifiers) {
        return empty(modifiers).copyInput();
    }

    public ItemStackProviderJS copyInput() {
        return empty().copyInput();
    }
}
