package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackModifierJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import org.jetbrains.annotations.Nullable;

public class RegisterItemStackModifierEventJS extends EventJS {

    @Info(value = "Creates a new item stack modifier with the given id and function", params = {
            @Param(name = "id", value = "The registry name of the modifier"),
            @Param(name = "applicator", value = "The function which will be applied when the modifier is applied to the stack"),
            @Param(name = "dependsOnInput", value = "Sets if the modifier is dependent on the input item, should be true if the input stack of the applicator is used")
    })
    public void register(ResourceLocation id, ItemStackModifierJS.ModifierApplicator applicator, boolean dependsOnInput) {
        ItemStackModifiers.register(id, ItemStackModifierJS.create(id.toString(), applicator, dependsOnInput));
    }

    @Info(value = "Returns the current crafting container. Only available during advanced shaped and shapeless crafting recipes")
    @Nullable
    public CraftingContainer getCraftingContainer() {
        return RecipeHelpers.getCraftingContainer();
    }
}
