package com.notenoughmail.kubejs_tfc.util.helpers.ducks;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.items.ToolItem;

// Ensures the JSDoc survives mixing
@SuppressWarnings("unused")
public interface IKubeJSTFCHandHeldItemBuilderExtension {

    @Info("""
            Sets the tool's attack damage baseline using TFC's calculations.
            
            Note: This uses the tool's tier to calculate the value, set this after setting the tier
            
            See: attackDamageBaseline
         """)
    default HandheldItemBuilder attackDamageBaselineTFC(float f) {
        return kubejs_tfc$wrapAttackDamageBaseline(ToolItem.calculateVanillaAttackDamage(f, kubejs_tfc$getTier()));
    }

    @HideFromJS
    HandheldItemBuilder kubejs_tfc$wrapAttackDamageBaseline(float f);

    @HideFromJS
    MutableToolTier kubejs_tfc$getTier();
}
