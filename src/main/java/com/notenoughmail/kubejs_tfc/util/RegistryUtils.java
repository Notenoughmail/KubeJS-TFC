package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.block.AnvilBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.entity.*;
import com.notenoughmail.kubejs_tfc.menu.AnvilMenuBuilder;
import com.notenoughmail.kubejs_tfc.menu.AnvilPlanMenuBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

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
    private static final ResourceLocation anvilId = KubeJSTFC.identifier("anvil");
    private static final ResourceLocation anvilPlanId = KubeJSTFC.identifier("anvil_plan");
    private static AnvilBlockEntityBuilder anvilBuilder;
    private static AnvilMenuBuilder anvilMenuBuilder;
    private static AnvilPlanMenuBuilder anvilPlanMenuBuilder;

    public static ParticleOptions getOrLogErrorParticle(ResourceLocation particle, ParticleOptions fallback) {
        final ParticleType<?> nullableParticle = RegistryInfo.PARTICLE_TYPE.getValue(particle);
        if (nullableParticle instanceof ParticleOptions options) {
            return options;
        }
        if (nullableParticle == null) {
            KubeJSTFC.error("The provided particle: '{}' does not exist!", particle);
        } else {
            KubeJSTFC.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions!", particle);
        }
        return fallback;
    }

    public static TickCounterBlockEntityBuilder getTickCounter() {
        assert tickCounterBuilder != null;
        return tickCounterBuilder;
    }

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

    public static boolean anvilPresent() {
        return anvilBuilder != null;
    }

    public static AnvilBlockEntityBuilder getAnvil() {
        assert anvilBuilder != null;
        return anvilBuilder;
    }

    public static AnvilMenuBuilder getAnvilMenu() {
        assert anvilMenuBuilder != null;
        return anvilMenuBuilder;
    }

    public static AnvilPlanMenuBuilder getAnvilPlanMenu() {
        assert anvilPlanMenuBuilder != null;
        return anvilPlanMenuBuilder;
    }

    // This does a whole lotta heavy lifting
    public static void addAnvil(AnvilBlockBuilder builder) {
        if (anvilBuilder == null) {
            anvilBuilder = new AnvilBlockEntityBuilder(anvilId);
            anvilMenuBuilder = new AnvilMenuBuilder(anvilId);
            anvilPlanMenuBuilder = new AnvilPlanMenuBuilder(anvilPlanId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(anvilBuilder);
            RegistryInfo.MENU.addBuilder(anvilMenuBuilder);
            RegistryInfo.MENU.addBuilder(anvilPlanMenuBuilder);
        }
        anvilBuilder.addBlock(builder);
    }
}
