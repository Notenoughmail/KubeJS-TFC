package io.github.notenoughmail.kubejstfc.events.startup;

import com.mojang.serialization.*;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.resources.RegistryOps;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Info("""
        Allows for editing of TFC's worldgen values after the `overworld.json` file is read
        and before players are able to edit values on TFC's worldgen configuration screen
        
        Note: this event may fire for already existing worlds, but any changes made will not effect them
        """)
@SuppressWarnings("unused")
public class KubeDefaultWorldSettingsEvent implements KubeEvent {

    public static final MapCodec.ResultFunction<Settings> SETTINGS_TRANSFORMER = new MapCodec.ResultFunction<>() {
        @Override
        public <T> DataResult<Settings> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<Settings> a) {
            // JsonOps means this is likely being called during world creation, the whole point of this event
            // There is the unfortunate detail of JsonOps being used once during world load, before NBTOps being used again ?
            if (ops instanceof RegistryOps<T> regOps && regOps.delegate instanceof JsonOps) {
                return a.map(settings -> {
                    if (KubeJSTFCEventHandlers.defaultWorldSettings.hasListeners()) {
                        final KubeDefaultWorldSettingsEvent event = new KubeDefaultWorldSettingsEvent(settings);
                        KubeJSTFCEventHandlers.defaultWorldSettings.post(event);
                        final Settings modified = event.build();
                        KubeJSTFC.debugWarning("Modified worldgen settings: {}", () -> Settings.CODEC.encoder().encodeStart(ops, modified).getOrThrow());
                        return modified;
                    } else {
                        return settings;
                    }
                });
            } else {
                return a;
            }
        }

