package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.BuildVeinProperties;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;
import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.weightedBlockState;

/**
 * TODO: [Future] more of TFC's types
 * <ul>
 *     <li>{@link net.dries007.tfc.world.feature.TFCFeatures#FISSURE}</li>
 *     <li>{@link net.dries007.tfc.world.feature.TFCFeatures#FOREST} & 'children'</li>
 *     <li>More?</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class TFCWorldgenDataEventJS extends EventJS {

    private final DataPackEventJS wrappedEvent;

    public TFCWorldgenDataEventJS(DataPackEventJS wrapped) {
        wrappedEvent = wrapped;
    }

    @HideFromJS
    public void addJson(ResourceLocation id, JsonElement json) {
        if (CommonConfig.debugMode.get()) {
            KubeJSTFC.LOGGER.warn(id.toString());
            KubeJSTFC.LOGGER.info(json.toString());
        }
        wrappedEvent.addJson(id, json);
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
        final JsonObject config = new JsonObject();
        config.add("outer", blockStateToLenient(outer));
        config.add("middle", blockStateToLenient(middle));
        final JsonArray innerArray = new JsonArray(innerValues.size());
        for (String inner : innerValues) {
            innerArray.add(weightedBlockState(inner, "data"));
        }
        config.add("inner", innerArray);

        finishFeature("tfc:geode", name, config, placement);
    }

    @Info(value = "Creates a boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BlockToBlockStatesMapEntry.class, PlacedFeatureProperties.class})
    public void boulder(String name, List<WorldGenUtils.BlockToBlockStatesMapEntry> states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:boulder", name, states, placement);
    }

    @Info(value = "Creates a baby boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the baby boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BlockToBlockStatesMapEntry.class, PlacedFeatureProperties.class})
    public void babyBoulder(String name, List<WorldGenUtils.BlockToBlockStatesMapEntry> states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:baby_boulder", name, states, placement);
    }

    private void boulder(String type, String name, List<WorldGenUtils.BlockToBlockStatesMapEntry> states, Consumer<PlacedFeatureProperties> placement) {
        final JsonArray statesArray = new JsonArray(states.size());
        states.forEach(entry -> statesArray.add(entry.toJson()));
        final JsonObject config = new JsonObject();
        config.add("states", statesArray);

        finishFeature(type, name, config, placement);
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
        final JsonObject config = new JsonObject();
        config.add("state", blockStateToLenient(state));
        config.addProperty("radius", radius);
        config.addProperty("tries", tries);
        config.addProperty("min_height", minHeight);
        config.addProperty("max_height", maxHeight);

        finishFeature("tfc:thin_spike", name, config, placement);
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
    @Generics(value = {WorldGenUtils.BlockToWeightedBlockStateMapEntry.class, BuildVeinProperties.Cluster.class, PlacedFeatureProperties.class})
    public void clusterVein(String name, List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int size, Consumer<BuildVeinProperties.Cluster> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Cluster cluster = new BuildVeinProperties.Cluster(replacementMap, rarity, density, minY, maxY, name, size);
        optionals.accept(cluster);

        finishFeature(name, cluster.toJson(), placement);
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
    @Generics(value = {WorldGenUtils.BlockToWeightedBlockStateMapEntry.class, BuildVeinProperties.Pipe.class, PlacedFeatureProperties.class})
    public void pipeVein(String name, List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int height, int radius, int minSkew, int maxSkew, int minSlant, int maxSlant, float sign, Consumer<BuildVeinProperties.Pipe> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Pipe pipe = new BuildVeinProperties.Pipe(replacementMap, rarity, density, minY, maxY, name, height, radius, minSkew, maxSkew, minSlant, maxSlant, sign);
        optionals.accept(pipe);

        finishFeature(name, pipe.toJson(), placement);
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
    @Generics(value = {WorldGenUtils.BlockToWeightedBlockStateMapEntry.class, BuildVeinProperties.Disc.class, PlacedFeatureProperties.class})
    public void discVein(String name, List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> replacementMap, int rarity, float density, int minY, int maxY, int size, int height, Consumer<BuildVeinProperties.Disc> optionals, Consumer<PlacedFeatureProperties> placement) {
        final BuildVeinProperties.Disc disc = new BuildVeinProperties.Disc(replacementMap, rarity, density, minY, maxY, name, size, height);
        optionals.accept(disc);

        finishFeature(name, disc.toJson(), placement);
    }

    @Info(value = "Creates a 'tfc:if_then' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "if_", value = "A placed feature id, that will always try to place"),
            @Param(name = "then", value = "A placed feature id, that will only place if the first feature is placed"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void ifThen(String name, String if_, String then, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        config.addProperty("if", if_);
        config.addProperty("then", then);

        finishFeature("tfc:if_then", name, config, placement);
    }

    @Info(value = "Creates a 'tfc:soil_disc' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "replacementMap", value = "A list of {block -> block state} objects in string form the define the disc's replacement map"),
            @Param(name = "minRadius", value = "The minimum radius of the soil disc"),
            @Param(name = "maxRadius", value = "The maximum radius of the soil disc"),
            @Param(name = "height", value = "How tall the soil disc should be"),
            @Param(name = "integrity", value = "A number, in the range [0, 1], the specifies the probability of any given block will place, may be null to specify the default value of 1"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BlockToBlockStateMapEntry.class, PlacedFeatureProperties.class})
    public void soilDisc(String name, List<WorldGenUtils.BlockToBlockStateMapEntry> replacementMap, int minRadius, int maxRadius, int height, @Nullable Float integrity, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        config.addProperty("min_radius", minRadius);
        config.addProperty("max_radius", maxRadius);
        config.addProperty("height", height);
        if (integrity != null) {
            config.addProperty("integrity", integrity);
        }
        final JsonArray states = new JsonArray(replacementMap.size());
        replacementMap.forEach(entry -> states.add(entry.toJson()));
        config.add("states", states);

        finishFeature("tfc:soil_disc", name, config, placement);
    }

    @Info(value = "Creates a 'tfc:hot_spring' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "wallState", value = "The block state to use for the hot spring, may be null to use the lowest rock layer rock"),
            @Param(name = "fluidState", value = "The fluid to fill the spring with, may be air"),
            @Param(name = "radius", value = "The approximate radius of the hot spring, in the range [1, 16]"),
            @Param(name = "allowUnderwater", value = "If the hot spring can generate underwater"),
            @Param(name = "replacesOnFluidContact", value = "A list of {block[] -> weighted blockstate[]} objects, the blocks to place if placed underwater, may be null"),
            @Param(name = "decoration", value = "A fissure decoration object, may be null to not have one present"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = {WorldGenUtils.BlockToWeightedBlockStateMapEntry.class, PlacedFeatureProperties.class})
    public void hotSpring(String name, @Nullable String wallState, String fluidState, int radius, boolean allowUnderwater, @Nullable List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> replacesOnFluidContact, @Nullable WorldGenUtils.FissureDecoration decoration, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        if (wallState != null) {
            config.add("wall_state", WorldGenUtils.blockStateToLenient(wallState));
        }
        config.add("fluid_state", WorldGenUtils.blockStateToLenient(fluidState));
        config.addProperty("radius", radius);
        config.addProperty("allow_underwater", allowUnderwater);
        if (replacesOnFluidContact != null) {
            final JsonArray fluidReplacementArray = new JsonArray(replacesOnFluidContact.size());
            replacesOnFluidContact.forEach(entry -> fluidReplacementArray.add(entry.toJson()));
            config.add("replaces_on_fluid_contact", fluidReplacementArray);
        }
        if (decoration != null) {
            config.add("decoration", decoration.toJson());
        }

        finishFeature("tfc:hot_spring", name, config, placement);
    }

    @Info(value = "Creates a 'minecraft:simple_block' configured feature and the matching placed feature, uses a SimpleStateProvider", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "blockState", value = "The string representation of a block state, the state to be placed"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void simpleBlockState(String name, String blockState, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        final JsonObject toPlace = new JsonObject();
        toPlace.addProperty("type", "minecraft:simple_state_provider");
        toPlace.add("state", WorldGenUtils.blockStateToLenient(blockState));
        config.add("to_place", toPlace);

        finishFeature("minecraft:simple_block", name, config, placement);
    }

    @Info(value = "Creates a 'minecraft:random_patch' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "tries", value = "How many times the feature should attempt to place, may be null to default to 128"),
            @Param(name = "xzSpread", value = "The horizontal spread of the patch, may be null to default to 7"),
            @Param(name = "ySpread", value = "The vertical spread of the patch, may be null to default to 3"),
            @Param(name = "feature", value = "The feature to attempt to place for the patch"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void randomPatch(String name, @Nullable Integer tries, @Nullable Integer xzSpread, @Nullable Integer ySpread, String feature, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        if (tries != null) {
            config.addProperty("tries", tries);
        }
        if (xzSpread != null) {
            config.addProperty("xz_spread", xzSpread);
        }
        if (ySpread != null) {
            config.addProperty("y_spread", ySpread);
        }
        config.addProperty("feature", feature);

        finishFeature("minecraft:random_patch", name, config, placement);
    }

    @Info(value = "Creates a 'tfc:tall_wild_crop' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof WildDoubleCropBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void tallWildCrop(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        config.addProperty("block", block);

        finishFeature("tfc:tall_wild_crop", name, config, placement);
    }

    @Info(value = "Creates a 'tfc:spreading_crop' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof WildSpreadingCropBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void spreadingCrop(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        config.addProperty("block", block);

        finishFeature("tfc:spreading_crop", name, config, placement);
    }

    @Info(value = "Creates a 'tfc:spreading_bush' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof SpreadingBushBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    public void spreadingBush(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject config = new JsonObject();
        config.addProperty("block", block);

        finishFeature("tfc:spreading_bush", name, config, placement);
    }

    @Info(value = "Creates a configured feature of the given type with the given config and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "type", value = "The type of configured feature to create"),
            @Param(name = "featureConfig", value = "The config json object for the feature"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(value = PlacedFeatureProperties.class)
    public void generic(String name, String type, JsonObject featureConfig, Consumer<PlacedFeatureProperties> placement) {
        finishFeature(type, name, featureConfig, placement);
    }

    @Info(value = "Creates a new block to block state list map entry for use in boulder configured features", params = {
            @Param(name = "block", value = "The registry name of a block to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of a block state")
    })
    @Generics(value = String.class)
    public WorldGenUtils.BlockToBlockStatesMapEntry boulderState(String rock, List<String> blockStates) {
        return new WorldGenUtils.BlockToBlockStatesMapEntry(rock, blockStates);
    }

    @Info(value = """
            Creates a new block list to weighted block state list map entry for use in vein configured features
            
            Deprecated in favor of the generic blockToWeightedBlockState method
            """, params = {
            @Param(name = "blocks", value = "A list of strings, the registry names of blocks to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of weighted block states")
    })
    @Generics(value = {String.class, String.class})
    @Deprecated(since = "1.1.0", forRemoval = true)
    public WorldGenUtils.BlockToWeightedBlockStateMapEntry veinReplacement(List<String> replace, List<String> with) {
        return blockToWeightedBlockState(replace, with);
    }

    @Info(value = "Creates a new block list to weighted block state list map entry for use several configured features", params = {
            @Param(name = "blocks", value = "A list of strings, the registry names of blocks to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of weighted block states")
    })
    @Generics(value = {String.class, String.class})
    public WorldGenUtils.BlockToWeightedBlockStateMapEntry blockToWeightedBlockState(List<String> replace, List<String> with) {
        return new WorldGenUtils.BlockToWeightedBlockStateMapEntry(replace, with);
    }

    @Info(value = "Creates a new block to block state map entry for use in soil disc configured features", params = {
            @Param(name = "block", value = "The registry name of a block to be replaced"),
            @Param(name = "state", value = "A string representation of a block state")
    })
    public WorldGenUtils.BlockToBlockStateMapEntry blockToBlockState(String block, String state) {
        return new WorldGenUtils.BlockToBlockStateMapEntry(block, state);
    }

    @Info(value = "Creates a new fissure decoration object", params = {
            @Param(name = "replacementMap", value = "A list of {block[] -> weighted blockstate[]} objects, the additional 'ores' that should spawn around the fissure"),
            @Param(name = "rarity", value = "The rarity that blocks should be replaced with decoration blocks"),
            @Param(name = "radius", value = "The radius around the fissure that blocks should be replaced"),
            @Param(name = "count", value = "The number of blocks that should be placed, actual amount will be `count / rarity`")
    })
    @Generics(WorldGenUtils.BlockToWeightedBlockStateMapEntry.class)
    public WorldGenUtils.FissureDecoration fissureDecoration(List<WorldGenUtils.BlockToWeightedBlockStateMapEntry> replacementMap, int rarity, int radius, int count) {
        return new WorldGenUtils.FissureDecoration(replacementMap, rarity, rarity, count);
    }

    private void finishFeature(String name, JsonObject configuredFeature, Consumer<PlacedFeatureProperties> placement) {
        addJson(DataUtils.configuredFeatureName(name), configuredFeature);

        addJson(DataUtils.placedFeatureName(name), Util.make(new PlacedFeatureProperties(name), placement).toJson());
    }

    private void finishFeature(String type, String name, JsonObject config, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.add("config", config);

        finishFeature(name, json, placement);
    }
}
