package com.notenoughmail.kubejs_tfc.event;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockLayerSettingsAccessor;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Info("""
        Allows for editing of TFC's worldgen values after the `overworld.json` file is read
        and before players are able to edit values on TFC's worldgen configuration screen
        
        Note: this event may fire for already existing worlds, but any changes made will not effect them
        """)
@SuppressWarnings("unused")
public class ModifyDefaultWorldGenSettingsEventJS extends EventJS {

    private final TFCChunkGenerator generator;
    private boolean flatBedrock;
    private int spawnDistance;
    private int spawnCenterX;
    private int spawnCenterZ;
    private int tempScale;
    private float tempConstant;
    private int rainScale;
    private float rainConstant;
    private float continentalness;
    private float grassDensity;

    private final Map<String, RockSettings> rocks;
    private final List<String> bottom, oceanFloor, land, volcanic, uplift;
    private final List<RockLayerSettings.LayerData> layers;

    public ModifyDefaultWorldGenSettingsEventJS(TFCChunkGenerator generator) {
        this.generator = generator;
        final Settings settings = generator.settings();
        flatBedrock = settings.flatBedrock();
        spawnDistance = settings.spawnDistance();
        spawnCenterX = settings.spawnCenterX();
        spawnCenterZ = settings.spawnCenterZ();
        tempScale = settings.temperatureScale();
        tempConstant = settings.temperatureConstant();
        rainScale = settings.rainfallScale();
        rainConstant = settings.rainfallConstant();
        continentalness = settings.continentalness();
        grassDensity = settings.grassDensity();

        // Copy values to mutable lists and maps
        final RockLayerSettings.Data data = ((RockLayerSettingsAccessor) (Object) settings.rockLayerSettings()).kubejs_tfc$Data();
        rocks = new HashMap<>(data.rocks());
        bottom = new ArrayList<>(data.bottom());
        oceanFloor = new ArrayList<>(data.oceanFloor());
        land = new ArrayList<>(data.land());
        volcanic = new ArrayList<>(data.volcanic());
        uplift = new ArrayList<>(data.uplift());
        layers = new ArrayList<>();
        data.layers().forEach(layerData -> layers.add(new RockLayerSettings.LayerData(layerData.id(), new HashMap<>(layerData.layers()))));
    }

    @Info("Sets if the world should have flat bedrock, defaults to false")
    public void flatBedrock(boolean b) {
        flatBedrock = b;
    }

    @Info("Sets flat bedrock to true")
    public void flatBedrock() {
        flatBedrock(true);
    }

    @Info("Sets the distance from the spawn center that players may spawn")
    public void setSpawnDistance(int distance) {
        spawnDistance = distance;
    }

    @Info("Sets the spawn center on the x-coordinate")
    public void setSpawnCenterX(int xCenter) {
        spawnCenterX = xCenter;
    }

    @Info("Sets the spawn center on the z-coordinate")
    public void setSpawnCenterZ(int zCenter) {
        spawnCenterZ = zCenter;
    }

    @Info("Sets the temperature scale of the world, the distance from pole to pole, defaults to 20000")
    public void setTemperatureScale(int scale) {
        tempScale = scale;
    }

    @Info("Sets the relative constant temperature of the world, defaults to 0")
    public void setTemperatureConstant(float constant) {
        tempConstant = constant;
    }

    @Info("Sets the rainfall scale of the world, the distance between peaks in intensity, defaults to 20000")
    public void setRainfallScale(int scale) {
        rainScale = scale;
    }

    @Info("Sets the relative constant temperature of the world, defaults to 0")
    public void setRainfallConstant(float constant) {
        rainConstant = constant;
    }

    @Info("Sets the proportion of the world that is land instead of water, defaults to 0.5")
    public void setContinentalness(float continentalness) {
        this.continentalness = continentalness;
    }

    @Info("Sets the grass density of the world, defaults to 0.5")
    public void setGrassDensity(float density) {
        this.grassDensity = density;
    }

    @Info(value = "Adds the given rock to the generator's pool of available rocks", params = {
            @Param(name = "rock", value = "the `RockSettings` to add"),
            @Param(name = "name", value = "The name which the rock can be referenced by"),
            @Param(name = "bottom", value = "If the rock should be added as a 'bottom' layer rock")
    })
    public void addRock(RockSettings rock, String name, boolean bottom) {
        rocks.put(name, rock);
        if (bottom) {
            this.bottom.add(name);
        }
    }