        @Override
        public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, Settings input, RecordBuilder<T> t) {
            return t;
        }

        @Override
        public String toString() {
            return "KubeJS TFC: TFC Settings Transformer";
        }
    };

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
    private boolean finiteContinents;

    private final Map<String, RockSettings> rocks;
    private final List<String> bottom, oceanFloor, land, volcanic, uplift;
    private final List<RockLayerSettings.LayerData> layers;
    private final RockLayerSettings oldRockLayerSettings;

    public KubeDefaultWorldSettingsEvent(Settings settings) {
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
        finiteContinents = settings.finiteContinents();
        oldRockLayerSettings = settings.rockLayerSettings();

        // Copy values to mutable lists and maps
        final RockLayerSettings.Data data = settings.rockLayerSettings().data;
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

    @Info("Gets the current flat bedrock value")
    public boolean getFlatBedrock() {
        return flatBedrock;
    }

    @Info("Sets flat bedrock to true")
    public void flatBedrock() {
        flatBedrock(true);
    }

    @Info("Sets the distance from the spawn center that players may spawn")
    public void setSpawnDistance(int distance) {
        spawnDistance = distance;
    }

    @Info("Gets the current spawn distance")
    public int getSpawnDistance() {
        return spawnDistance;
    }

    @Info("Sets the spawn center on the x-coordinate")
    public void setSpawnCenterX(int xCenter) {
        spawnCenterX = xCenter;
    }

    @Info("Gets the current x-coordinate of the spawn center")
    public int getSpawnCenterX() {
        return spawnCenterX;
    }

    @Info("Sets the spawn center on the z-coordinate")
    public void setSpawnCenterZ(int zCenter) {
        spawnCenterZ = zCenter;
    }

    @Info("Gets the current z-coordinate of the spawn center")
    public int getSpawnCenterZ() {
        return spawnCenterZ;
    }

    @Info("Sets the temperature scale of the world, the distance from pole to pole, defaults to 20000")
    public void setTemperatureScale(int scale) {
        tempScale = scale;
    }

    @Info("Gets the current temperature scale")
    public int getTemperatureScale() {
        return tempScale;
    }

    @Info("Sets the relative constant temperature of the world, defaults to 0")
    public void setTemperatureConstant(float constant) {
        tempConstant = constant;
    }

    @Info("Gets the current temperature constant")
    public float getTemperatureConstant() {
        return tempConstant;
    }

    @Info("Sets the rainfall scale of the world, the distance between peaks in intensity, defaults to 20000")
    public void setRainfallScale(int scale) {
        rainScale = scale;
    }

    @Info("Gets the current rainfall scale")
    public int getRainfallScale() {
        return rainScale;
    }

    @Info("Sets the relative constant temperature of the world, defaults to 0")
    public void setRainfallConstant(float constant) {
        rainConstant = constant;
    }

    @Info("Gets the current rainfall constant")
    public float getRainfallConstant() {
        return rainConstant;
    }

    @Info("Sets the proportion of the world that is land instead of water, defaults to 0.5")
    public void setContinentalness(float continentalness) {
        this.continentalness = continentalness;
    }

    @Info("gets the current continentalness factor")
    public float getContinentalness() {
        return continentalness;
    }

    @Info("Sets the grass density of the world, defaults to 0.5")
    public void setGrassDensity(float density) {
        this.grassDensity = density;
    }

    @Info("Gets the current grass density fo the world")
    public float getGrassDensity() {
        return grassDensity;
    }

    @Info("Sets if the world should spawn only a finite number of continents")
    public void setFiniteContinents(boolean finite) {
        finiteContinents = finite;
    }

    @Info("Sets finite continents to true")
    public void finiteContinents() {
        setFiniteContinents(true);
    }

    @Info("Gets the current finite continents value")
    public boolean getFiniteContinents() {
        return finiteContinents;
    }

    @Info(value = "Adds the given rock to the generator's pool of available rocks", params = {
            @Param(name = "name", value = "The name which the rock can be referenced by"),
            @Param(name = "rock", value = "the `RockSettings` to add"),
            @Param(name = "bottom", value = "If the rock should be added as a 'bottom' layer rock")
    })
    public void addRock(String name, RockSettings rock, boolean bottom) {
        rocks.put(name, rock);
        if (bottom) {
            this.bottom.add(name);
        }
    }

    @Nullable
    @Info("Gets the `RockSettings` with the given name")
    public RockSettings getRock(String name) {
        return rocks.get(name);
    }

    @Info("Gets the names of all rocks currently in the generator's pool of rocks")
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
    public void defineLayer(String id, Map<String, String> rockMap) {
        layers.add(new RockLayerSettings.LayerData(id, rockMap));
    }

    @Info("Removes the given layer from the generator")
    public void removeLayer(String layerId) {
        layers.removeIf(layer -> layer.id().equals(layerId));
        layers.forEach(layer ->
                layer.layers().entrySet().removeIf(entry ->
                        entry.getValue().equals(layerId)
                )
        );
        oceanFloor.remove(layerId);
        land.remove(layerId);
        volcanic.remove(layerId);
        uplift.remove(layerId);
    }

    @Info("Gets the names of all layers currently in the generator's pool of layers")
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
    public List<String> getUpliftLayers() {
        return uplift;
    }

    @HideFromJS
    public Settings build() {
        final boolean validRocks;
        if (bottom.isEmpty() || oceanFloor.isEmpty() || land.isEmpty() || volcanic.isEmpty() || uplift.isEmpty()) {
            validRocks = false;
            ConsoleJS.STARTUP.error("""
                    Custom rock layer settings are invalid, cannot have an empty layer type, using default rock settings.
                        bottom=%s
                        ocean_floor=%s
                        land=%s
                        volcanic=%s
                        uplift=%s
                    """.formatted(bottom, oceanFloor, land, volcanic, uplift));
        } else {
            validRocks = true;
        }

        return new Settings(
                flatBedrock,
                spawnDistance,
                spawnCenterX,
                spawnCenterZ,
                tempScale,
                tempConstant,
                rainScale,
                rainConstant,
                validRocks ?
                        KubeJSTFC.tryOrElse(
                                () -> RockLayerSettings.decode(new RockLayerSettings.Data(rocks, bottom, layers, oceanFloor, land, volcanic, uplift)).getOrThrow(),
                                oldRockLayerSettings,
                                e -> {
                                    KubeJSTFC.LOGGER.error("Error encountered while parsing rock settings", e);
                                    ConsoleJS.SERVER.error("""
                                        Error encountered while parsing rock settings:
                                        %s:
                                        %s
                                        \t%s
                                        Using default rock settings
                                        """.formatted(
                                            e.getClass(),
                                            e.getMessage(),
                                            String.join("\n\t", Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new))
                                    ));
                                }
                        ) :
                        oldRockLayerSettings,
                continentalness,
                grassDensity,
                finiteContinents
        );
    }
}
