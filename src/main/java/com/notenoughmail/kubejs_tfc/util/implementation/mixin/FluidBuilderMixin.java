package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.block.fluid.HotWaterFluidBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.IFluidBuilderMixin;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

// TODO: move this to a separate builder type
@Mixin(value = FluidBuilder.class, remap = false)
public abstract class FluidBuilderMixin implements IFluidBuilderMixin {

    @Shadow(remap = false)
    public FluidBlockBuilder block;

    private transient ResourceLocation bubbleParticle;
    private transient ResourceLocation steamParticle;
    private transient float healingAmount;
    private transient boolean hasBubbles;
    private transient boolean hasSteam;

    @Unique
    public FluidBuilder hotWaterBlock() {
        block = new HotWaterFluidBlockBuilder((FluidBuilder) (Object) this);
        bubbleParticle = new ResourceLocation("minecraft", "bubble");
        steamParticle = new ResourceLocation("tfc", "steam");
        healingAmount = 0.08f;
        hasBubbles = true;
        hasSteam = true;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder bubbleParticle(ResourceLocation particle) {
        bubbleParticle = particle;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder steamParticle(ResourceLocation particle) {
        steamParticle = particle;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder setHealAmount(float f) {
        healingAmount = f;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder noBubbles() {
        hasBubbles = false;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder noSteam() {
        hasSteam = false;
        return (FluidBuilder) (Object) this;
    }

    @Override
    public ResourceLocation getBubbleParticle() {
        return bubbleParticle;
    }

    @Override
    public ResourceLocation getSteamParticle() {
        return steamParticle;
    }

    @Override
    public float getHealAmount() {
        return healingAmount;
    }

    @Override
    public boolean hasBubbleParticle() {
        return hasBubbles;
    }

    @Override
    public boolean hasSteamParticle() {
        return hasSteam;
    }
}