    @Info(value = "Adds the given rock to the generator's pool of available rocks", params = {
            @Param(name = "id", value = "The registered id of the `RockSettings` to add"),
            @Param(name = "name", value = "The name which the rock can be referenced by"),
            @Param(name = "bottom", value = "If the rock should be added as a 'bottom' layer rock")
    })
    public void addRockFromId(ResourceLocation id, String name, boolean bottom) {
        addRock(RockSettings.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive(id.toString())).getOrThrow(false, error -> {}).getFirst(), name, bottom);
    }

    @Nullable
    @Info("Gets the `RockSettings` with the given name")
    public RockSettings getRock(String name) {
        return rocks.get(name);
    }

    @Info("Gets the names of all rocks currently in the generator's pool of rocks")
    @Generics(String.class)
    public Set<String> getRockNames() {
        return rocks.keySet();
    }

    @Info("Removes the given rock from the generator")
    public void removeRock(String name) {
        rocks.remove(name);
        bottom.remove(name);
        layers.forEach(layerData -> layerData.layers().remove(name));
    }

    @Info("Adds the given rock to the bottom layer")
    public void addToBottom(String name) {
        bottom.add(name);
    }

    @Info("Removes the given rock from the bottom layer")
    public void removeFromBottom(String name) {
        bottom.remove(name);
    }

    @Info(value = "Defines a new rock layer", params = {
            @Param(name = "id", value = "The name of the layer to add"),
            @Param(name = "rockMap", value = "A map of rock names to layer names, associates a rock with the layer that will generate underneath it")
    })
    @Generics({String.class, String.class})
    public void defineLayer(String id, Map<String, String> rockMap) {
        layers.add(new RockLayerSettings.LayerData(id, rockMap));
    }

    @Info("Removes the given layer from the generator")
    public void removeLayer(String layerId) {
        layers.removeIf(layer -> layer.id().equals(layerId));
        layers.forEach(layer -> {
            final List<String> removals = new ArrayList<>(layer.layers().size());
            layer.layers().forEach((rock, rockLayer) -> {
                if (rockLayer.equals(layerId)) {
                    removals.add(rock);
                }
            });
            removals.forEach(rock -> layer.layers().remove(rock));
        });
        oceanFloor.remove(layerId);
        land.remove(layerId);
        volcanic.remove(layerId);
        uplift.remove(layerId);
    }

    @Info("Gets the names of all layers currently in the generator's pool of layers")
    @Generics(String.class)
    public List<String> getLayerIds() {
        return layers.stream().map(RockLayerSettings.LayerData::id).toList();
    }

    @Info("Removes all rocks and rock layers from the generator")
    public void cleanSlate() {
        rocks.clear();
        layers.clear();
        bottom.clear();
        oceanFloor.clear();
        land.clear();
        volcanic.clear();
        uplift.clear();
    }

    @Info("Adds the given layer to the 'ocean_floor' layer type")
    public void addOceanFloorLayer(String name) {
        oceanFloor.add(name);
    }

    @Info("Removes the given layer from the 'ocean_floor' layer type")
    public void removeOceanFloorLayer(String name) {
        oceanFloor.remove(name);
    }

    @Info("Gets the layers that are currently in the 'ocean_floor' layer type")
    @Generics(String.class)
    public List<String> getOceanFloorLayers() {
        return oceanFloor;
    }

    @Info("Adds the given layer to the 'land' layer type")
    public void addLandLayer(String name) {
        land.add(name);
    }

    @Info("Removes the given layer from the 'land' layer type")
    public void removeLandLayer(String name) {
        land.remove(name);
    }

    @Info("Gets the layers that are currently in the 'land' layer type")
    @Generics(String.class)
    public List<String> getLandLayers() {
        return land;
    }

    @Info("Adds the given layer to the 'volcanic' layer type")
    public void addVolcanicLayer(String name) {
        volcanic.add(name);
    }

    @Info("Removes the given layer from the 'volcanic' layer type")
    public void removeVolcanicLayer(String name) {
        volcanic.remove(name);
    }

    @Info("Gets the layers that are currently in the 'volcanic' layer type")
    @Generics(String.class)
    public List<String> getVolcanicLayers() {
        return volcanic;
    }

    @Info("Adds the given layer to the 'uplift' layer type")
    public void addUpliftLayer(String name) {
        uplift.add(name);
    }

    @Info("Removes the given layer from the 'uplift' layer type")
    public void removeUpliftLayer(String name) {
        uplift.remove(name);
    }

    @Info("Gets the layers that are currently in the 'uplift' layer type")
    @Generics(String.class)
    public List<String> getUpliftLayers() {
        return uplift;
    }

    @Override
    protected void afterPosted(EventResult result) {
        final boolean validSettings;
        if (bottom.isEmpty() || oceanFloor.isEmpty() || land.isEmpty() || volcanic.isEmpty() || uplift.isEmpty()) {
            validSettings = false;
            ConsoleJS.SERVER.error("""
                    Custom rock layer settings are invalid, cannot have an empty layer type, using default settings.
                        bottom=%s
                        ocean_floor=%s
                        land=%s
                        volcanic=%s
                        uplift=%s
                    """.formatted(bottom, oceanFloor, land, volcanic, uplift));
        } else {
            validSettings = true;
        }
        generator.applySettings(old -> new Settings(
                flatBedrock,
                spawnDistance,
                spawnCenterX,
                spawnCenterZ,
                tempScale,
                tempConstant,
                rainScale,
                rainConstant,
                validSettings ? new RockLayerSettings.Data(rocks, bottom, layers, oceanFloor, land, volcanic, uplift).parse() : old.rockLayerSettings(),
                continentalness,
                grassDensity
        ));
    }
}
