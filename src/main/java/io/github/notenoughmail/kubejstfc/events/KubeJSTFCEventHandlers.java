package io.github.notenoughmail.kubejstfc.events;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.event.*;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.plugin.builtin.event.PlayerEvents;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import io.github.notenoughmail.kubejstfc.events.common.KubeCustomNutritionEvent;
import io.github.notenoughmail.kubejstfc.events.server.*;
import io.github.notenoughmail.kubejstfc.events.startup.*;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import net.dries007.tfc.common.capabilities.ItemCapabilities;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.util.data.DataManagers;
import net.dries007.tfc.util.events.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KubeJSTFCEventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    // SERVER
    public static final TargetedEventHandler<String> createChunkDataProvider = TFCEvents.server("createChunkDataProvider", () -> KubeChunkDataProviderEvent.class).requiredTarget(EventTargetType.STRING);
    public static final EventHandler animalProduct = TFCEvents.server("animalProduct", () -> KubeAnimalProductEvent.class);
    public static final EventHandler collapse = TFCEvents.server("collapse", () -> KubeCollapseEvent.class);
    public static final EventHandler douseFire = TFCEvents.server("douseFire", () -> KubeDouseFireEvent.class);
    public static final EventHandler log = TFCEvents.server("log", () -> KubeLogEvent.class);
    public static final EventHandler prospect = TFCEvents.server("prospect", () -> KubeProspectEvent.class);
    public static final EventHandler startFire = TFCEvents.server("startFire", () -> KubeStartFireEvent.class);
    public static final EventHandler selectClimateModel = TFCEvents.server("selectClimateModel", () -> KubeSelectClimateModelEvent.class);
    public static final TargetedEventHandler<ResourceKey<MenuType<?>>> limitContainers = TFCEvents.startup("limitContainers", () -> KubeLimitContainerEvent.class).requiredTarget(PlayerEvents.MENU_TARGET);
    public static final EventHandler data = TFCEvents.server("data", () -> KubeDataEvent.class);

    // STARTUP
    public static final EventHandler defaultWorldSettings = TFCEvents.startup("defaultWorldSettings", () -> KubeDefaultWorldSettingsEvent.class);
    public static final EventHandler registerInteractions = TFCEvents.startup("registerInteractions", () -> KubeRegisterInteractionsEvent.class);
    public static final EventHandler prospectRepresentatives = TFCEvents.startup("prospectRepresentatives", () -> KubeProspectRepresentativeEvent.class);
    public static final EventHandler faunaSpawns = TFCEvents.startup("faunaSpawns", () -> KubeFaunaSpawnsEvent.class);

    // COMMON
    public static final EventHandler customNutrition = TFCEvents.common("customNutrition", () -> KubeCustomNutritionEvent.class);

    public static void init(IEventBus modBus) {
        modBus.addListener(KubeJSTFCEventHandlers::newRegistries);
        modBus.addListener(KubeJSTFCEventHandlers::commonSetup);
        modBus.addListener(KubeJSTFCEventHandlers::loadFinish);
        modBus.addListener(EventPriority.LOWEST, KubeJSTFCEventHandlers::registerSpawnPlacements);
        modBus.addListener(KubeJSTFCEventHandlers::registerCapabilities);

        final IEventBus gameBus = NeoForge.EVENT_BUS;
        gameBus.addListener(KubeJSTFCEventHandlers::animalProduct);
        gameBus.addListener(KubeJSTFCEventHandlers::collapse);
        gameBus.addListener(KubeJSTFCEventHandlers::douseFire);
        gameBus.addListener(KubeJSTFCEventHandlers::log);
        gameBus.addListener(KubeJSTFCEventHandlers::prospect);
        gameBus.addListener(KubeJSTFCEventHandlers::startFire);
        gameBus.addListener(KubeJSTFCEventHandlers::selectClimateModel);
        gameBus.addListener(KubeJSTFCEventHandlers::closeContainer);
        gameBus.addListener(KubeJSTFCEventHandlers::openContainer);
        gameBus.addListener(KubeJSTFCEventHandlers::nutritionData);
    }

    // ===MOD BUS===
    private static void newRegistries(NewRegistryEvent event) {
        event.register(KubeJSTFCRegistries.DATA_TYPES);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        KubeRegisterInteractionsEvent.registerPlacements();
        if (prospectRepresentatives.hasListeners()) {
            prospectRepresentatives.post(new KubeProspectRepresentativeEvent());
        }
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

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        if (faunaSpawns.hasListeners()) {
            faunaSpawns.post(new KubeFaunaSpawnsEvent(event));
        }
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        final ItemLike[] molds = BuilderRefs.fluidContainers.stream()
                .filter(FluidCapacityItemBuilder::mold)
                .map(ItemBuilder::get)
                .toArray(ItemLike[]::new);
        final ItemLike[] notMolds = BuilderRefs.fluidContainers.stream()
                .filter(Predicate.not(FluidCapacityItemBuilder::mold))
                .map(ItemBuilder::get)
                .toArray(ItemLike[]::new);

        if (molds.length != 0) {
            event.registerItem(ItemCapabilities.MOLD, ItemCapabilities::forMold, molds);
            event.registerItem(ItemCapabilities.HEAT, ItemCapabilities::forMold, molds);
            event.registerItem(ItemCapabilities.FLUID, ItemCapabilities::forMold, molds);
        }

        if (notMolds.length != 0) {
            event.registerItem(ItemCapabilities.FLUID, ItemCapabilities::forBucket, notMolds);
        }
    }

    // ===GAME BUS===
    private static void animalProduct(AnimalProductEvent event) {
        if (!event.getLevel().isClientSide() && animalProduct.hasListeners()) {
            animalProduct.post(new KubeAnimalProductEvent(event)).applyCancel(event);
        }
    }

    // Guaranteed only server
    private static void collapse(CollapseEvent event) {
        if (collapse.hasListeners()) {
            collapse.post(new KubeCollapseEvent(event));
        }
    }

    private static void douseFire(DouseFireEvent event) {
        if (!event.getLevel().isClientSide() && douseFire.hasListeners()) {
            douseFire.post(new KubeDouseFireEvent(event)).applyCancel(event);
        }

        if (event.getState().getBlock() instanceof ICustomTorchBlock torch) {
            torch.handleFireDouse(event);
        }
    }

    private static void log(LoggingEvent event) {
        if (log.hasListeners() && event.getLevel() instanceof Level level) {
            log.post(new KubeLogEvent(level, event)).applyCancel(event);
        }
    }

    private static void prospect(ProspectedEvent event) {
        if (!event.getPlayer().level().isClientSide() && prospect.hasListeners()) {
            prospect.post(new KubeProspectEvent(event));
        }
    }

    private static void startFire(StartFireEvent event) {
        if (!event.getLevel().isClientSide() && startFire.hasListeners()) {
            startFire.post(new KubeStartFireEvent(event)).applyCancel(event);
        }

        if (event.getState().getBlock() instanceof ICustomTorchBlock torch) {
            torch.handleFireStart(event);
        }
    }

    private static void selectClimateModel(SelectClimateModelEvent event) {
        if (selectClimateModel.hasListeners()) {
            selectClimateModel.post(new KubeSelectClimateModelEvent(event));
        }
    }

    private static void closeContainer(PlayerContainerEvent.Close event) {
        limitContainer(event);
    }

    private static void openContainer(PlayerContainerEvent.Open event) {
        limitContainer(event);
    }

    /**
     * The majority of this event's handling is based off of <i><a href="https://github.com/DoubleDoorDevelopment/OversizedItemInStorageArea">Oversized Item in Storage Area</a></i><br>
     * <i>Oversized Item in Storage Area</i> is licenced under the <a href="https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area/comments#license">BSD Licence</a>
     */
    private static void limitContainer(PlayerContainerEvent event) {
        if (limitContainers.hasListeners()) {
            final AbstractContainerMenu container = event.getContainer();
            final MenuType<?> menuType;
            try {
                menuType = container.getType();
            } catch (Exception e) {
                // Instead of returning null mojang throws an exception!
                return; // Do nothing as a menu is needed
            }

            final List<Slot> slotsToHandle = container.slots
                    .stream()
                    .filter(s -> !(s.container instanceof Inventory))
                    .toList();

            limitContainers.post(new KubeLimitContainerEvent(
                    slotsToHandle,
                    event.getEntity().level(),
                    event.getEntity().getOnPos()
            ), menuType.kjs$getKey());
        }
    }

    private static void nutritionData(NutritionDataEvent event) {
        if (customNutrition.hasListeners()) {
            customNutrition.post(new KubeCustomNutritionEvent(event));
        }
    }
}
