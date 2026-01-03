package io.github.notenoughmail.kubejstfc.builders.fluid;

import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.block.HotWaterFluidBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.client.particle.TFCParticles;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ReturnsSelf
@SuppressWarnings("unused")
public class SpringWaterBuilder extends FluidBuilder {

    public transient Supplier<@Nullable ParticleOptions> bubbleParticle, steamParticle;
    public transient float healingAmount;

    public SpringWaterBuilder(ResourceLocation i) {
        super(i);
        block = new HotWaterFluidBlockBuilder(this);
        bubbleParticle = () -> ParticleTypes.BUBBLE;
        steamParticle = TFCParticles.STEAM::get;
        healingAmount = 0.08f;
    }

    @Info("Sets the liquid's bubble particle")
    public SpringWaterBuilder bubbleParticle(@Nullable Holder<ParticleType<?>> particle) {
        this.bubbleParticle = Assistant.getParticleOptions(particle);
        return this;
    }

    @Info("Sets the liquid's bubble particle")
    public SpringWaterBuilder fullBubbleParticle(Supplier<ParticleOptions> bubble) {
        bubbleParticle = Assistant.wrapParticleOptionsSafely(bubble);
        return this;
    }

    @Info("Sets the liquid's steam particle")
    public SpringWaterBuilder steamParticle(@Nullable Holder<ParticleType<?>> particle) {
        this.steamParticle = Assistant.getParticleOptions(particle);
        return this;
    }

    @Info("Sets the liquid's steam particle")
    public SpringWaterBuilder fullSteamParticle(Supplier<ParticleOptions> steam) {
        steamParticle = Assistant.wrapParticleOptionsSafely(steam);
        return this;
    }

    @Info("Sets the amount of health the liquid heals while a living entity is in it")
    public SpringWaterBuilder healingAmount(float healing) {
        healingAmount = healing;
        return this;
    }
}
