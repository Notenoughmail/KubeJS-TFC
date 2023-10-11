package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.architectury.registry.registries.Registrar;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class RegistrationUtils {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KubeJSTFC.MODID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static <T extends Item> RegistryObject<Item> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

    public static Registrar<ParticleType<?>> particleTypes() {
        return KubeJSRegistries.genericRegistry(Registry.PARTICLE_TYPE_REGISTRY);
    }

    public static ParticleOptions getOrLogErrorParticle(ResourceLocation particle, ParticleOptions fallback) {
        var nullableParticle = particleTypes().get(particle);
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

    public static List<Block> getBlockList() {
        NonNullList<Block> blockList = NonNullList.create();

        KubeJSRegistries.blocks().forEach(blockList::add);

        return blockList;
    }

    public static List<Fluid> getFluidList() {
        NonNullList<Fluid> fluidList = NonNullList.create();

        KubeJSRegistries.fluids().forEach(fluidList::add);

        return fluidList;
    }
}
