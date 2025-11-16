package com.notenoughmail.kubejs_tfc.util.client;

import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Predicate;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ClientEventHandlers::clientSetup);
    }

    @SuppressWarnings("deprecation")
    private static void clientSetup(FMLClientSetupEvent event) {
        final Predicate<RenderType> ghostBlock = rt -> rt == RenderType.cutoutMipped() || rt == Sheets.translucentCullBlockSheet();

        BuilderRefs.ghostRenders.forEach(builder -> ItemBlockRenderTypes.setRenderLayer(builder.get(), ghostBlock));
    }
}
