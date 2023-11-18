package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

public class RegistryUtils {

    public static ParticleOptions getOrLogErrorParticle(ResourceLocation particle, ParticleOptions fallback) {
        final ParticleType<?> nullableParticle = RegistryInfo.PARTICLE_TYPE.getValue(particle);
        if (nullableParticle instanceof ParticleOptions options) {
            return options;
        }
        if (nullableParticle == null) {
            KubeJSTFC.LOGGER.error("The provided particle: '{}' does not exist!", particle);
        } else {
            KubeJSTFC.LOGGER.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions!", particle);
        }
        return fallback;
    }
}
