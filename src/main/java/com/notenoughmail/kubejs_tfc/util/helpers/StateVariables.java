package com.notenoughmail.kubejs_tfc.util.helpers;

public class StateVariables {

    /**
     * If TFC's worldgen has been transformed, used to prevent {@code TFCEvents.defaultWorldSettings} from firing a
     * second time when the worlds saved settings are loaded from disk
     * <p>
     * This gets reset to false once Forge's {@link net.minecraftforge.event.server.ServerAboutToStartEvent ServerAboutToStart}
     * event fires
     */
    public static boolean worldgenHasBeenTransformed = false;
}
