package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;

public class TFCBindings {

    @Info("Miscellaneous recipe helpers")
    public static final RecipeBindings recipe = RecipeBindings.INSTANCE;

    @Info("ISP helpers")
    public static final ISPBindings isp = ISPBindings.INSTANCE;

    @Info("Miscellaneous noise helpers")
    public static final NoiseBindings noise = NoiseBindings.INSTANCE;

    @Info("Miscellaneous TFC-related world(gen) helpers")
    public static final WorldBindings world = WorldBindings.INSTANCE;
}
