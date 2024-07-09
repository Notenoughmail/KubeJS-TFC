package com.notenoughmail.kubejs_tfc.addons.powerfuljs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class PowerfulJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("TFCCapabilities", TFCCapabilities.class);
    }
}
