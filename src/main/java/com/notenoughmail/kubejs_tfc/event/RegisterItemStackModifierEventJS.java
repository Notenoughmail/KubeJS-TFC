package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.KubeJSItemStackModifier;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.minecraft.resources.ResourceLocation;

@Info("""
        Used to register custom stack modifiers which can be used with item stack providers
        """)
@SuppressWarnings("unused")
public class RegisterItemStackModifierEventJS extends EventJS {

    private void register(ResourceLocation id, KubeJSItemStackModifier.ModifierApplicator applicator, boolean dependsOnInput) {
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
}
