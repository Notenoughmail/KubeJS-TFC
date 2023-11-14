package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.dries007.tfc.common.items.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

// TODO: Ensure the JSDoc is kept through mixing
@Mixin(value = HandheldItemBuilder.class, remap = false)
public abstract class HandHeldItemBuilderMixin {

    @Shadow(remap = false)
    public transient float attackDamageBaseline;

    @Shadow(remap = false)
    public transient MutableToolTier toolTier;

    @Shadow(remap = false)
    public abstract HandheldItemBuilder attackDamageBaseline(float f);

    @Info("""
            Sets the tool's attack damage baseline using TFC's calculations.
            
            See: attackDamageBaseline
         """)
    @RemapForJS("attackDamageBaselineTFC")
    @Unique
    public HandheldItemBuilder kubejs_tfc$TFCDamageBaseLine(float f) {
        return attackDamageBaseline(ToolItem.calculateVanillaAttackDamage(f, toolTier));
    }
}
