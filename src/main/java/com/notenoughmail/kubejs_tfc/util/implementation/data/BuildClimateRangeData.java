package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;

public class BuildClimateRangeData {

    @Nullable
    private Integer minHydro;
    @Nullable
    private Integer maxHydro;
    @Nullable
    private Integer hydroWiggle;
    @Nullable
    private Integer minTemp;
    @Nullable
    private Integer maxTemp;
    @Nullable
    private Integer tempWiggle;

    public BuildClimateRangeData minHydration(int i) {
        minHydro = i;
        return this;
    }

    public BuildClimateRangeData maxHydration(int i) {
        maxHydro = i;
        return this;
    }

    public BuildClimateRangeData hydrationWiggle(int i) {
        hydroWiggle = i;
        return this;
    }

    public BuildClimateRangeData minTemperature(int i) {
        minTemp = i;
        return this;
    }

    public BuildClimateRangeData maxTemperature(int i) {
        maxTemp = i;
        return this;
    }

    public BuildClimateRangeData temperatureWiggle(int i) {
        tempWiggle = i;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        if (minHydro != null) {
            json.addProperty("min_hydration", minHydro);
        }
        if (maxHydro != null) {
            json.addProperty("max_hydration", maxHydro);
        }
        if (hydroWiggle != null) {
            json.addProperty("hydration_wiggle_range", hydroWiggle);
        }
        if (minTemp != null) {
            json.addProperty("min_temperature", minTemp);
        }
        if (maxTemp != null) {
            json.addProperty("max_temperature", maxTemp);
        }
        if (tempWiggle != null) {
            json.addProperty("temperature_wiggle_range", tempWiggle);
        }
        return json;
    }
}
