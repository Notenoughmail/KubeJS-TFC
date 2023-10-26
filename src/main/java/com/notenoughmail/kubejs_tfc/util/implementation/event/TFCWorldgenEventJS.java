package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.*;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCWorldgenEventJS extends EventJS {

    private final DataJsonGenerator generator;

    public TFCWorldgenEventJS(DataJsonGenerator generator) {
        this.generator = generator;
    }

    public void buildTFCGeode(String name, Consumer<BuildGeodeProperties> geode, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildGeodeProperties();
        geode.accept(properties);
        generator.json(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        generator.json(DataUtils.placedFeatureName(name), place.toJson());
    }

    public void buildTFCBoulder(String name, Consumer<BuildBoulderProperties> boulder, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildBoulderProperties();
        boulder.accept(properties);
        generator.json(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        generator.json(DataUtils.placedFeatureName(name), place.toJson());
    }

    public void buildTFCThinSpike(String name, Consumer<BuildThinSpikeProperties> spike, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildThinSpikeProperties();
        spike.accept(properties);
        generator.json(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        generator.json(DataUtils.placedFeatureName(name), place.toJson());
    }

    public void buildTFCVein(String name, Consumer<BuildVeinProperties> vein) {
        var properties = new BuildVeinProperties(name);
        vein.accept(properties);
        generator.json(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        generator.json(DataUtils.placedFeatureName(name), place.toJson());
    }

    public void buildTFCIfThen(String name, String if_, String then, Consumer<PlacedFeatureProperties> placement) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:if_then");
        var config = new JsonObject();
        config.addProperty("if", if_);
        config.addProperty("then", then);
        json.add("config", config);
        generator.json(DataUtils.configuredFeatureName(name), json);
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        generator.json(DataUtils.placedFeatureName(name), place.toJson());
    }
}
