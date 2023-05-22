package com.notenoughmail.kubejs_tfc.util;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface IFluidBuilderMixin {

    @Nullable
    ResourceLocation getBubbleParticle();
    @Nullable
    ResourceLocation getSteamParticle();
    float getHealAmount();
}
