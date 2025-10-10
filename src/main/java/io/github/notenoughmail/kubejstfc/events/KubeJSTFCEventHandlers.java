package io.github.notenoughmail.kubejstfc.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class KubeJSTFCEventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");


    public static void init(IEventBus modBus) {
        modBus.addListener(KubeJSTFCEventHandlers::newRegistries);
        modBus.addListener(KubeJSTFCEventHandlers::commonSetup);

        final IEventBus gameBus = NeoForge.EVENT_BUS;
    }

    // ===MOD BUS===
    private static void newRegistries(NewRegistryEvent event) {
        event.register(KubeJSTFCRegistries.DATA_TYPES);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
    }

    // ===GAME BUS===
}
