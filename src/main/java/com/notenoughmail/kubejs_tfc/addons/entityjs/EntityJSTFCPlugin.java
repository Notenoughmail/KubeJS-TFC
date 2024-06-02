package com.notenoughmail.kubejs_tfc.addons.entityjs;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.MammalJSBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;

public class EntityJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        EventHandlers.init();

        RegistryInfo.ENTITY_TYPE.addType("tfc:mammal", MammalJSBuilder.class, MammalJSBuilder::new);
    }
}
