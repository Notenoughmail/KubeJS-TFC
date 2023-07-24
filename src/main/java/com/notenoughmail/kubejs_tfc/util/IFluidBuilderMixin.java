package com.notenoughmail.kubejs_tfc.util;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

@HideFromJS
public interface IFluidBuilderMixin {

    ResourceLocation getBubbleParticle();
    ResourceLocation getSteamParticle();
    float getHealAmount();
    boolean hasBubbleParticle();
    boolean hasSteamParticle();
}
