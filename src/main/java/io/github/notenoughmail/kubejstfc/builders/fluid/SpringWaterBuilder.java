package io.github.notenoughmail.kubejstfc.builders.fluid;

import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.client.particle.TFCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpringWaterBuilder extends FluidBuilder {

    public transient Supplier<ParticleOptions> bubbleParticle, steamParticle;
    public transient float healingAmount;

    public SpringWaterBuilder(ResourceLocation i) {
        super(i);
        block = new HotWaterFluidBlockBuilder(this);
        bubbleParticle = () -> ParticleTypes.BUBBLE;
        steamParticle = TFCParticles.STEAM::get;
        healingAmount = 0.08f;
    }

    // TODO: 2.0.0 | This sucks
    @Info("Sets the liquid's bubble particle")
    public SpringWaterBuilder bubbleParticle(@Nullable Supplier<ParticleOptions> particle) {
        this.bubbleParticle = particle;
        return this;
    }

    @Info("Sets the liquid's steam particle")
    public SpringWaterBuilder steamParticle(@Nullable Supplier<ParticleOptions> particle) {
        this.steamParticle = particle;
        return this;
    }

    @Info("Sets the amount of health the liquid heals while a living entity is in it")
    public SpringWaterBuilder healingAmount(float healing) {
        healingAmount = healing;
        return this;
    }
}
