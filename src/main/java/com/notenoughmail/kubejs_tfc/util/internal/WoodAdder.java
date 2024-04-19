package com.notenoughmail.kubejs_tfc.util.internal;

import com.google.common.collect.ImmutableMap;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;

public class WoodAdder {

    private final ImmutableMap.Builder<String, NamedRegistryWood> builder;

    public WoodAdder(ImmutableMap.Builder<String, NamedRegistryWood> builder) {
        this.builder = builder;
    }

    public void put(String woodName, NamedRegistryWood wood) {
        builder.put(woodName, wood);
    }
}
