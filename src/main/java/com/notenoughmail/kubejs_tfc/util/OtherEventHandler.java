package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.util.implementation.containerlimiter.SemiFunctionalContainerLimiterEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.RockSettingsEventJS;
import dev.architectury.event.events.common.LifecycleEvent;

public class OtherEventHandler {

    public static void init() {
        LifecycleEvent.SETUP.register(OtherEventHandler::setupEvents);
    }

    private static void setupEvents() {
        new RockSettingsEventJS().post("rock_settings.register");
        new SemiFunctionalContainerLimiterEventJS().post("tfc.limit_container_size");
    }
}
