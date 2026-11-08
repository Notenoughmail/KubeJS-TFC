package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cg;

import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.util.RecordIndexHint;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.core.Holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ReturnsSelf
public class SettingsBuilder {

    private static final TypeInfo ROCK_SETTINGS = TypeInfo.of(RockSettings.class);

    public static Settings build(Context ctx, Consumer<SettingsBuilder> builder) {
        final SettingsBuilder b = new SettingsBuilder();
        builder.accept(b);
        return b.build(SourceLine.of(ctx));
    }

    boolean flatBedrock, finite;
    int spawnDistance = 20000, centerX = 0, centerZ = 0, tempScale = 20000, rainScale = 20000;
    float tempConstant, rainConstant, continentalness = 0.5F, grassDensity = 0.5F;
    final Map<String, Holder<RockSettings>> rocks = new HashMap<>();
    final List<String> bottom = new ArrayList<>(), oceanFloor = new ArrayList<>(), land = new ArrayList<>(), volcanic = new ArrayList<>(), uplift = new ArrayList<>();
    final List<RockLayerSettings.LayerData> layers = new ArrayList<>();

    @Info("Mark the world as generating with a flat bedrock bottom")
    public SettingsBuilder flatBedrock() {
        flatBedrock = true;
        return this;
    }

    @Info("Mark the world as generating only a finite amount of continents")
    public SettingsBuilder finiteContinents() {
        finite = true;
        return this;
    }

    @Info(value = "The player spawn settings", params = {
            @Param(name = "distance", value = "The distance from the center that players may spawn"),
            @Param(name = "xCenter", value = "The x-coordinate of the spawn center"),
            @Param(name = "zCenter", value = "The z-coordinate of the spawn center")
    })
    public SettingsBuilder spawnSettings(int distance, int xCenter, int zCenter) {
        spawnDistance = distance;
        centerX = xCenter;
        centerZ = zCenter;
        return this;
    }

    @Info("The temperature scale, the distance pole-to-pole")
    public SettingsBuilder temperatureScale(int scale) {
        tempScale = scale;
        return this;
    }

    @Info("The rainfall scale, the distance between peaks in intensity")
    public SettingsBuilder rainfallScale(int scale) {
        rainScale = scale;
        return this;
    }

    @Info("The relative constant temperature of the world")
    public SettingsBuilder temperatureConstant(float constant) {
        tempConstant = constant;
        return this;
    }

    @Info("The relative constant rainfall of the world")
    public SettingsBuilder rainfallConstant(float constant) {
        rainConstant = constant;
        return this;
    }

    @Info(value = "Creates and adds the given rock to the world", params = {
            @Param(name = "name", value = "The name the rock will be referred to in the generator"),
            @Param(name = "settings", value = "The blocks that make up the rock")
    })
    @RecordIndexHint(1) // Not sure if this'll actually help
    public SettingsBuilder withRock(String name, RockSettings settings) {
        rocks.put(name, Holder.direct(settings));
        return this;
    }

    @Info(value = "Adds a registered rock to the world", params = {
            @Param(name = "name", value = "The name the rock will be referred to in the generator"),
            @Param(name = "rock", value = "The id of the registered rock")
    })
    public SettingsBuilder withRock(String name, Holder.Reference<RockSettings> rock) {
        rocks.put(name, rock);
        return this;
    }

    @Info("Adds the registered rocks to the world")
    public SettingsBuilder withRocks(Map<String, Holder.Reference<RockSettings>> rocks) {
        this.rocks.putAll(rocks);
        return this;
    }

    @Info("The rocks to use as the bottom layer of the world")
    public SettingsBuilder bottomRocks(String... rocks) {
        return add(bottom, rocks);
    }

    @Info(value = "Creates and adds a layer to the world", params = {
            @Param(name = "id", value = "The name the layer can be referred by"),
            @Param(name = "mapping", value = "A mapping of rock names to layer ids, the rock that will generate in this layer and the layer below it")
    })
    public SettingsBuilder layer(String id, Map<String, String> mapping) {
        layers.add(new RockLayerSettings.LayerData(id, mapping));
        return this;
    }

    @Info("The layers to use for the ocean floor rock type")
    public SettingsBuilder oceanFloorLayers(String... layers) {
        return add(oceanFloor, layers);
    }

    @Info("The layers to use for the land rock type")
    public SettingsBuilder landLayers(String... layers) {
        return add(land, layers);
    }

    @Info("The layers to use for the volcanic rock type")
    public SettingsBuilder volcanicLayers(String... layers) {
        return add(volcanic, layers);
    }

    @Info("The layers to use for the uplift rock type")
    public SettingsBuilder upliftLayers(String... layers) {
        return add(uplift, layers);
    }

    @HideFromJS
    private Settings build(SourceLine source) {
        return new Settings(
                flatBedrock,
                spawnDistance,
                centerX,
                centerZ,
                tempScale,
                tempConstant,
                rainScale,
                rainConstant,
                RockLayerSettings.decode(new RockLayerSettings.Data(
                        rocks,
                        bottom,
                        layers,
                        oceanFloor,
                        land,
                        volcanic,
                        uplift
                )).getOrThrow(s -> Validations.exception(source, s)),
                continentalness,
                grassDensity,
                finite
        );
    }

    private SettingsBuilder add(List<String> l, String... s) {
        l.addAll(List.of(s));
        return this;
    }
}
