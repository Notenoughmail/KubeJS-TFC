package io.github.notenoughmail.kubejstfc.util;

import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class KubeJSTFCEventHandlers {

    public static void init(IEventBus modBus) {
        modBus.addListener(KubeJSTFCEventHandlers::newRegistries);

        final IEventBus gameBus = NeoForge.EVENT_BUS;
    }

    private static void newRegistries(NewRegistryEvent event) {
        event.register(KubeJSTFCRegistries.DATA_TYPES);
    }
}
