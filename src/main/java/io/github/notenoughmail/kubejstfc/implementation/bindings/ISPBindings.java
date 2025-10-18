package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;

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

    public ItemStackProvider of(ItemStack stack, List<ItemStackModifier> modifiers) {
        return ItemStackProvider.of(stack, modifiers);
    }

    public ItemStackProvider of(ItemStack stack) {
        return of(stack, new ArrayList<>());
    }

    public ItemStackProvider empty() {
        return of(ItemStack.EMPTY);
    }

    public ItemStackProvider copyInputStack() {
        return empty().kubejs_tfc$copyInputStack();
    }
}
