package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.item.FluidContainerItemBuilder;
import com.notenoughmail.kubejs_tfc.item.JavelinItemBuilder;
import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandlers::registerItemColorHandlers);
        bus.addListener(ClientEventHandlers::clientSetup);
    }

    // This doesn't work very well with pure white 16x16 images for both textures
    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        FluidContainerItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
        MoldItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
    }

    // TODO: Try fishing rods again, see if this fixes the issues I had
    private static void clientSetup(FMLClientSetupEvent event) {
        JavelinItemBuilder.thisList.forEach(builder -> {
            ItemProperties.register(builder.get(), Helpers.identifier("throwing"), (stack, level, entity, unused) ->
                    entity != null && ((entity.isUsingItem() && entity.getUseItem() == stack) || (entity instanceof Monster monster && monster.isAggressive())) ? 1.0F : 0.0F
            );
        });
    }
}
