package com.notenoughmail.kubejs_tfc.fluid;

import com.notenoughmail.kubejs_tfc.block.fluid.HotWaterFluidBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.client.particle.TFCParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class HotWaterFluidBuilder extends FluidBuilder {

    public transient Supplier<Optional<ParticleOptions>> bubbleParticle, steamParticle;
    public transient float healingAmount;

    public HotWaterFluidBuilder(ResourceLocation i) {
        super(i);
        block = new HotWaterFluidBlockBuilder(this);
        bubbleParticle = Lazy.of(() -> Optional.of(ParticleTypes.BUBBLE));
        steamParticle = Lazy.of(() -> Optional.of(TFCParticles.STEAM.get()));
        healingAmount = 0.08f;
    }

    @Info(value = "Sets the liquid's bubble particle", params = @Param(name = "particle", value = "The registry name of the particle type, may be null"))
    public HotWaterFluidBuilder bubbleParticle(@Nullable ResourceLocation particle) {
        this.bubbleParticle = RegistryUtils.getParticleOrLogError(particle);
        return this;
    }

    @Info(value = "Sets the liquid's steam particle", params = @Param(name = "particle", value = "The registry name of the particle type, may be null"))
    public HotWaterFluidBuilder steamParticle(@Nullable ResourceLocation particle) {
        this.steamParticle = RegistryUtils.getParticleOrLogError(particle);
        return this;
    }

    @Info("Sets the amount of health the liquid heals while a living entity is in it")
    public HotWaterFluidBuilder healingAmount(float healing) {
        healingAmount = healing;
        return this;
    }

    @Deprecated(since = "1.3.0")
    @Info("Deprecated, please pass `null` to `#bubbleParticle` to disable bubble particles instead")
    public HotWaterFluidBuilder hasBubbles(boolean b) {
        return this;
    }

    @Deprecated(since = "1.3.0")
    @Info("Deprecated, please pass `null` to `#steamParticle` to disable steam particles instead")
    public HotWaterFluidBuilder hasSteam(boolean b) {
        return this;
    }
}
