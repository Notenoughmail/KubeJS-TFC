package com.notenoughmail.kubejs_tfc.util;

import com.mojang.datafixers.util.Pair;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.implementation.event.*;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.util.events.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final EventHandler registerRocks = TFCEvents.startup("rockSettings", () -> RockSettingsEventJS.class);
    public static final EventHandler limitContainerSize = TFCEvents.startup("limitContainerSize", () -> SemiFunctionalContainerLimiterEventJS.class);
    public static final EventHandler registerClimateModel = TFCEvents.startup("registerClimateModel", () -> RegisterClimateModelEventJS.class);
    public static final EventHandler registerFoodTrait = TFCEvents.startup("registerFoodTrait", () -> RegisterFoodTraitEventJS.class);

    public static final EventHandler selectClimateModel = TFCEvents.server("selectClimateModel", () -> SelectClimateModelEventJS.class);
    public static final EventHandler startFire = TFCEvents.server("startFire", () -> StartFireEventJS.class);
    public static final EventHandler prospect = TFCEvents.server("prospect", () -> ProspectedEventJS.class);
    public static final EventHandler log = TFCEvents.server("log", () -> LoggingEventJS.class);
    public static final EventHandler animalProduct = TFCEvents.server("animalProduct", () -> AnimalProductEventJS.class);
    public static final EventHandler collapse = TFCEvents.server("collapse", () -> CollapseEventJS.class);
    public static final EventHandler douseFire = TFCEvents.server("douseFire", () -> DouseFireEventJS.class);
    public static final EventHandler data = TFCEvents.server("data", () -> TFCDataEventJS.class);
    public static final EventHandler worldgenData = TFCEvents.server("worldgenData", () -> TFCWorldgenDataEventJS.class);

    public static void init() {
        LifecycleEvent.SETUP.register(EventHandlers::setupEvents);

        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(EventHandlers::onSelectClimateModel);
        bus.addListener(EventHandlers::onFireStart);
        bus.addListener(EventHandlers::onProspect);
        bus.addListener(EventHandlers::onLog);
        bus.addListener(EventHandlers::onAnimalProduct);
        bus.addListener(EventHandlers::limitContainers);
        bus.addListener(EventHandlers::onCollapse);
        bus.addListener(EventHandlers::onDouseFire);

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(EventHandlers::loadComplete);
    }

    private static void setupEvents() {
        registerRocks.post(new RockSettingsEventJS());
        limitContainerSize.post(new SemiFunctionalContainerLimiterEventJS());
        registerClimateModel.post(new RegisterClimateModelEventJS());
        registerFoodTrait.post(new RegisterFoodTraitEventJS());
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

    public static void postDataEvents(VirtualKubeJSDataPack pack, MultiPackResourceManager manager) {
        if (pack != null && manager != null) {
            if (CommonConfig.debugMode.get()) {
                KubeJSTFC.LOGGER.info("Posting KubeJS TFC data events");
            }
            if (data.hasListeners()) {
                data.post(new TFCDataEventJS(pack, manager));
            }
            if (worldgenData.hasListeners()) {
                worldgenData.post(new TFCWorldgenDataEventJS(pack, manager));
            }
            if (CommonConfig.debugMode.get()) {
                KubeJSTFC.LOGGER.info("KubeJS TFC data events successfully posted");
            }
        } else {
            KubeJSTFC.LOGGER.warn("KubeJS TFC was unable to post its data events due to a failed mixin!");
        }
    }

    /**
     * The majority of this event's handling is based off of <i><a href="https://github.com/DoubleDoorDevelopment/OversizedItemInStorageArea">Oversized Item in Storage Area</a></i><br>
     * <i>Oversized Item in Storage Area</i> is licenced under the <a href="https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area/comments#license">BSD Licence</a>
     */
    // TODO: Investigate if there is a better way to do this now
    private static void limitContainers(PlayerContainerEvent.Close event) {
        AbstractContainerMenu container = event.getContainer();
        MenuType<?> menuType;
        try {
            menuType = container.getType();
        } catch (UnsupportedOperationException ignored) {
            // Instead of returning null mojang throws an exception!
            return; // Do nothing as a menu is needed
        }

        final ResourceLocation menuName = RegistryInfo.MENU.getId(menuType);

        if (event.getEntity() instanceof ServerPlayer player && SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.containsKey(menuName)) {
            Pair<Size, List<Pair<Integer, Integer>>> function = SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.get(menuName);

            // Filter slots to only the ones that have items and (if present) within the ranges given
            List<Slot> sanitizedSlots = new ArrayList<>();
            for (Slot slot : container.slots) { // This also gives the player's inventory slots because why not
                // Do NOT touch the player inventory
                if (!(slot.container instanceof Inventory)) {
                    sanitizedSlots.add(slot);
                }
            }
            List<Slot> filteredSlots = new ArrayList<>();
            if (function.getSecond().isEmpty()) {
                sanitizedSlots.forEach(slot -> {
                    if (slot.hasItem()) {
                        filteredSlots.add(slot);
                    }
                });
            } else {
                for (Pair<Integer, Integer> range : function.getSecond()) {
                    sanitizedSlots.subList(Math.max(0, range.getFirst()), Math.min(sanitizedSlots.size(), range.getSecond())).forEach(slot -> {
                        if (slot.hasItem()) {
                            filteredSlots.add(slot);
                        }
                    });
                }
            }

            Level level = player.level();
            BlockPos pos = player.getOnPos().above();
            for (Slot slot : filteredSlots) {
                ItemStack slotItem = slot.getItem();
                // If the item is bigger than the provided size, spit it out around the player
                if (!ItemSizeManager.get(slotItem).getSize(slotItem).isEqualOrSmallerThan(function.getFirst()) && !level.isClientSide()) {
                    float randX = level.random.nextFloat() * 0.8F;
                    float randY = level.random.nextFloat() * 0.8F + 0.3F;
                    float randZ = level.random.nextFloat() * 0.8F;
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX() + randX, pos.getY() + randY, pos.getZ() + randZ, slotItem);
                    itemEntity.setPickUpDelay(30);
                    level.addFreshEntity(itemEntity);
                    slot.set(ItemStack.EMPTY);
                    slot.setChanged();
                }
            }
        }
    }

    private static void loadComplete(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            KubeJSTFC.LOGGER.info("KubeJS TFC configuration:");
            KubeJSTFC.LOGGER.info("    Disable async recipes: {}", CommonConfig.disableAsyncRecipes.get());
            KubeJSTFC.LOGGER.info("    Debug mode enabled: {}", CommonConfig.debugMode.get());
        });
    }
}