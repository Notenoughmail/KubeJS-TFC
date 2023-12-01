package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.item.FluidContainerItemBuilder;
import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandlers::registerItemColorHandlers);
    }

    // This doesn't work very well with pure white 16x16 images for both textures
    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        FluidContainerItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
        MoldItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
    }
}
