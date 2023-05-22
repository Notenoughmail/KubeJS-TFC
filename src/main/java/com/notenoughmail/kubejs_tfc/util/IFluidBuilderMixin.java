package com.notenoughmail.kubejs_tfc.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface IFluidBuilderMixin {

    ResourceLocation getBubbleParticle();
    ResourceLocation getSteamParticle();
    float getHealAmount();
    boolean hasBubbleParticle();
    boolean hasSteamParticle();
}
