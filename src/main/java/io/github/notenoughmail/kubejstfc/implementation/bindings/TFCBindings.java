package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;

public interface TFCBindings {

    @Info("Miscellaneous TFC ingredient helpers")
    IngredientBindings ingredient = IngredientBindings.INSTANCE;

    @Info("ItemStackProvider helpers")
    ISPBindings isp = ISPBindings.INSTANCE, itemStackProvider = ISPBindings.INSTANCE;

    @Info("Miscellaneous noise helpers")
    NoiseBindings noise = NoiseBindings.INSTANCE;

    @Info("Miscellaneous TFC-related worldgen helpers")
    WorldgenBindings worldgen = WorldgenBindings.INSTANCE;

    @Info("Helpers for getting information on and interacting with TFC's climate system")
    ClimateBindings climate = ClimateBindings.INSTANCE;

    @Info("Helpers for getting and using calendar functions")
    CalendarBindings calendar = CalendarBindings.INSTANCE;

    @Info("Miscellaneous TFC data getters and helpers")
    DataBindings data = DataBindings.INSTANCE;
}
