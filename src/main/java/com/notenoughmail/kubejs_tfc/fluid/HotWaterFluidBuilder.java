package com.notenoughmail.kubejs_tfc.fluid;

import com.notenoughmail.kubejs_tfc.block.fluid.HotWaterFluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
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

    @Info(value = "Sets the liquid's bubble particle", params = @Param(name = "bubbleParticle", value = "The registry name of the particle type"))
    public HotWaterFluidBuilder bubbleParticle(ResourceLocation bubbleParticle) {
        this.bubbleParticle = bubbleParticle;
        return this;
    }

    @Info(value = "Sets the liquid's steam particle", params = @Param(name = "steamParticle", value = "The registry name of the particle type"))
    public HotWaterFluidBuilder steamParticle(ResourceLocation steamParticle) {
        this.steamParticle = steamParticle;
        return this;
    }

    @Info(value = "Sets the amount of health the liquid heals while a living entity is in it")
    public HotWaterFluidBuilder healingAmount(float healing) {
        healingAmount = healing;
        return this;
    }

    @Info(value = "Determines if the liquid gives off bubble particles")
    public HotWaterFluidBuilder hasBubbles(boolean b) {
        hasBubbles = b;
        return this;
    }

    @Info(value = "Determines if the liquid gives off steam particles")
    public HotWaterFluidBuilder hasSteam(boolean b) {
        hasSteam = b;
        return this;
    }
}
