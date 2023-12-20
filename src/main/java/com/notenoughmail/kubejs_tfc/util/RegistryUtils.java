package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.block.entity.TickCounterBlockEntityBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegistryUtils {

    private static final ResourceLocation tickCounterId = new ResourceLocation(KubeJSTFC.MODID, "tick_counter");
    private static TickCounterBlockEntityBuilder tickCounterBuilder;

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

    /**
     * Only use this for blocks which use the simple tick counter and call {@link #addTickCounter(BlockBuilder)} in {@link dev.latvian.mods.kubejs.registry.BuilderBase#createAdditionalObjects() createAdditionalObjects()}
     * @return The {@link TickCounterBlockEntityBuilder} which can be passed to {@link net.dries007.tfc.common.blocks.ExtendedProperties#blockEntity(Supplier) ExtendedProperties#blockEntity(Supplier)}
     */
    public static Supplier<? extends BlockEntityType<?>> getTickCounter() {
        assert tickCounterBuilder != null;
        return tickCounterBuilder;
    }

    // This assists block builders in not creating a block entity type per block made as its really not needed for something so simple

    /**
     * Adds/creates a simple {@link TickCounterBlockEntityBuilder} to kube's builder list if not already present and adds the builder to its list of valid blocks, use {@link #getTickCounter()} to retrieve the tick counter
     * @param builder The block builder whose block will be added to the tick counter's valid blocks
     */
    public static void addTickCounter(BlockBuilder builder) {
        if  (tickCounterBuilder == null) {
            tickCounterBuilder = new TickCounterBlockEntityBuilder(tickCounterId);
        }
        if (!RegistryInfo.BLOCK_ENTITY_TYPE.objects.containsKey(tickCounterId)) {
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(tickCounterBuilder);
        }
        tickCounterBuilder.addBlock(builder);
    }
}
