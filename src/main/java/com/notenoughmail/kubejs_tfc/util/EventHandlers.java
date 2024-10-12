package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.*;
import dev.latvian.mods.kubejs.bindings.event.PlayerEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import net.dries007.tfc.util.events.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final EventHandler rockSettings = TFCEvents.startup("rockSettings", () -> RockSettingsEventJS.class);
    public static final EventHandler registerClimateModel = TFCEvents.startup("registerClimateModel", () -> RegisterClimateModelEventJS.class);
    public static final EventHandler registerFoodTrait = TFCEvents.startup("registerFoodTrait", () -> RegisterFoodTraitEventJS.class);
    public static final EventHandler birthdays = TFCEvents.startup("birthdays", () -> BirthdayEventJS.class);
    public static final EventHandler registerModifiers = TFCEvents.startup("registerItemStackModifier", () -> RegisterItemStackModifierEventJS.class);
    public static final EventHandler representatives = TFCEvents.startup("prospectRepresentative", () -> RegisterRepresentativeBlocksEventJS.class);
    public static final EventHandler interactions = TFCEvents.startup("registerInteractions", () -> RegisterInteractionsEventJS.class);
    public static final EventHandler defaultSettings = TFCEvents.startup("defaultWorldSettings", () -> ModifyDefaultWorldGenSettingsEventJS.class);
    public static final EventHandler registerFaunas = TFCEvents.startup("registerFaunas", () -> RegisterFaunasEventJS.class);

    public static final EventHandler selectClimateModel = TFCEvents.server("selectClimateModel", () -> SelectClimateModelEventJS.class);
    public static final EventHandler startFire = TFCEvents.server("startFire", () -> StartFireEventJS.class).hasResult();
    public static final EventHandler prospect = TFCEvents.server("prospect", () -> ProspectedEventJS.class);
    public static final EventHandler log = TFCEvents.server("log", () -> LoggingEventJS.class).hasResult();
    public static final EventHandler animalProduct = TFCEvents.server("animalProduct", () -> AnimalProductEventJS.class).hasResult();
    public static final EventHandler collapse = TFCEvents.server("collapse", () -> CollapseEventJS.class);
    public static final EventHandler douseFire = TFCEvents.server("douseFire", () -> DouseFireEventJS.class).hasResult();
    public static final EventHandler data = TFCEvents.server("data", () -> TFCDataEventJS.class);
    public static final EventHandler worldgenData = TFCEvents.server("worldgenData", () -> TFCWorldgenDataEventJS.class);
    public static final EventHandler limitContainer = TFCEvents.server("limitContainer", () -> ContainerLimiterEventJS.class).extra(PlayerEvents.SUPPORTS_MENU_TYPE.copy().required());

    public static void init() {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(EventHandlers::onSelectClimateModel);
        bus.addListener(EventHandlers::onFireStart);
        bus.addListener(EventHandlers::onProspect);
        bus.addListener(EventHandlers::onLog);
        bus.addListener(EventHandlers::onAnimalProduct);
        bus.addListener(EventHandlers::limitContainers);
        bus.addListener(EventHandlers::onCollapse);
        bus.addListener(EventHandlers::onDouseFire);
        bus.addListener(EventHandlers::serverAboutToStart);

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(EventHandlers::commonSetup);
        modBus.addListener(EventHandlers::loadComplete);
    }

    // Guaranteed only server - provides a ServerLevel
    private static void onSelectClimateModel(SelectClimateModelEvent event) {
        if (selectClimateModel.hasListeners()) {
            selectClimateModel.post(new SelectClimateModelEventJS(event));
        }
    }

    private static void onFireStart(StartFireEvent event) {
        if (!event.getLevel().isClientSide() && startFire.hasListeners()) {
            if (startFire.post(new StartFireEventJS(event)).interruptFalse()) {
                event.setCanceled(true);
            }
        }
    }

    private static void onProspect(ProspectedEvent event) {
        if (!event.getPlayer().level().isClientSide() && prospect.hasListeners()) {
            prospect.post(new ProspectedEventJS(event));
        }
    }

    private static void onLog(LoggingEvent event) {
        if (event.getLevel() instanceof Level level && !level.isClientSide() && log.hasListeners()) {
            if (log.post(new LoggingEventJS(level, event.getPos(), event.getAxe())).interruptFalse()) {
                event.setCanceled(true);
            }
        }
    }

    private static void onAnimalProduct(AnimalProductEvent event) {
        if (!event.getLevel().isClientSide() && animalProduct.hasListeners()) {
            if (animalProduct.post(new AnimalProductEventJS(event)).interruptFalse()) {
                event.setCanceled(true);
            }
        }
    }

    // Guaranteed only server
    private static void onCollapse(CollapseEvent event) {
        if (collapse.hasListeners()) {
            collapse.post(new CollapseEventJS(event));
        }
    }

    private static void onDouseFire(DouseFireEvent event) {
        if (!event.getLevel().isClientSide() && douseFire.hasListeners()) {
            if (douseFire.post(new DouseFireEventJS(event)).interruptFalse()) {
                event.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("SameReturnValue")
    @Nullable
    public static Object postDataEvents(EventJS event) {
        if (event instanceof DataPackEventJS dataEvent) {
            if (data.hasListeners()) {
                data.post(new TFCDataEventJS(dataEvent));
            }
            if (worldgenData.hasListeners()) {
                worldgenData.post(new TFCWorldgenDataEventJS(dataEvent));
            }
        } else {
            KubeJSTFC.error("KubeJSTFC data events failed to post due to wrapped event not being an instanceof DataPackEventJS, somehow");
        }
        return null;
    }

    /**
     * The majority of this event's handling is based off of <i><a href="https://github.com/DoubleDoorDevelopment/OversizedItemInStorageArea">Oversized Item in Storage Area</a></i><br>
     * <i>Oversized Item in Storage Area</i> is licenced under the <a href="https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area/comments#license">BSD Licence</a>
     */
    private static void limitContainers(PlayerContainerEvent.Close event) {
        final AbstractContainerMenu container = event.getContainer();
        final MenuType<?> menuType;
        try {
            menuType = container.getType();
        } catch (UnsupportedOperationException ignored) {
            // Instead of returning null mojang throws an exception!
            return; // Do nothing as a menu is needed
        }

        if (limitContainer.hasListeners()) {
            final List<Slot> slotsToHandle = new ArrayList<>();
            for (Slot slot : container.slots) {
                if (!(slot.container instanceof Inventory)) {
                    slotsToHandle.add(slot);
                }
            }

            limitContainer.post(new ContainerLimiterEventJS(slotsToHandle, event.getEntity().level(), event.getEntity().getOnPos().above()), menuType);
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        if (registerFoodTrait.hasListeners()) {
            registerFoodTrait.post(new RegisterFoodTraitEventJS());
        }
        if (registerClimateModel.hasListeners()) {
            registerClimateModel.post(new RegisterClimateModelEventJS());
        }
        if (registerModifiers.hasListeners()) {
            registerModifiers.post(new RegisterItemStackModifierEventJS());
        }
        if (representatives.hasListeners()) {
            representatives.post(new RegisterRepresentativeBlocksEventJS());
        }
        if (interactions.hasListeners()) {
            interactions.post(new RegisterInteractionsEventJS());
        }
        event.enqueueWork(() -> {
            if (rockSettings.hasListeners()) {
                rockSettings.post(new RockSettingsEventJS()); // Fire after TFC (and hopefully anyone else) adds their layers
            }
            if (birthdays.hasListeners()) {
                birthdays.post(new BirthdayEventJS());
            }
            RegistryUtils.hackBlockEntities();
        });
    }

    private static void serverAboutToStart(ServerAboutToStartEvent event) {
        WorldGenUtils.worldgenHasBeenTransformed = false;
    }

    private static void loadComplete(FMLLoadCompleteEvent event) {
        if (registerFaunas.hasListeners()) {
            registerFaunas.post(new RegisterFaunasEventJS());
        }
    }
}