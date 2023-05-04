package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.dries007.tfc.common.items.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HandheldItemBuilder.class)
public class HandHeldItemBuilderMixin {

    @Shadow public transient float attackDamageBaseline;

    @Shadow public transient MutableToolTier toolTier;

    @Unique
    public void useTFCDamageCalculation() { // This doesn't return a HandHeldItemBuilder which is troublesome, not sure how to amend that
        attackDamageBaseline = ToolItem.calculateVanillaAttackDamage(attackDamageBaseline, toolTier);
    }
}
