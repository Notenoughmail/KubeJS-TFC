package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.NativeObject;

import java.util.ArrayList;
import java.util.List;

public class PlacedFeatureProperties {

    private final String feature;
    private final List<JsonObject> placements = new ArrayList<>();

    public PlacedFeatureProperties(String name) {
        feature = "kubejs_tfc:" + name;
    }

    public PlacedFeatureProperties placement(Object o) {
        if (o instanceof NativeObject obj) {
            placements.add(ListJS.orSelf(obj).toJson().get(0).getAsJsonObject());
        } else if (o instanceof JsonObject obj) {
            placements.add(obj);
        } else if (o instanceof MapJS map) {
            placements.add(map.toJson());
        } else if(o instanceof CharSequence) {
            var obj = new JsonObject();
            obj.addProperty("type", o.toString());
            placements.add(obj);
        }else if (o instanceof List<?> list) {
            var listJS = ListJS.orSelf(list);
            for (var element : listJS) {
                if (element instanceof NativeObject obj) {
                    placements.add(ListJS.orSelf(obj).toJson().get(0).getAsJsonObject());
                } else if (element instanceof JsonObject obj) {
                    placements.add(obj);
                } else if (element instanceof MapJS map) {
                    placements.add(map.toJson());
                } else if(element instanceof CharSequence) {
                    var obj = new JsonObject();
                    obj.addProperty("type", element.toString());
                    placements.add(obj);
                }
            }
        }
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("feature", feature);
        var array = new JsonArray();
        for (var obj : placements) {
            array.add(obj);
        }
        json.add("placement", array);
        return json;
    }
}
