package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;

import javax.annotation.Nullable;

public class BuildFaunaData {

    private final PlacedFeatureProperties.Climate climate;
    @Nullable
    private Integer chance;
    @Nullable
    private Integer belowSeaLevel;
    @Nullable
    private Boolean solidGround;
    @Nullable
    private Integer brightness;

    public BuildFaunaData(PlacedFeatureProperties.Climate climate) {
        this.climate = climate;
    }

    public BuildFaunaData chance(int i) {
        this.chance = i;
        return this;
    }

    public BuildFaunaData distanceBelowSeaLevel(int i) {
        this.belowSeaLevel = i;
        return this;
    }

    public BuildFaunaData solidGround(boolean b) {
        this.solidGround = b;
        return this;
    }

    public BuildFaunaData maxBrightness(int i) {
        this.brightness = i;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("climate", climate.toJson());
        if (chance != null) {
            json.addProperty("chance", chance);
        }
        if (belowSeaLevel != null) {
            json.addProperty("distance_below_sea_level", belowSeaLevel);
        }
        if (solidGround != null) {
            json.addProperty("solid_ground", solidGround);
        }
        if (brightness != null) {
            json.addProperty("max_brightness", brightness);
        }
        return json;
    }
}
