package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.KubeJSItemStackModifier;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RegisterItemStackModifierEventJS extends EventJS {

    @Info(value = "Creates a new item stack modifier with the given id and function", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function which will be applied when the modifier is applied to the stack"),
            @Param(name = "dependsOnInput", value = "Sets if the modifier is dependent on the input item, should be true if the input stack or the crafting container/inputs of the applicator is used")
    })
    public void register(ResourceLocation id, KubeJSItemStackModifier.ModifierApplicator applicator, boolean dependsOnInput) {
        ItemStackModifiers.register(id, new KubeJSItemStackModifier(id, applicator, dependsOnInput));
    }

    @Deprecated(forRemoval = true, since = "1.2.0")
    @Info(value = "Returns a supplier for the current crafting container. Only available during advanced shaped and shapeless crafting recipes. Deprecated, use `getCraftingInput()` instead")
    @Generics(CraftingContainer.class)
    public Supplier<@Nullable CraftingContainer> getCraftingContainer() {
        return RecipeHelpers::getCraftingContainer;
    }

    @Info("Returns an iterator over the current crafting input items. Always available, but may be empty if the recipe type does not support it")
    @Generics(ItemStack.class)
    public Iterable<ItemStack> getCraftingInput() {
        return RecipeHelpers.getCraftingInput();
    }
}
