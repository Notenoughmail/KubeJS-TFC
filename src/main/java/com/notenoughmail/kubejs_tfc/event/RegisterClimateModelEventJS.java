package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.climate.KubeJSClimateModel;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.OverworldClimateModel;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Info("""
        Used to register/create new climate models which can be used during climate model selection
        """)
@SuppressWarnings("unused")
public class RegisterClimateModelEventJS extends StartupEventJS {

    @HideFromJS
    public static final Map<ResourceLocation, ClimateModelType> CUSTOM_MODELS = new HashMap<>(); // This exists purely to allow custom models to provide their type, so that TFC can get their name

    private static final ClimateModel overworld = new OverworldClimateModel();

    @Deprecated
    @Info(value = "Deprecated, use `.register`")
    public void registerClimateModel(ResourceLocation name, Consumer<KubeJSClimateModel.Builder> model) {
        register(name, model);
    }

    @Deprecated
    @Info(value = "Deprecated, use `.register`")
    public void registerAdvancedClimateModel(ResourceLocation name, Consumer<KubeJSClimateModel.Builder> model) {
        register(name, model);
    }

    @Info(value = "Creates a new climate model with the given name and properties", params = {
            @Param(name = "name", value = "The name of the climate model"),
            @Param(name = "model", value = "A consumer for a model builder")
    })
    @Generics(value = KubeJSClimateModel.Builder.class)
    public void register(ResourceLocation name, Consumer<KubeJSClimateModel.Builder> modelBuilder) {
        CUSTOM_MODELS.put(name, Climate.register(name, () -> Util.make(new KubeJSClimateModel.Builder(name), modelBuilder).build()));
    }

    @Deprecated
    public Vec2 newVec2(float x, float z) {
        return new Vec2(x, z);
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.TemperatureCallback getDefaultCurrentTemperatureCallback() {
        return overworld::getTemperature;
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.LevelPos2FloatCallback getDefaultAverageTemperatureCallback() {
        return overworld::getAverageTemperature;
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.LevelPos2FloatCallback getDefaultAverageRainfallCallback() {
        return overworld::getRainfall;
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.LevelPosLong2FloatCallback getDefaultAirFogCallback() {
        return overworld::getFogginess;
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.LevelPosLong2FloatCallback getDefaultWaterFogCallback() {
        return overworld::getWaterFogginess;
    }

    @Deprecated
    @Info(value = "Deprecated")
    public KubeJSClimateModel.WindVectorCallback getDefaultWindVectorCallback() {
        return overworld::getWindVector;
    }
}