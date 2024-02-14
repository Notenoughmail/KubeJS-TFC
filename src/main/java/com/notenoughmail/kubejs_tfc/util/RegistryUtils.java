package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.block.entity.*;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistryUtils {

    private static final ResourceLocation tickCounterId = KubeJSTFC.identifier("tick_counter");
    private static TickCounterBlockEntityBuilder tickCounterBuilder;
    private static final ResourceLocation lampId = KubeJSTFC.identifier("lamp");
    private static LampBlockEntityBuilder lampBuilder;
    private static final ResourceLocation berryBushId = KubeJSTFC.identifier("berry_bush");
    private static BerryBushBlockEntityBuilder berryBushBuilder;
    private static final ResourceLocation farmlandId = KubeJSTFC.identifier("farmland");
    private static FarmlandBlockEntityBuilder farmlandBuilder;
    private static final ResourceLocation cropId = KubeJSTFC.identifier("crop");
    private static CropBlockEntityBuilder cropBuilder;

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
    public static TickCounterBlockEntityBuilder getTickCounter() {
        assert tickCounterBuilder != null;
        return tickCounterBuilder;
    }

    /**
     * Adds/creates a simple {@link TickCounterBlockEntityBuilder} to kube's builder list if not already present and adds the builder to its list of valid blocks, use {@link #getTickCounter()} to retrieve the tick counter
     * @param builder The block builder whose block will be added to the tick counter's valid blocks
     */
    public static void addTickCounter(BlockBuilder builder) {
        if  (tickCounterBuilder == null) {
            tickCounterBuilder = new TickCounterBlockEntityBuilder(tickCounterId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(tickCounterBuilder);
        }
        tickCounterBuilder.addBlock(builder);
    }

    public static LampBlockEntityBuilder getLamp() {
        assert lampBuilder != null;
        return lampBuilder;
    }

    public static void addLamp(BlockBuilder builder) {
        if (lampBuilder == null) {
            lampBuilder = new LampBlockEntityBuilder(lampId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(lampBuilder);
        }
        lampBuilder.addBlock(builder);
    }

    public static BerryBushBlockEntityBuilder getBerryBush() {
        assert berryBushBuilder != null;
        return berryBushBuilder;
    }

    public static void addBerryBush(BlockBuilder builder) {
        if (berryBushBuilder == null) {
            berryBushBuilder = new BerryBushBlockEntityBuilder(berryBushId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(berryBushBuilder);
        }
        berryBushBuilder.addBlock(builder);
    }

    public static FarmlandBlockEntityBuilder getFarmland() {
        assert farmlandBuilder != null;
        return farmlandBuilder;
    }

    public static void addFarmland(BlockBuilder builder) {
        if (farmlandBuilder == null) {
            farmlandBuilder = new FarmlandBlockEntityBuilder(farmlandId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(farmlandBuilder);
        }
        farmlandBuilder.addBlock(builder);
    }

    public static CropBlockEntityBuilder getCrop() {
        assert cropBuilder != null;
        return cropBuilder;
    }

    public static void addCrop(BlockBuilder builder) {
        if (cropBuilder == null) {
            cropBuilder = new CropBlockEntityBuilder(cropId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(cropBuilder);
        }
        cropBuilder.addBlock(builder);
    }
}
