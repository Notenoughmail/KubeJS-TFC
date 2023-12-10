package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;

public class RegisterFoodTraitEventJS extends StartupEventJS {

    public void registerTrait(float decayModifier, ResourceLocation id) {
        FoodTrait.register(id, new FoodTrait(decayModifier, null));
    }

    public void registerTraitWithTooltip(float decayModifier, ResourceLocation id) {
        FoodTrait.register(id, new FoodTrait(decayModifier, id.getNamespace() + ".tooltip.foodtrait." + id.getPath()));
    }
}
