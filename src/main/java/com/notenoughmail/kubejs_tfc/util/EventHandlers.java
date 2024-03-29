package com.notenoughmail.kubejs_tfc.util;

import com.mojang.datafixers.util.Pair;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.LampBlockJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.*;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.latvian.mods.kubejs.bindings.event.PlayerEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.Extra;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.util.events.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final EventHandler rockSettings = TFCEvents.startup("rockSettings", () -> RockSettingsEventJS.class);
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static final EventHandler limitContainerSize = TFCEvents.startup("limitContainerSize", () -> LegacyContainerLimiterEventJS.class);
    public static final EventHandler registerClimateModel = TFCEvents.startup("registerClimateModel", () -> RegisterClimateModelEventJS.class);
    public static final EventHandler registerFoodTrait = TFCEvents.startup("registerFoodTrait", () -> RegisterFoodTraitEventJS.class);
    public static final EventHandler birthdays = TFCEvents.startup("birthdays", () -> BirthdayEventJS.class);
    public static final EventHandler registerModifiers = TFCEvents.startup("registerItemStackModifier", () -> RegisterItemStackModifierEventJS.class);
    public static final EventHandler representatives = TFCEvents.startup("prospectRepresentative", () -> RegisterRepresentativeBlocksEventJS.class);

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

    @Deprecated(forRemoval = true, since = "1.1.0")
    private static void setupEvents() {
        if (limitContainerSize.hasListeners()) {
            final String warning = "Usage of the legacy container limiter is deprecated! Please use the server TFCEvents.limitContainer event instead!";
            KubeJSTFC.LOGGER.warn(warning);
            ConsoleJS.STARTUP.warn(warning);
            limitContainerSize.post(new LegacyContainerLimiterEventJS());
        }
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

        // Handle custom lamps and possibly future implementations
        final Level level = event.getLevel();
        final BlockPos pos = event.getPos();
        final BlockState state = level.getBlockState(pos);
        final Block block = state.getBlock();

        if (RegistryUtils.getLamp() != null) {
            if (block instanceof LampBlockJS) {
                level.getBlockEntity(pos, RegistryUtils.getLamp().get()).ifPresent(lamp -> {
                    if (lamp.getFuel() != null) {
                        level.setBlock(pos, state.setValue(LampBlock.LIT, true), 3);
                        lamp.resetCounter();
                    }
                });
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

    @Nullable
    public static Object postDataEvents(EventJS event) {
        if (event instanceof DataPackEventJS dataEvent) {
            if (data.hasListeners()) {
                data.post(new TFCDataEventJS(dataEvent));
            }
            if (worldgenData.hasListeners()) {
                worldgenData.post(new TFCWorldgenDataEventJS(dataEvent));
            }
        } else if (CommonConfig.debugMode.get()){
            KubeJSTFC.LOGGER.error("KubeJSTFC data events failed to post due to wrapped event not being an instanceof DataPackEventJS, somehow");
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

        final ResourceLocation menuName = RegistryInfo.MENU.getId(menuType);

        // TODO: 1.2.0 | Deprecated for removal
        if (event.getEntity() instanceof ServerPlayer player && LegacyContainerLimiterEventJS.LIMITED_SIZES.containsKey(menuName)) {
            ConsoleJS.SERVER.warn("KubeJS TFC: A legacy container limiter was used to limit a container! This form of limiting is deprecated, please use the new system"); // Dirty, but this implementation is actually awful
            Pair<Size, List<Pair<Integer, Integer>>> function = LegacyContainerLimiterEventJS.LIMITED_SIZES.get(menuName);

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
        event.enqueueWork(() -> {
            KubeJSTFC.LOGGER.info("KubeJS TFC configuration:");
            KubeJSTFC.LOGGER.info("    Debug mode enabled: {}", CommonConfig.debugMode.get());
            if (rockSettings.hasListeners()) {
                rockSettings.post(new RockSettingsEventJS()); // Fire after TFC (and hopefully anyone else) adds their layers
            }
            if (birthdays.hasListeners()) {
                birthdays.post(new BirthdayEventJS());
            }
        });
    }
}