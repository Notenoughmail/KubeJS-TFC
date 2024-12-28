package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.util.EntityDamageResistance;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EntityDamageResistance.class, remap = false)
public interface EntityDamageResistanceAccessor {

    @Accessor(value = "entity", remap = false)
    TagKey<EntityType<?>> kubejs_tfc$Entity();
}
