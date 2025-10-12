package io.github.notenoughmail.kubejstfc.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.BuilderRefs;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.DataManagers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class KubeJSTFCEventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final TargetedEventHandler<String> createChunkDataProvider = TFCEvents.server("createChunkDataProvider", () -> KubeChunkDataProviderEvent.class).requiredTarget(EventTargetType.STRING);

    public static void init(IEventBus modBus) {
        modBus.addListener(KubeJSTFCEventHandlers::newRegistries);
        modBus.addListener(KubeJSTFCEventHandlers::commonSetup);
        modBus.addListener(KubeJSTFCEventHandlers::loadFinish);

        final IEventBus gameBus = NeoForge.EVENT_BUS;
    }

    // ===MOD BUS===
    private static void newRegistries(NewRegistryEvent event) {
        event.register(KubeJSTFCRegistries.DATA_TYPES);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
    }

    private static void loadFinish(FMLLoadCompleteEvent event) {
        event.enqueueWork(BuilderRefs::clear);
        if (!FMLLoader.isProduction()) {
            final Set<DataManager<?>> managers = KubeJSTFCRegistries.DATA_TYPES.stream()
                    .filter(DataTypes.DataManagerType.class::isInstance)
                    .map(DataTypes.DataManagerType.class::cast)
                    .<DataManager<?>>map(DataTypes.DataManagerType::manager)
                    .collect(Collectors.toSet());

            final Collection<ResourceLocation> unhandled = new HashSet<>();

            DataManagers.REGISTRY.stream().forEach(manager -> {
                if (!managers.contains(manager)) {
                    unhandled.add(DataManagers.REGISTRY.getKey(manager));
                }
            });

            if (!unhandled.isEmpty()) {
                throw new AssertionError("All DataManagers should be handled! Unhandled: %s".formatted(unhandled));
            }
        }
    }

    // ===GAME BUS===
}
