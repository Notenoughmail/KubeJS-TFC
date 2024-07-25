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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class RegisterItemStackModifierEventJS extends EventJS {

    @Deprecated(forRemoval = true, since = "1.2.0")
    @Info(value = "Deprecated, please use `simple`, `withInput`, or `withInventory` to register a modifier instead. Creates a new item stack modifier with the given id and function", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function which will be applied when the modifier is applied to the stack"),
            @Param(name = "dependsOnInput", value = "Sets if the modifier is dependent on the input item, should be true if the input stack or the crafting container/inputs of the applicator is used")
    })
    public void register(ResourceLocation id, KubeJSItemStackModifier.ModifierApplicator applicator, boolean dependsOnInput) {
        ItemStackModifiers.register(id, new KubeJSItemStackModifier(id, applicator, dependsOnInput));
    }

    @Info(value = "Creates a new item stack modifier with the given id and function", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function that will be applied to the stack when the modifier is called")
    })
    public void simple(ResourceLocation id, KubeJSItemStackModifier.SimpleApplicator applicator) {
        register(id, applicator, false);
    }

    @Info(value = "Creates a new item stack modifier with the given id and function. Depends on the input item", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function that will be applied to the stack when the modifier is called")
    })
    public void withInput(ResourceLocation id, KubeJSItemStackModifier.ModifierApplicator applicator) {
        register(id, applicator, true);
    }

    @Info(value = "Creates a new item stack modifier with the given id and function. Depends on the input item. The inventory may be empty if the recipe type does not support it", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function that will be applied to the stack when the modifier is called")
    })
    public void withInventory(ResourceLocation id, KubeJSItemStackModifier.WithInventoryApplicator applicator) {
        register(id, applicator, true);
    }

    @Deprecated(forRemoval = true, since = "1.2.0")
    @Info(value = "Deprecated, use `withInventory` to register a modifier with access to the inventory instead. Returns a supplier for the current crafting container. Only available during advanced shaped and shapeless crafting recipes")
    @Generics(CraftingContainer.class)
    public Supplier<@Nullable CraftingContainer> getCraftingContainer() {
        return RecipeHelpers::getCraftingContainer;
    }
}
