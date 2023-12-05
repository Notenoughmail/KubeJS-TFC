package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.IKubeJSTFCHandHeldItemBuilderExtension;
import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HandheldItemBuilder.class, remap = false)
public abstract class HandHeldItemBuilderMixin implements IKubeJSTFCHandHeldItemBuilderExtension {

    @Shadow(remap = false)
    public transient MutableToolTier toolTier;

    @Shadow(remap = false)
    public abstract HandheldItemBuilder attackDamageBaseline(float f);

    @Override
    public HandheldItemBuilder kubejs_tfc$wrapAttackDamageBaseline(float f) {
        return attackDamageBaseline(f);
    }

    @Override
    public MutableToolTier kubejs_tfc$getTier() {
        return toolTier;
    }
}
