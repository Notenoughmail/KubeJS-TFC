package com.notenoughmail.kubejs_tfc.util.internal;

import com.google.common.collect.ImmutableMap;
import net.dries007.tfc.util.registry.RegistryRock;

public class RockAdder {

    private final ImmutableMap.Builder<String, RegistryRock> builder;

    public RockAdder(ImmutableMap.Builder<String, RegistryRock> builder) {
        this.builder = builder;
    }

    public void put(String name, RegistryRock rock) {
        builder.put(name, rock);
    }
}
