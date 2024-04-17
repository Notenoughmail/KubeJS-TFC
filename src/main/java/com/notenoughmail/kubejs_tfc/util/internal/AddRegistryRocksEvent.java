package com.notenoughmail.kubejs_tfc.util.internal;

import com.google.common.collect.ImmutableMap;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraftforge.eventbus.api.Event;

public class AddRegistryRocksEvent extends Event {

    private final ImmutableMap.Builder<String, RegistryRock> builder;

    public AddRegistryRocksEvent(ImmutableMap.Builder<String, RegistryRock> builder) {
        this.builder = builder;
    }

    public void put(String name, RegistryRock rock) {
        builder.put(name, rock);
    }
}
