package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.notenoughmail.kubejs_tfc.util.implementation.AdvancedKubeJSClimateModel;
import com.notenoughmail.kubejs_tfc.util.implementation.KubeJSClimateModel;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class RegisterClimateModelEventJS extends StartupEventJS {

    @HideFromJS
    public static final BiMap<ResourceLocation, ClimateModelType> CUSTOM_MODELS = HashBiMap.create(); // This exists purely to allow custom models to provide their type, so that TFC can get their name

    public void registerClimateModel(ResourceLocation name, Consumer<KubeJSClimateModel> model) {
        var climate = new KubeJSClimateModel(name);
        model.accept(climate);
        CUSTOM_MODELS.put(name, Climate.register(name, () -> climate));
    }

    public void registerAdvancedClimateModel(ResourceLocation name, Consumer<AdvancedKubeJSClimateModel> model) {
        var climate = new AdvancedKubeJSClimateModel(name);
        model.accept(climate);
        CUSTOM_MODELS.put(name, Climate.register(name, () -> climate));
    }
}