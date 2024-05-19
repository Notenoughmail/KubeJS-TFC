package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.custom.climate.AdvancedKubeJSClimateModel;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class RegisterClimateModelEventJS extends StartupEventJS {

    @HideFromJS
    public static final Map<ResourceLocation, ClimateModelType> CUSTOM_MODELS = new HashMap<>(); // This exists purely to allow custom models to provide their type, so that TFC can get their name

    private final ClimateModel overworld = new OverworldClimateModel();

    @Info(value = "Creates a new climate model with the given name and properties", params = {
            @Param(name = "name", value = "The name of the climate model"),
            @Param(name = "model", value = "A consumer for a KubeJSClimateModel")
    })
    @Generics(value = KubeJSClimateModel.class)
    public void registerClimateModel(ResourceLocation name, Consumer<KubeJSClimateModel> model) {
        var climate = new KubeJSClimateModel(name, overworld);
        model.accept(climate);
        CUSTOM_MODELS.put(name, Climate.register(name, () -> climate));
    }

    @Info(value = "Creates a new climate model with the given name and properties", params = {
            @Param(name = "name", value = "The name of the climate model"),
            @Param(name = "model", value = "A consumer for an AdvancedKubeJSClimateModel, which has access to the onWorldLoad and onChunkLoad methods")
    })
    @Generics(value = AdvancedKubeJSClimateModel.class)
    public void registerAdvancedClimateModel(ResourceLocation name, Consumer<AdvancedKubeJSClimateModel> model) {
        var climate = new AdvancedKubeJSClimateModel(name, overworld);
        model.accept(climate);
        CUSTOM_MODELS.put(name, Climate.register(name, () -> climate));
    }

    @Info(value = "Creates a new Vec2 with the given x and y values, used for creating custom wind vectors in your models", params = {
            @Param(name = "x", value = "The x component"),
            @Param(name = "z", value = "The y component")
    })
    public Vec2 newVec2(float x, float z) {
        return new Vec2(x, z);
    }

    @Info(value = "Returns the callback version of TFC's overworld current temperature calculation")
    public KubeJSClimateModel.TemperatureCallback getDefaultCurrentTemperatureCallback() {
        return overworld::getTemperature;
    }

    @Info(value = "Returns the callback version of TFC's overworld average temperature calculation")
    public KubeJSClimateModel.LevelPos2FloatCallback getDefaultAverageTemperatureCallback() {
        return overworld::getAverageTemperature;
    }

    @Info(value = "Returns the callback version of TFC's average rainfall calculation")
    public KubeJSClimateModel.LevelPos2FloatCallback getDefaultAverageRainfallCallback() {
        return overworld::getRainfall;
    }

    @Info(value = "Returns the callback version of TFC's air fog calculation")
    public KubeJSClimateModel.LevelPosLong2FloatCallback getDefaultAirFogCallback() {
        return overworld::getFogginess;
    }

    @Info(value = "Returns the callback version of TFC's water fog calculation")
    public KubeJSClimateModel.LevelPosLong2FloatCallback getDefaultWaterFogCallback() {
        return overworld::getWaterFogginess;
    }

    @Info(value = "Returns the callback version of TFC's wind vector calculation")
    public KubeJSClimateModel.WindVectorCallback getDefaultWindVectorCallback() {
        return (block, calendarTick) -> overworld.getWindVector(block.getLevel(), block.getPos(), calendarTick);
    }
}