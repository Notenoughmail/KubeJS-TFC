package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.*;
import dev.latvian.mods.kubejs.bindings.event.PlayerEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.Extra;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.DispenserBehaviors;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventHandlers {

    public static final EventGroup TFCEvents = EventGroup.of("TFCEvents");

    public static final EventHandler data = TFCEvents.server("data", () -> TFCDataEventJS.class);
    public static final EventHandler worldgenData = TFCEvents.server("worldgenData", () -> TFCWorldgenDataEventJS.class);
    public static final EventHandler limitContainer = TFCEvents.server("limitContainer", () -> ContainerLimiterEventJS.class).extra(PlayerEvents.SUPPORTS_MENU_TYPE.copy().required());

    public static void init() {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(EventPriority.LOWEST, EventHandlers::onSelectClimateModel);
        bus.addListener(EventHandlers::limitContainers);

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(EventHandlers::commonSetup);
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
        event.enqueueWork(() -> {
            RegistryUtils.hackBlockEntities();
            BuilderRefs.fluidContainerDispenser.forEach(b -> DispenserBlock.registerBehavior(b.get(), DispenserBehaviors.TFC_BUCKET_BEHAVIOR));
        });
    }
}