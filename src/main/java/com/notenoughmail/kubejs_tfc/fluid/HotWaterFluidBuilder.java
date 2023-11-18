package com.notenoughmail.kubejs_tfc.fluid;

import com.notenoughmail.kubejs_tfc.block.fluid.HotWaterFluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import net.minecraft.resources.ResourceLocation;

public class HotWaterFluidBuilder extends FluidBuilder {

    public transient ResourceLocation bubbleParticle;
    public transient ResourceLocation steamParticle;
    public transient float healingAmount;
    public transient boolean hasBubbles;
    public transient boolean hasSteam;

    public HotWaterFluidBuilder(ResourceLocation i) {
        super(i);
        block = new HotWaterFluidBlockBuilder(this);
        bubbleParticle = new ResourceLocation("minecraft", "bubble");
        steamParticle = new ResourceLocation("tfc", "steam");
        healingAmount = 0.08f;
        hasBubbles = true;
        hasSteam = true;
    }

    public HotWaterFluidBuilder bubbleParticle(ResourceLocation bubbleParticle) {
        this.bubbleParticle = bubbleParticle;
        return this;
    }

    public HotWaterFluidBuilder steamParticle(ResourceLocation steamParticle) {
        this.steamParticle = steamParticle;
        return this;
    }

    public HotWaterFluidBuilder healingAmount(float healing) {
        healingAmount = healing;
        return this;
    }

    public HotWaterFluidBuilder hasBubbles(boolean b) {
        hasBubbles = b;
        return this;
    }

    public HotWaterFluidBuilder hasSteam(boolean b) {
        hasSteam = b;
        return this;
    }
}
