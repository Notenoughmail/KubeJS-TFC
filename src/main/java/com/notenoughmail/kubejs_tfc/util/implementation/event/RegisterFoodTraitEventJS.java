package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;

public class RegisterFoodTraitEventJS extends StartupEventJS {

    @Info(value = "registers a new food trait", params = {
            @Param(name = "decayModifier", value = "The decay modifier of the trait, a higher values means the food rots faster"),
            @Param(name = "id", value = "The registry id of the food trait")
    })
    public void registerTrait(float decayModifier, ResourceLocation id) {
        FoodTrait.register(id, new FoodTrait(decayModifier, null));
    }

    @Info(value = "registers a new food trait with a tooltip", params = {
            @Param(name = "decayModifier", value = "The decay modifier of the trait, a higher values means the food rots faster"),
            @Param(name = "id", value = "The registry id of the food trait")
    })
    public void registerTraitWithTooltip(float decayModifier, ResourceLocation id) {
        FoodTrait.register(id, new FoodTrait(decayModifier, id.getNamespace() + ".tooltip.foodtrait." + id.getPath()));
    }
}
