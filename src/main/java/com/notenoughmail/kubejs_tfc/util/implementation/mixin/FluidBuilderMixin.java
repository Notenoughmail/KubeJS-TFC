package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.block.HotWaterFluidBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.IFluidBuilderMixin;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = FluidBuilder.class, remap = false)
public abstract class FluidBuilderMixin implements IFluidBuilderMixin {

    @Shadow public FluidBlockBuilder block;

    @Nullable
    private transient ResourceLocation bubbleParticle;
    @Nullable
    private transient ResourceLocation steamParticle;
    private transient float healingAmount;

    @Unique
    public FluidBuilder hotWaterBlock() {
        block = new HotWaterFluidBlockBuilder((FluidBuilder) (Object) this);
        healingAmount = 0.08f;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder setBubbleParticle(ResourceLocation particle) {
        bubbleParticle = particle;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder setSteamParticle(ResourceLocation particle) {
        steamParticle = particle;
        return (FluidBuilder) (Object) this;
    }

    @Unique
    public FluidBuilder setHealAmount(float f) {
        healingAmount = f;
        return (FluidBuilder) (Object) this;
    }

    @Override
    public @Nullable ResourceLocation getBubbleParticle() {
        return bubbleParticle;
    }

    @Override
    public @Nullable ResourceLocation getSteamParticle() {
        return steamParticle;
    }

    @Override
    public float getHealAmount() {
        return healingAmount;
    }
}
