package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.TFCWorldgenDataEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final EventHandler worldgenData = TFCEvents.server("worldgenData", () -> TFCWorldgenDataEventJS.class);

    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(EventHandlers::commonSetup);
    }

    @SuppressWarnings("SameReturnValue")
    @Nullable
    public static Object postDataEvents(EventJS event) {
        if (event instanceof DataPackEventJS dataEvent) {
            if (worldgenData.hasListeners()) {
                worldgenData.post(new TFCWorldgenDataEventJS(dataEvent));
            }
        } else {
            KubeJSTFC.error("KubeJSTFC data events failed to post due to wrapped event not being an instanceof DataPackEventJS, somehow");
        }
        return null;
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            RegistryUtils.hackBlockEntities();
        });
    }
}