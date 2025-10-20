package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;

public class TFCBindings {

    @Info("Miscellaneous TFC ingredient helpers")
    public static final IngredientBindings ingredient = IngredientBindings.INSTANCE;

    @Info("ISP helpers")
    public static final ISPBindings isp = ISPBindings.INSTANCE;

    @Info("Miscellaneous noise helpers")
    public static final NoiseBindings noise = NoiseBindings.INSTANCE;

    @Info("Miscellaneous TFC-related world(gen) helpers")
    public static final WorldBindings world = WorldBindings.INSTANCE;

    @Info("Helpers for getting information on and interacting with TFC's climate system")
    public static final ClimateBindings climate = ClimateBindings.INSTANCE;

    @Info("Helpers for getting and using calendar functions")
    public static final CalendarBindings calendar = CalendarBindings.INSTANCE;
}
