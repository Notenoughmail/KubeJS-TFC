package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.BuildVeinProperties;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;

import java.util.List;
import java.util.function.Consumer;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;
import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.weightedBlockState;

// TODO: More of TFC's types
@SuppressWarnings("unused")
public class TFCWorldgenDataEventJS extends DataPackEventJS {

    public TFCWorldgenDataEventJS(VirtualKubeJSDataPack pack, MultiPackResourceManager manager) {
        super(pack, manager);
    }

    @HideFromJS
    @Override
    public void add(ResourceLocation id, String content) {
        super.add(id, content);
    }

    @HideFromJS
    @Override
    public void addJson(ResourceLocation id, JsonElement json) {
        // KubeJSTFC.LOGGER.debug(id + " | " + json);
        super.addJson(id, json);
    }

    @Info(value = "Creates a geode configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "outer", value = "Sets the outer block state of the geode"),
            @Param(name = "middle", value = "Sets the middle block state of the geode"),
            @Param(name = "innerValues", value = "A list of weight block state in string form, sets the inner state of the geode"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {String.class, PlacedFeatureProperties.class})
    public void geode(String name, String outer, String middle, List<String> innerValues, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:geode");
        final JsonObject config = new JsonObject();
        config.add("outer", blockStateToLenient(outer));
        config.add("middle", blockStateToLenient(middle));
        final JsonArray innerArray = new JsonArray(innerValues.size());
        for (String inner : innerValues) {
            innerArray.add(weightedBlockState(inner, "data"));
        }
        config.add("inner", innerArray);
        json.add("config", config);
        addJson(DataUtils.configuredFeatureName(name), json);
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    // Baby boulder is identical to this, make sure this is actually correct
    @Info(value = "Creates a boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BoulderState.class, PlacedFeatureProperties.class})
    public void boulder(String name, List<WorldGenUtils.BoulderState> states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:boulder", name, states, placement);
    }

    @Info(value = "Creates a baby boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the baby boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BoulderState.class, PlacedFeatureProperties.class})
    public void babyBoulder(String name, List<WorldGenUtils.BoulderState> states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:baby_boulder", name, states, placement);
    }

    private void boulder(String type, String name, List<WorldGenUtils.BoulderState> states, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", type);
        final JsonArray statesArray = new JsonArray(states.size());
        states.forEach(entry -> statesArray.add(entry.toJson()));
        final JsonObject config = new JsonObject();
        config.add("states", statesArray);
        json.add("config", config);
        addJson(DataUtils.configuredFeatureName(name), json);
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a thin spike configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "state", value = "Sets the 'state' property of the modifier"),
            @Param(name = "radius", value = "Sets the 'radius' property of the modifier"),
            @Param(name = "tries", value = "Sets the 'tries' property of the modifier"),
            @Param(name = "minHeight", value = "Sets the 'min_height' property of the modifier"),
            @Param(name = "maxHeight", value = "Sets the 'max_height' property of the modifier"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void thinSpike(String name, String state, int radius, int tries, int minHeight, int maxHeight, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:thin_spike");
        final JsonObject config = new JsonObject();
        config.add("state", blockStateToLenient(state));
        config.addProperty("radius", radius);
        config.addProperty("tries", tries);
        config.addProperty("min_height", minHeight);
        config.addProperty("max_height", maxHeight);
        json.add("config", config);
        addJson(DataUtils.configuredFeatureName(name), json);
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a 'tfc:cluster_vein' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "replacementMap", value = "A list of {Block[] -> WeightedBlockState[]} objects in string form that define the vein's replacement map"),
            @Param(name = "rarity", value = "Sets the 'rarity' value of the vein"),
            @Param(name = "density", value = "Sets the 'density' value of the vein"),
            @Param(name = "minY", value = "Sets the 'min_y' value of the vein"),
            @Param(name = "maxY", value = "Sets the 'max_y' value of the vein"),
            @Param(name = "size", value = "Sets the 'size' value of the vein"),
            @Param(name = "optionals", value = "Sets the optional values of the vein through a consumer"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.VeinReplacementMapEntry.class, BuildVeinProperties.Cluster.class, PlacedFeatureProperties.class})
    public void clusterVein(String name, List<WorldGenUtils.VeinReplacementMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int size, Consumer<BuildVeinProperties.Cluster> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Cluster cluster = new BuildVeinProperties.Cluster(replacementMap, rarity, density, minY, maxY, name, size);
        optionals.accept(cluster);
        addJson(DataUtils.configuredFeatureName(name), cluster.toJson());
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a 'tfc:pipe_vein' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "replacementMap", value = "A list of {Block[] -> WeightedBlockState[]} objects in string form that define the vein's replacement map"),
            @Param(name = "rarity", value = "Sets the 'rarity' value of the vein"),
            @Param(name = "density", value = "Sets the 'density' value of the vein"),
            @Param(name = "minY", value = "Sets the 'min_y' value of the vein"),
            @Param(name = "maxY", value = "Sets the 'max_y' value of the vein"),
            @Param(name = "height", value = "Sets the 'height' value of the vein"),
            @Param(name = "radius", value = "Sets the 'radius' value of the vein"),
            @Param(name = "minSkew", value = "Sets the 'min_skew' value of the vein"),
            @Param(name = "maxSkew", value = "Sets the 'max_skew' value of the vein"),
            @Param(name = "minSlant", value = "Sets the 'min_slant' value of the vein"),
            @Param(name = "maxSlant", value = "Sets the 'max_slant' value of the vein"),
            @Param(name = "sign", value = "Sets the 'sign' value of the vein"),
            @Param(name = "optionals", value = "Sets the optional values of the vein through a consumer"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.VeinReplacementMapEntry.class, BuildVeinProperties.Pipe.class, PlacedFeatureProperties.class})
    public void pipeVein(String name, List<WorldGenUtils.VeinReplacementMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int height, int radius, int minSkew, int maxSkew, int minSlant, int maxSlant, float sign, Consumer<BuildVeinProperties.Pipe> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Pipe pipe = new BuildVeinProperties.Pipe(replacementMap, rarity, density, minY, maxY, name, height, radius, minSkew, maxSkew, minSlant, maxSlant, sign);
        optionals.accept(pipe);
        addJson(DataUtils.configuredFeatureName(name), pipe.toJson());
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a 'tfc:cluster_vein' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "replacementMap", value = "A list of {Block[] -> WeightedBlockState[]} objects in string form that define the vein's replacement map"),
            @Param(name = "rarity", value = "Sets the 'rarity' value of the vein"),
            @Param(name = "density", value = "Sets the 'density' value of the vein"),
            @Param(name = "minY", value = "Sets the 'min_y' value of the vein"),
            @Param(name = "maxY", value = "Sets the 'max_y' value of the vein"),
            @Param(name = "size", value = "Sets the 'size' value of the vein"),
            @Param(name = "height", value = "Sets the 'height' value of the vein"),
            @Param(name = "optionals", value = "Sets the optional values of the vein through a consumer"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.VeinReplacementMapEntry.class, BuildVeinProperties.Disc.class, PlacedFeatureProperties.class})
    public void discVein(String name, List<WorldGenUtils.VeinReplacementMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int size, int height, Consumer<BuildVeinProperties.Disc> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Disc disc = new BuildVeinProperties.Disc(replacementMap, rarity, density, minY, maxY, name, size, height);
        optionals.accept(disc);
        addJson(DataUtils.configuredFeatureName(name), disc.toJson());
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a 'tfc:if_then' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "if_", value = "A placed feature id, that will always try to place"),
            @Param(name = "then", value = "A placed feature id, that will only place if the first feature is placed"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void ifThen(String name, String if_, String then, Consumer<PlacedFeatureProperties> placement) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:if_then");
        var config = new JsonObject();
        config.addProperty("if", if_);
        config.addProperty("then", then);
        json.add("config", config);
        addJson(DataUtils.configuredFeatureName(name), json);
        final PlacedFeatureProperties place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @Info(value = "Creates a new block to block state list map entry for use in boulder configured features", params = {
            @Param(name = "block", value = "The registry name of a block to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of a block state")
    })
    @Generics(value = String.class)
    public WorldGenUtils.BoulderState boulderState(String rock, List<String> blockStates) {
        return new WorldGenUtils.BoulderState(rock, blockStates);
    }

    @Info(value = "Creates a new block list to weighted block state list map entry for use in vein configured features", params = {
            @Param(name = "blocks", value = "A list of strings, the registry names of blocks to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of a weighted block state")
    })
    @Generics(value = {String.class, String.class})
    public WorldGenUtils.VeinReplacementMapEntry veinReplacement(List<String> replace, List<String> with) {
        return new WorldGenUtils.VeinReplacementMapEntry(replace, with);
    }
}
