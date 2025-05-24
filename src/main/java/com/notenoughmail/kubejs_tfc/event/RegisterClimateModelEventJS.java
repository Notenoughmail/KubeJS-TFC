package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.climate.KubeJSClimateModel;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Info("""
        Used to register/create new climate models which can be used during climate model selection
        """)
@SuppressWarnings("unused")
public class RegisterClimateModelEventJS extends StartupEventJS {

    // This exists purely to allow custom models to provide their type, so that TFC can get their name
    @HideFromJS
    public static final Map<ResourceLocation, ClimateModelType> CUSTOM_MODELS = new HashMap<>();

    @Info(value = "Creates a new climate model with the given name and properties", params = {
            @Param(name = "name", value = "The name of the climate model"),
            @Param(name = "model", value = "A consumer for a model builder")
    })
    @Generics(value = KubeJSClimateModel.Builder.class)
    public void register(ResourceLocation name, Consumer<KubeJSClimateModel.Builder> modelBuilder) {
        CUSTOM_MODELS.put(name, Climate.register(name, () -> Util.make(new KubeJSClimateModel.Builder(name), modelBuilder).build()));
    }
}