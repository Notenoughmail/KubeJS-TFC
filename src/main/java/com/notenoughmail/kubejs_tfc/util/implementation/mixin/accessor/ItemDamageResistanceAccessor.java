package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.util.ItemDamageResistance;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemDamageResistance.class, remap = false)
public interface ItemDamageResistanceAccessor {

    @Accessor(value = "ingredient", remap = false)
    Ingredient kubejs_tfc$Ingredient();
}
