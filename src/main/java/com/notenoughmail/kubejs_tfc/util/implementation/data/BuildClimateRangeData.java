package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.typings.Info;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BuildClimateRangeData {

    @Nullable
    private Integer minHydro;
    @Nullable
    private Integer maxHydro;
    @Nullable
    private Integer hydroWiggle;
    @Nullable
    private Float minTemp;
    @Nullable
    private Float maxTemp;
    @Nullable
    private Float tempWiggle;

    @Info("Sets the minimum hydration of the climate range")
    public BuildClimateRangeData minHydration(int i) {
        minHydro = i;
        return this;
    }

    @Info("Sets the maximum hydration of the climate range")
    public BuildClimateRangeData maxHydration(int i) {
        maxHydro = i;
        return this;
    }

    @Info("Sets the wiggle range when determining if the hydration fits the hydration bounds, defaults to 0")
    public BuildClimateRangeData hydrationWiggle(int i) {
        hydroWiggle = i;
        return this;
    }

    @Info("Sets the minimum temperature of the climate range")
    public BuildClimateRangeData minTemperature(float f) {
        minTemp = f;
        return this;
    }

    @Info("Sets the maximum temperature of the climate range")
    public BuildClimateRangeData maxTemperature(float f) {
        maxTemp = f;
        return this;
    }

    @Info("Sets the wiggle range when determining if the temperature fits the temperature bounds, defaults to 0")
    public BuildClimateRangeData temperatureWiggle(float f) {
        tempWiggle = f;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        ResourceUtils.nullable(json, "min_hydration", minHydro);
        ResourceUtils.nullable(json, "max_hydration", maxHydro);
        ResourceUtils.nullable(json, "hydration_wiggle_range", hydroWiggle);
        ResourceUtils.nullable(json, "min_temperature", minTemp);
        ResourceUtils.nullable(json, "max_temperature", maxTemp);
        ResourceUtils.nullable(json, "temperature_wiggle_range", tempWiggle);
        return json;
    }
}
