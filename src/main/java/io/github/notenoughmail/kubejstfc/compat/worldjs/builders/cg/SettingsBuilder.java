package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.cg;

import dev.latvian.mods.kubejs.holder.HolderWrapper;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.misc.RockSettingsBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.worldjs.util.Validations;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

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

    public SettingsBuilder flatBedrock() {
        flatBedrock = true;
        return this;
    }

    public SettingsBuilder finiteContinents() {
        finite = true;
        return this;
    }

    public SettingsBuilder spawnSettings(int distance, int xCenter, int zCenter) {
        spawnDistance = distance;
        centerX = xCenter;
        centerZ = zCenter;
        return this;
    }

    public SettingsBuilder temperatureScale(int scale) {
        tempScale = scale;
        return this;
    }

    public SettingsBuilder rainfallScale(int scale) {
        rainScale = scale;
        return this;
    }

    public SettingsBuilder temperatureConstant(float constant) {
        tempConstant = constant;
        return this;
    }

    public SettingsBuilder rainfallConstant(float constant) {
        rainConstant = constant;
        return this;
    }

    public SettingsBuilder withRock(Context ctx, String name, ResourceLocation id, Consumer<RockSettingsBuilder> builder) {
        final RockSettingsBuilder rock = new RockSettingsBuilder(id);
        Assistant.registerBuilder(SourceLine.of(ctx), RockSettings.KEY, rock);
        builder.accept(rock);
        return withRock(name, Cast.to(HolderWrapper.wrapRef(Cast.to(ctx), id, ROCK_SETTINGS)));
    }

    public SettingsBuilder withRock(String name, Holder.Reference<RockSettings> rock) {
        rocks.put(name, rock);
        return this;
    }

    public SettingsBuilder withRocks(Map<String, Holder.Reference<RockSettings>> rocks) {
        this.rocks.putAll(rocks);
        return this;
    }

    public SettingsBuilder bottomRocks(String... rocks) {
        return add(bottom, rocks);
    }

    public SettingsBuilder layer(String id, Map<String, String> mapping) {
        layers.add(new RockLayerSettings.LayerData(id, mapping));
        return this;
    }

    public SettingsBuilder oceanFloorLayers(String... layers) {
        return add(oceanFloor, layers);
    }

    public SettingsBuilder landLayers(String... layers) {
        return add(land, layers);
    }

    public SettingsBuilder volcanicLayers(String... layers) {
        return add(volcanic, layers);
    }

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
