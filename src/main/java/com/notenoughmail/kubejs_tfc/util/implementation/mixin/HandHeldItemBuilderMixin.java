package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HandheldItemBuilder.class)
public abstract class HandHeldItemBuilderMixin {

    @Shadow public transient float attackDamageBaseline;

    @Shadow public transient MutableToolTier toolTier;

    @Shadow public abstract HandheldItemBuilder attackDamageBaseline(float f);

    @Unique
    public HandheldItemBuilder useTFCDamageCalculation() {
        return attackDamageBaseline(ToolItem.calculateVanillaAttackDamage(attackDamageBaseline, toolTier));
    }
}
