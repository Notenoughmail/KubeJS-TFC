package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.jewey.rosia.event.ModEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ModEvents.class)
public abstract class RosiaModEventsMixin {

    /**
     * @author NotEnoughMail
     * @reason Currently, Rosia is broken on servers due to not checking client-ness, this is a hacky (unfortunate) temp fix to allow debug mode with its nice coloured log. This mixin will be removed once Rosia publishes the fix. This <strong> does not </strong> work outside dev environments
     */
    @Overwrite(remap = false)
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ModEvents::onPackFinder);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(ModEvents::onTextureStitch);
        }
    }
}
