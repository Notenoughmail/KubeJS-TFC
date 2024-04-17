package com.notenoughmail.kubejs_tfc.util.internal;

import com.google.common.collect.ImmutableMap;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import net.minecraftforge.eventbus.api.Event;

public class AddNamedRegistryWoodEvent extends Event {

    private final ImmutableMap.Builder<String, NamedRegistryWood> builder;

    public AddNamedRegistryWoodEvent(ImmutableMap.Builder<String, NamedRegistryWood> builder) {
        this.builder = builder;
    }

    public void put(String woodName, NamedRegistryWood wood) {
        builder.put(woodName, wood);
    }
}
