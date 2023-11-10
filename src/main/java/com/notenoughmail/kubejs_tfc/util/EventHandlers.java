package com.notenoughmail.kubejs_tfc.util;

import com.mojang.datafixers.util.Pair;
import com.notenoughmail.kubejs_tfc.util.implementation.event.*;
import com.notenoughmail.kubejs_tfc.util.implementation.event.RockSettingsEventJS;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.util.events.*;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.List;

// Basically everything mentioned here requires a massive refactor
public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFC");

    public static final EventHandler registerRocks = TFCEvents.startup("registerRockSettings", () -> RockSettingsEventJS.class);
    public static final EventHandler limitContainerSize = TFCEvents.startup("limitContainerSize", () -> SemiFunctionalContainerLimiterEventJS.class);
    public static final EventHandler registerClimateModel = TFCEvents.startup("registerClimateModel", () -> RegisterClimateModelEventJS.class);
    public static final EventHandler registerFoodTrait = TFCEvents.startup("registerFoodTrait", () -> RegisterFoodTraitEventJS.class);

    public static final EventHandler selectClimateModel = TFCEvents.server("selectClimateModel", () -> SelectClimateModelEventJS.class);
    public static final EventHandler startFire = TFCEvents.server("startFire", () -> StartFireEventJS.class);
    public static final EventHandler prospect = TFCEvents.server("prospect", () -> ProspectedEventJS.class);
    public static final EventHandler log = TFCEvents.server("log", () -> LoggingEventJS.class);
    public static final EventHandler animalProduct = TFCEvents.server("animalProduct", () -> AnimalProductEventJS.class);
    public static final EventHandler collapse = TFCEvents.server("collapse", () -> CollapseEventJS.class);
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
    }

    private static void setupEvents() {
        new RockSettingsEventJS().post("tfc.rock_settings.register");
        new SemiFunctionalContainerLimiterEventJS().post("tfc.limit_container_size");
        new RegisterClimateModelEventJS().post("tfc.climate_model.register");
        new RegisterFoodTraitEventJS().post("tfc.food_trait.register");
    }

    // Guaranteed only server - provides a ServerLevel
    private static void onSelectClimateModel(SelectClimateModelEvent event) {
        new SelectClimateModelEventJS(event).post(ScriptType.SERVER, "tfc.climate_model.select");
    }

    private static void onFireStart(StartFireEvent event) {
        if (!event.getLevel().isClientSide() && new StartFireEventJS(event).post(ScriptType.SERVER, "tfc.start_fire")) {
            event.setCanceled(true);
        }
    }

    private static void onProspect(ProspectedEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            new ProspectedEventJS(event).post(ScriptType.SERVER, "tfc.prospect");
        }
    }

    private static void onLog(LoggingEvent event) {
        if (event.getLevel() instanceof Level level && !level.isClientSide()) {
            if (new LoggingEventJS(level, event.getPos(), event.getAxe()).post(ScriptType.SERVER, "tfc.logging")) {
                event.setCanceled(true);
            }
        }
    }

    private static void onAnimalProduct(AnimalProductEvent event) {
        if (!event.getLevel().isClientSide() && new AnimalProductEventJS(event).post(ScriptType.SERVER, "tfc.animal_product")) {
            event.setCanceled(true);
        }
    }

    // Guaranteed only server
    private static void onCollapse(CollapseEvent event) {
        new CollapseEventJS(event.getCenterPos(), event.getNextPositions(), event.getRadiusSquared(), event.getLevel(), event.isFake()).post(ScriptType.SERVER, "tfc.collapse");
    }

    public static void postDataEvents(VirtualKubeJSDataPack pack, MultiPackResourceManager manager) {
        if (pack != null && manager != null) {
            new TFCDataEventJS(pack, manager).post(ScriptType.SERVER, "tfc.data");
            new TFCWorldgenDataEventJS(pack, manager).post(ScriptType.SERVER, "tfc.worldgen.data");
        }
    }

    /**
     * The majority of this event's handling is based off of <i><a href="https://github.com/DoubleDoorDevelopment/OversizedItemInStorageArea">Oversized Item in Storage Area</a></i><br>
     * <i>Oversized Item in Storage Area</i> is licenced under the <a href="https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area/comments#license">BSD Licence</a>
     */
    private static void limitContainers(PlayerContainerEvent.Close event) {
        AbstractContainerMenu container = event.getContainer();
        MenuType<?> menuType;
        try {
            menuType = container.getType();
        } catch (UnsupportedOperationException ignored) {
            // Instead of returning null mojang throws an exception!
            return; // Do nothing as a menu is needed
        }

        if (event.getPlayer() instanceof ServerPlayer player && SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.containsKey(menuType.getRegistryName())) {
            Pair<Size, List<Pair<Integer, Integer>>> function = SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.get(menuType.getRegistryName());

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

            Level level = player.level;
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
}