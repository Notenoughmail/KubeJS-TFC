package com.notenoughmail.kubejs_tfc.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IDataConstructor;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.BuildVeinProperties;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.blockStateToLenient;
import static com.notenoughmail.kubejs_tfc.util.WorldGenUtils.weightedBlockState;

/**
 * TODO: [Future] Move to WorldJS compat | more of TFC's types
 */
@SuppressWarnings("unused")
public class TFCWorldgenDataEventJS extends EventJS implements IDataConstructor {

    private final DataPackEventJS wrappedEvent;

    public TFCWorldgenDataEventJS(DataPackEventJS wrapped) {
        wrappedEvent = wrapped;
    }

    @Override
    public void addJson(ResourceLocation id, JsonElement json) {
        KubeJSTFC.warningLog(id.toString());
        KubeJSTFC.infoLog(json.toString());
        wrappedEvent.addJson(id, json);
    }

    @Info(value = "Creates a geode configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "outer", value = "Sets the outer block state of the geode"),
            @Param(name = "middle", value = "Sets the middle block state of the geode"),
            @Param(name = "innerValues", value = "A list of weight block state in string form, sets the inner state of the geode"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void geode(String name, String outer, String middle, String[] innerValues, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:geode", name, ResourceUtils.buildJson(config -> {
            config.add("outer", blockStateToLenient(outer));
            config.add("middle", blockStateToLenient(middle));
            final JsonArray innerArray = new JsonArray(innerValues.length);
            for (String inner : innerValues) {
                innerArray.add(weightedBlockState(inner, "data"));
            }
            config.add("inner", innerArray);
        }), placement);
    }

    @Info(value = "Creates a boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void boulder(String name, WorldGenUtils.BlockToBlockStatesMapEntry[] states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:boulder", name, states, placement);
    }

    @Info(value = "Creates a baby boulder configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "states", value = "A list of {Block -> BlockState[]} objects in string form that define the baby boulder's state property"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void babyBoulder(String name, WorldGenUtils.BlockToBlockStatesMapEntry[] states, Consumer<PlacedFeatureProperties> placement) {
        boulder("tfc:baby_boulder", name, states, placement);
    }

    private void boulder(String type, String name, WorldGenUtils.BlockToBlockStatesMapEntry[] states, Consumer<PlacedFeatureProperties> placement) {
        finishFeature(type, name, ResourceUtils.buildJson(config -> {
            WorldGenUtils.BlockToBlockStatesMapEntry.toJson(config, "states", "rock", states);
        }), placement);
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
    @Generics(PlacedFeatureProperties.class)
    public void thinSpike(String name, String state, int radius, int tries, int minHeight, int maxHeight, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:thin_spike", name, ResourceUtils.buildJson(config -> {
            config.add("state", blockStateToLenient(state));
            config.addProperty("radius", radius);
            config.addProperty("tries", tries);
            config.addProperty("min_height", minHeight);
            config.addProperty("max_height", maxHeight);
        }), placement);
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
    @Generics({ BuildVeinProperties.Cluster.class, PlacedFeatureProperties.class })
    public void clusterVein(String name, WorldGenUtils.BlockToWeightedBlockStateMapEntry[] replacementMap, int rarity, float density, int minY, int maxY, int size, Consumer<BuildVeinProperties.Cluster> optionals, Consumer<PlacedFeatureProperties> placement) {
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
    @Generics({ BuildVeinProperties.Pipe.class, PlacedFeatureProperties.class })
    public void pipeVein(String name, WorldGenUtils.BlockToWeightedBlockStateMapEntry[] replacementMap, int rarity, float density, int minY, int maxY, int height, int radius, int minSkew, int maxSkew, int minSlant, int maxSlant, float sign, Consumer<BuildVeinProperties.Pipe> optionals, Consumer<PlacedFeatureProperties> placement) {
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
    @Generics({ BuildVeinProperties.Disc.class, PlacedFeatureProperties.class })
    public void discVein(String name, WorldGenUtils.BlockToWeightedBlockStateMapEntry[] replacementMap, int rarity, float density, int minY, int maxY, int size, int height, Consumer<BuildVeinProperties.Disc> optionals, Consumer<PlacedFeatureProperties> placement) {
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
    @Generics(PlacedFeatureProperties.class)
    public void ifThen(String name, String if_, String then, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:if_then", name, ResourceUtils.buildJson(config -> {
            config.addProperty("if", if_);
            config.addProperty("then", then);
        }), placement);
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
    @Generics(PlacedFeatureProperties.class)
    public void soilDisc(String name, WorldGenUtils.BlockToBlockStateMapEntry[] replacementMap, int minRadius, int maxRadius, int height, @Nullable Float integrity, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:soil_disc", name, ResourceUtils.buildJson(config -> {
            config.addProperty("min_radius", minRadius);
            config.addProperty("max_radius", maxRadius);
            config.addProperty("height", height);
            ResourceUtils.nullable(config, "integrity", integrity);
            WorldGenUtils.BlockToBlockStateMapEntry.toJson(config, "states", replacementMap);
        }), placement);
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
    @Generics(PlacedFeatureProperties.class)
    public void hotSpring(String name, @Nullable String wallState, String fluidState, int radius, boolean allowUnderwater, WorldGenUtils.BlockToWeightedBlockStateMapEntry @Nullable [] replacesOnFluidContact, @Nullable WorldGenUtils.FissureDecoration decoration, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:hot_spring", name, ResourceUtils.buildJson(config -> {
            ResourceUtils.nullable(config, "wall_state", wallState, WorldGenUtils::blockStateToLenient);
            config.add("fluid_state", WorldGenUtils.blockStateToLenient(fluidState));
            config.addProperty("radius", radius);
            config.addProperty("allow_underwater", allowUnderwater);
            if (replacesOnFluidContact != null) {
                WorldGenUtils.BlockToWeightedBlockStateMapEntry.toJson(config, "replace_on_fluid_contact", replacesOnFluidContact);
            }
            ResourceUtils.nullable(config, "deocration", decoration, WorldGenUtils.FissureDecoration::toJson);
        }), placement);
    }

    @Info(value = "Creates a 'minecraft:simple_block' configured feature and the matching placed feature, uses a SimpleStateProvider", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "blockState", value = "The string representation of a block state, the state to be placed"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void simpleBlockState(String name, String blockState, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("minecraft:simple_block", name, ResourceUtils.buildJson(config -> {
            config.add("to_place", ResourceUtils.buildJson(toPlace -> {
                toPlace.addProperty("type", "minecraft:simple_state_provider");
                toPlace.add("state", WorldGenUtils.blockStateToLenient(blockState, true));
            }));
        }), placement);
    }

    @Info(value = "Creates a 'minecraft:random_patch' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "tries", value = "How many times the feature should attempt to place, may be null to default to 128"),
            @Param(name = "xzSpread", value = "The horizontal spread of the patch, may be null to default to 7"),
            @Param(name = "ySpread", value = "The vertical spread of the patch, may be null to default to 3"),
            @Param(name = "feature", value = "The feature to attempt to place for the patch"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void randomPatch(String name, @Nullable Integer tries, @Nullable Integer xzSpread, @Nullable Integer ySpread, String feature, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("minecraft:random_patch", name, ResourceUtils.buildJson(config -> {
            ResourceUtils.nullable(config, "tries", tries);
            ResourceUtils.nullable(config, "xz_spread", xzSpread);
            ResourceUtils.nullable(config, "y_spread", ySpread);
            config.addProperty("feature", feature);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:tall_wild_crop' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof WildDoubleCropBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void tallWildCrop(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:tall_wild_crop", name, ResourceUtils.buildJson(config -> config.addProperty("block", block)), placement);
    }

    @Info(value = "Creates a 'tfc:spreading_crop' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof WildSpreadingCropBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void spreadingCrop(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:spreading_crop", name, ResourceUtils.buildJson(config -> config.addProperty("block", block)), placement);
    }

    @Info(value = "Creates a 'tfc:spreading_bush' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to placed, must be an instanceof SpreadingBushBlock"),
            @Param(name = "placement", value = "The placement properties")
    })
    public void spreadingBush(String name, String block, Consumer<PlacedFeatureProperties> placement) {
        finishFeature("tfc:spreading_bush", name, ResourceUtils.buildJson(config -> config.addProperty("block", block)), placement);
    }

    @Info(value = "Creates a configured feature of the given type with the given config and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "type", value = "The type of configured feature to create"),
            @Param(name = "featureConfig", value = "The config json object for the feature"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void generic(String name, String type, JsonObject featureConfig, Consumer<PlacedFeatureProperties> placement) {
        finishFeature(type, name, featureConfig, placement);
    }

    @Info(value = "Creates a 'tfc:fissure' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "wallState", value = "The blockstate to use for the wall of the fissure, may be null to use the raw rock at the bottom of the world"),
            @Param(name = "fluidState", value = "The blockstate of the fluid to fill the fissure with"),
            @Param(name = "count", value = "The number of fissures to place. May be null, defaults to 5"),
            @Param(name = "radius", value = "The radius around the target position to attempt to place fissures. May be null, defaults to 12"),
            @Param(name = "minDepth", value = "A `VerticalAnchor`, the minimum depth of the fissure. May be null, defaults to 16 above bottom"),
            @Param(name = "minPieces", value = "The minimum number of pieces that makes up a fissure. May be null, defaults to 10"),
            @Param(name = "maxPieces", value = "The maximum number of pieces that make up a fissure. May be null, defaults to 24"),
            @Param(name = "maxPieceLength", value = "The maximum length and individual piece of a fissure may be. May be null, defaults to 6"),
            @Param(name = "fissureDecoration", value = "A fissure decoration object, may be null to not have one present"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void fissure(
            String name,
            @Nullable String wallState,
            String fluidState,
            @Nullable Integer count,
            @Nullable Integer radius,
            @Nullable JsonObject minDepth,
            @Nullable Integer minPieces,
            @Nullable Integer maxPieces,
            @Nullable Integer maxPieceLength,
            @Nullable WorldGenUtils.FissureDecoration fissureDecoration,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:fissure", name, ResourceUtils.buildJson(config -> {
            ResourceUtils.nullable(config, "wall_state", wallState, WorldGenUtils::blockStateToLenient);
            config.add("fluid_state", WorldGenUtils.blockStateToLenient(fluidState));
            ResourceUtils.nullable(config, "count", count);
            ResourceUtils.nullable(config, "radius", radius);
            ResourceUtils.nullable(config, "min_depth", minDepth);
            ResourceUtils.nullable(config, "min_pieces", minPieces);
            ResourceUtils.nullable(config, "max_pieces", maxPieces);
            ResourceUtils.nullable(config, "max_piece_length", maxPieceLength);
            ResourceUtils.nullable(config, "decoration", fissureDecoration, WorldGenUtils.FissureDecoration::toJson);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:forest' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "entriesTag", value = "A configured feature tag, the entries that will be placed. All values of the tag must be of the type 'tfc:forest_entry'"),
            @Param(name = "types", value = "A list of {ForestType -> ForestTypeConfig} objects"),
            @Param(name = "useWeirdness", value = "If weirdness should be considered. May be null, defaults to true"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void forest(
        String name,
        String entriesTag, // Technically, TFC supports a list of features (possibly inline?), but I'd like to avoid generics, and realistically its not hard for scriptors to tag their forest entries
        WorldGenUtils.ForestTypesMapEntry[] types,
        @Nullable Boolean useWeirdness,
        Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:forest", name, ResourceUtils.buildJson(config -> {
            config.addProperty("entries", entriesTag.startsWith("#") ? entriesTag : "#" + entriesTag);
            final JsonObject obj = new JsonObject();
            for (WorldGenUtils.ForestTypesMapEntry type : types) {
                type.toJson(obj);
            }
            config.add("types", obj);
            ResourceUtils.nullable(config, "use_weirdness", useWeirdness);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:forest_entry' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is specified"),
            @Param(name = "climate", value = "The climate restrictions"),
            @Param(name = "bushLog", value = "The log block to place for bushes. May be null to not have bushes"),
            @Param(name = "bushLeaves", value = "The leaves block to place for bushes. Mau be null to not have bushes"),
            @Param(name = "fallenLog", value = "The log block to place for fallen trees. May be null to not have fallen trees"),
            @Param(name = "fallenLeaves", value = "The leaf block to place for leaf litter. May be null to not have leaf litter"),
            @Param(name = "groundCover", value = "A list of weighted block states for other ground cover. May be null to not have any ground cover"),
            @Param(name = "normalTreeFeature", value = "The feature to place for normal trees"),
            @Param(name = "deadTreeFeature", value = "The feature to place for dead trees"),
            @Param(name = "oldGrowthTreeFeature", value = "The feature to place for old growth trees. May be null to place the normal trees in place of old growth trees"),
            @Param(name = "krummholzFeature", value = "The feature to place for krummkolz. May be null to not have krummholz"),
            @Param(name = "oldGrowthChance", value = "The chance that a placed tree will be an old growth tree. Higher values are more rare. May be null to default to 6"),
            @Param(name = "spoilerOldGrowthChance", value = "The chance that a placed tree will be an old growth tree during spoiler selection. Higher values are more rare. Mau be null to default to 200"),
            @Param(name = "fallenTreeChance", value = "The chance that fallen trees will be placed. Higher values are more rare. May be null, defaults to 14"),
            @Param(name = "deadChance", value = "The chance that dead trees will be placed. Higher values are more rare. May be null, defaults to 75"),
            @Param(name = "floating", value = "Sets the placement height to be world surface instead of ocean floor. May be null"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics({ PlacedFeatureProperties.Climate.class, PlacedFeatureProperties.class })
    public void forestEntry(
            String name,
            Consumer<PlacedFeatureProperties.Climate> climate,
            @Nullable String bushLog,
            @Nullable String bushLeaves,
            @Nullable String fallenLog,
            @Nullable String fallenLeaves,
            String @Nullable [] groundCover,
            String normalTreeFeature,
            String deadTreeFeature,
            @Nullable String oldGrowthTreeFeature,
            @Nullable String krummholzFeature,
            @Nullable Integer oldGrowthChance,
            @Nullable Integer spoilerOldGrowthChance,
            @Nullable Integer fallenTreeChance,
            @Nullable Integer deadChance,
            @Nullable Boolean floating,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:forest_entry", name, ResourceUtils.buildJson(config -> {
            config.add("climate", PlacedFeatureProperties.buildClimate(climate).toJson());
            WorldGenUtils.nullableLenient(config, "bush_log", bushLog);
            WorldGenUtils.nullableLenient(config, "bush_leaves", bushLeaves);
            WorldGenUtils.nullableLenient(config, "fallen_log", fallenLog);
            WorldGenUtils.nullableLenient(config, "fallen_leaves", fallenLeaves);
            ResourceUtils.nullable(config, "groundcover", groundCover, l -> {
                final JsonArray array = new JsonArray(l.length);
                for (String s : l) {
                    array.add(WorldGenUtils.weightedBlockState(s, "block"));
                }
                return array;
            });
            config.addProperty("normal_tree", normalTreeFeature);
            config.addProperty("dead_tree", deadTreeFeature);
            ResourceUtils.nullable(config, "old_growth_tree", oldGrowthTreeFeature);
            ResourceUtils.nullable(config, "krummholz", krummholzFeature);
            ResourceUtils.nullable(config, "old_growth_chance", oldGrowthChance);
            ResourceUtils.nullable(config, "spoiler_old_growth_chance", spoilerOldGrowthChance);
            ResourceUtils.nullable(config, "fallen_tree_chance", fallenTreeChance);
            ResourceUtils.nullable(config, "dead_chance", deadChance);
            ResourceUtils.nullable(config, "floating", floating);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:overlay_tree' configured feature and the matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "baseStructure", value = "The base structure to place"),
            @Param(name = "overlayStructure", value = "The structure to overlay on top of the base structure"),
            @Param(name = "trunk", value = "The trunk of the tree, may be null"),
            @Param(name = "overlayIntegrity", value = "The % of overlay blocks to 'rot', in the range [0, 1]. May be null to default to 0.5"),
            @Param(name = "treePlacement", value = "The tree placement properties"),
            @Param(name = "rootSystem", value = "The root properties, maye be null"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void overlayTree(
            String name,
            String baseStructure,
            String overlayStructure,
            @Nullable WorldGenUtils.Trunk trunk,
            @Nullable Float overlayIntegrity,
            WorldGenUtils.TreePlacement treePlacement,
            @Nullable WorldGenUtils.Root rootSystem,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:overlay_tree", name, ResourceUtils.buildJson(config -> {
            config.addProperty("base", baseStructure);
            config.addProperty("overlay", overlayStructure);
            ResourceUtils.nullable(config, "trunk", trunk, WorldGenUtils.Trunk::toJson);
            ResourceUtils.nullable(config, "overlay_integrity", overlayIntegrity);
            config.add("placement", treePlacement.toJson());
            ResourceUtils.nullable(config, "root_system", rootSystem, WorldGenUtils.Root::toJson);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:random_tree' configured feature and matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "structures", value = "A list of Structures that could be placed"),
            @Param(name = "trunk", value = "THe trunk of the tree, may be null"),
            @Param(name = "treePlacement", value = "The tree placement properties"),
            @Param(name = "rootSystem", value = "The root properties, may be null"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void randomTree(
            String name,
            String[] structures,
            @Nullable WorldGenUtils.Trunk trunk,
            WorldGenUtils.TreePlacement treePlacement,
            @Nullable WorldGenUtils.Root rootSystem,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:random_tree", name, ResourceUtils.buildJson(config -> {
            final JsonArray array = new JsonArray(structures.length);
            for (String s : structures) array.add(s);
            config.add("structures", array);
            ResourceUtils.nullable(config, "trunk", trunk, WorldGenUtils.Trunk::toJson);
            config.add("placement", treePlacement.toJson());
            ResourceUtils.nullable(config, "root_system", rootSystem, WorldGenUtils.Root::toJson);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:stacked_tree' configured feature and matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "layers", value = "A list of tree layers"),
            @Param(name = "trunk", value = "The trunk of the tree"),
            @Param(name = "treePlacement", value = "The tree placement properties"),
            @Param(name = "rootSystem", value = "The root properties, may be null"),
            @Param(name = "placement", value = "The placement properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void stackedTree(
            String name,
            WorldGenUtils.TreeLayer[] layers,
            WorldGenUtils.Trunk trunk,
            WorldGenUtils.TreePlacement treePlacement,
            @Nullable WorldGenUtils.Root rootSystem,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:stacked_tree", name, ResourceUtils.buildJson(config -> {
            final JsonArray array = new JsonArray(layers.length);
            for (WorldGenUtils.TreeLayer l : layers) array.add(l.toJson());
            config.add("layers", array);
            config.add("trunk", trunk.toJson());
            config.add("placement", treePlacement.toJson());
            ResourceUtils.nullable(config, "root_system", rootSystem, WorldGenUtils.Root::toJson);
        }), placement);
    }

    @Info(value = "Creates a 'tfc:krummholz' configured feature and matching placed feature", params = {
            @Param(name = "name", value = "The name of the feature, the namespace will default to 'kubejs_tfc' if none is provided"),
            @Param(name = "block", value = "The block to place"),
            @Param(name = "height", value = "The height the krummholz may be"),
            @Param(name = "spawnsOnStone", value = "If the krummholz may spawn on stone. May be null to default to false"),
            @Param(name = "spawnsOnGravel", value = "If the krummholz may spawn on gravel. May be null to default to false"),
            @Param(name = "placement", value = "The placement Properties")
    })
    @Generics(PlacedFeatureProperties.class)
    public void krummholz(
            String name,
            String block,
            IntProvider height,
            @Nullable Boolean spawnsOnStone,
            @Nullable Boolean spawnsOnGravel,
            Consumer<PlacedFeatureProperties> placement
    ) {
        finishFeature("tfc:krummholz", name, ResourceUtils.buildJson(config -> {
            config.addProperty("block", block);
            config.add("height", IntProvider.CODEC.encodeStart(JsonOps.INSTANCE, height).getOrThrow(false, KubeJSTFC::warningLog));
            ResourceUtils.nullable(config, "spawns_on_stone", spawnsOnStone);
            ResourceUtils.nullable(config, "spawns_on_gravel", spawnsOnGravel);
        }), placement);
    }

    @Info(value = "Creates a new block to block state list map entry for use in boulder configured features", params = {
            @Param(name = "block", value = "The registry name of a block to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of a block state")
    })
    public WorldGenUtils.BlockToBlockStatesMapEntry boulderState(String rock, String[] blockStates) {
        return new WorldGenUtils.BlockToBlockStatesMapEntry(rock, blockStates);
    }

    @Info(value = "Creates a new block list to weighted block state list map entry for use several configured features", params = {
            @Param(name = "blocks", value = "A list of strings, the registry names of blocks to be replaced"),
            @Param(name = "blockStates", value = "A list of string representations of weighted block states")
    })
    public WorldGenUtils.BlockToWeightedBlockStateMapEntry blockToWeightedBlockState(String[] replace, String[] with) {
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
    public WorldGenUtils.FissureDecoration fissureDecoration(WorldGenUtils.BlockToWeightedBlockStateMapEntry[] replacementMap, int rarity, int radius, int count) {
        return new WorldGenUtils.FissureDecoration(replacementMap, rarity, rarity, count);
    }

    @Info(value = "Creates a new forest types map entry for use with forest features", params = {
            @Param(name = "type", value = "The `ForestType` and the key of this entry"),
            @Param(name = "treeCount", value = "The tree count, may be null"),
            @Param(name = "groundcoverCount", value = "The groundcover count, may be null"),
            @Param(name = "perChunkChance", value = "The chance, in the range [0, 1], per chunk, that forests will spawn"),
            @Param(name = "bushCount", value = "The bush count, may be null"),
            @Param(name = "hasSpoilerOldGrowth", value = "If this entry has old growth spoilers. May be null to default to false"),
            @Param(name = "allowsOldGrowth", value = "If this entry has old growth trees. May be null to default to false"),
            @Param(name = "leafPileCount", value = "The leaf pile count, may be null")
    })
    public WorldGenUtils.ForestTypesMapEntry forestTypesMapEntry(ForestType type, @Nullable IntProvider treeCount, @Nullable IntProvider groundcoverCount, @Nullable Float perChunkChance, @Nullable IntProvider bushCount, @Nullable Boolean hasSpoilerOldGrowth, @Nullable Boolean allowsOldGrowth, @Nullable IntProvider leafPileCount) {
        return new WorldGenUtils.ForestTypesMapEntry(type, treeCount, groundcoverCount, perChunkChance, bushCount, hasSpoilerOldGrowth, allowsOldGrowth, leafPileCount);
    }

    @Info(value = "Creates a new tree trunk object for use with tree features", params = {
            @Param(name = "state", value = "The block state to place"),
            @Param(name = "minHeight", value = "The minimum height the trunk may be"),
            @Param(name = "maxHeight", value = "The maximum height the trunk may be"),
            @Param(name = "wide", value = "If the trunk is 2x2")
    })
    public WorldGenUtils.Trunk trunk(String state, int minHeight, int maxHeight, boolean wide) {
        return new WorldGenUtils.Trunk(state, minHeight, maxHeight, wide);
    }

    @Info(value = "Creates a new tree placement object for use with tree features", params = {
            @Param(name = "width", value = "The horizontal distance to check"),
            @Param(name = "height", value = "The vertical distance to check"),
            @Param(name = "groundType", value = "The type of ground the tree can place in. May be null to default to 'normal'")
    })
    public WorldGenUtils.TreePlacement treePlacement(int width, int height, @Nullable TreePlacementConfig.GroundType groundType) {
        return new WorldGenUtils.TreePlacement(width, height, groundType);
    }

    @Info(value = "Creates a new tree root object for use with tree features", params = {
            @Param(name = "blocks", value = "A list of {Block[] -> BlockState[]} objects, the replacement map for root blocks"),
            @Param(name = "width", value = "The horizontal distance to attempt to place roots. May be null to default to 4"),
            @Param(name = "height", value = "The vertical distance to attempt to place roots. May be null to default to 3"),
            @Param(name = "tries", value = "The number of attempts to place roots that should be made. May be null to default to 5"),
            @Param(name = "specialPlacerSkewChance", value = "The chance, in the range [0, 1], for extra roots to be placed. May be null"),
            @Param(name = "required", value = "If roots must be placed")
    })
    public WorldGenUtils.Root root(WorldGenUtils.BlockToWeightedBlockStateMapEntry[] blocks, @Nullable Integer width, @Nullable Integer height, @Nullable Integer tries, @Nullable Float specialPlacerSkewChance, @Nullable Boolean required) {
        return new WorldGenUtils.Root(blocks, width, height, tries, specialPlacerSkewChance, required);
    }

    @Info(value = "Creates a new tree layer object for use with layer tree features", params = {
            @Param(name = "templates", value = "A list of structures to place"),
            @Param(name = "minCount", value = "The minimum number of structures to place"),
            @Param(name = "maxCount", value = "The maximum number of structures to place")
    })
    public WorldGenUtils.TreeLayer treeLayer(String[] templates, int minCount, int maxCount) {
        return new WorldGenUtils.TreeLayer(templates, minCount, maxCount);
    }

    private void finishFeature(String name, JsonObject configuredFeature, Consumer<PlacedFeatureProperties> placement) {
        addJson(ResourceUtils.configuredFeatureName(name), configuredFeature);

        addJson(ResourceUtils.placedFeatureName(name), Util.make(new PlacedFeatureProperties(name), placement).toJson());
    }

    private void finishFeature(String type, String name, JsonObject config, Consumer<PlacedFeatureProperties> placement) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.add("config", config);

        finishFeature(name, json, placement);
    }
}
