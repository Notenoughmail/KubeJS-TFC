package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.util.implementation.containerlimiter.SemiFunctionalContainerLimiterEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.*;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.RockSettingsEventJS;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.dries007.tfc.util.events.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {

    public static void init() {
        LifecycleEvent.SETUP.register(EventHandler::setupEvents);

        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(EventHandler::onSelectClimateModel);
        bus.addListener(EventHandler::onFireStart);
        bus.addListener(EventHandler::onProspect);
        bus.addListener(EventHandler::onLog);
        bus.addListener(EventHandler::onAnimalProduct);
    }

    private static void setupEvents() {
        new RockSettingsEventJS().post("rock_settings.register");
        new RockSettingsEventJS().post("tfc.rock_settings.register");
        new SemiFunctionalContainerLimiterEventJS().post("tfc.limit_container_size");
    }

    private static void onSelectClimateModel(SelectClimateModelEvent event) {
        new SelectClimateModelEventJS(event).post(ScriptType.STARTUP, "tfc.select_climate_model");
    }

    private static void onFireStart(StartFireEvent event) {
        if (new StartFireEventJS(event).post(ScriptType.STARTUP, "tfc.start_fire")) {
            event.setCanceled(true);
        }
    }

    private static void onProspect(ProspectedEvent event) {
        new ProspectedEventJS(event).post(ScriptType.STARTUP, "tfc.prospect");
    }

    private static void onLog(LoggingEvent event) {
        if (new LoggingEventJS(event).post(ScriptType.STARTUP, "tfc.logging")) {
            event.setCanceled(true);
        }
    }

    private static void onAnimalProduct(AnimalProductEvent event) {
        if (new AnimalProductEventJS(event).post(ScriptType.STARTUP, "tfc.animal_product")) {
            event.setCanceled(true);
        }
    }
}