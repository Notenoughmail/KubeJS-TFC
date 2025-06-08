package com.notenoughmail.kubejs_tfc.util.implementation.custom.climate;

import com.notenoughmail.kubejs_tfc.event.RegisterClimateModelEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.OverworldClimateModelAccessor;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.OverworldClimateModel;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class KubeJSClimateModel implements ClimateModel {

    private final Builder builder;
    private WindVectorCallback wind;
    private OnChunkLoadCallback chunkLoad;
    private TemperatureCallback currentTemperature;
    private LevelPos2FloatCallback averageTemp, averageRain;
    private LevelPosLong2FloatCallback waterFog, airFog;
    private Consumer<ServerLevel> worldLoad;

    private final List<Function<OpenSimplex2D, Noise2D>> noiseBuilders = new ArrayList<>();
    private final List<Noise2D> noises = new ArrayList<>();
    private ClimateModelType modelType;

    public KubeJSClimateModel(Builder builder) {
        this.builder = builder;
        wind = (level, pos, calendarTicks) -> Vec2.ZERO;
        chunkLoad = (level, chunk, chunkData) -> {};
        currentTemperature = (level, pos, calendarTicks, daysInMonth) -> 0;
        averageTemp = (level, pos) -> 0;
        averageRain = (level, pos) -> 0;
        waterFog = (level, pos, calendarTicks) -> 0;
        airFog = (level, pos, calendarTicks) -> 0;
    }

    @NotNull
    @Override
    public ClimateModelType type() {
        if (modelType == null) {
            modelType = RegisterClimateModelEventJS.CUSTOM_MODELS.get(builder.name);
        }
        return modelType;
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
        return currentTemperature.getValue(level, pos, calendarTicks, daysInMonth);
    }

    @Override
    public float getAverageTemperature(LevelReader level, BlockPos pos) {
        return averageTemp.getValue(level, pos);
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        return Mth.clamp(averageRain.getValue(level, pos), MINIMUM_RAINFALL, MAXIMUM_RAINFALL);
    }

    @Override
    public float getFogginess(LevelReader level, BlockPos pos, long calendarTime) {
        return Mth.clamp(airFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @Override
    public float getWaterFogginess(LevelReader level, BlockPos pos, long calendarTime) {
        return Mth.clamp(waterFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @NotNull
    @Override
    public Vec2 getWindVector(Level level, BlockPos pos, long calendarTime) {
        return wind.getValue(level, pos, calendarTime);
    }

    @Override
    public void onWorldLoad(ServerLevel level) {
        builder.climateSeed = LinearCongruentialGenerator.next(level.getSeed(), builder.name.hashCode() * 4621445665421L);

        noises.clear();
        for (int i = 0 ; i < noiseBuilders.size() ; i++) {
            noises.add(noiseBuilders.get(i).apply(new OpenSimplex2D(builder.climateSeed + (35242456354313L * i))));
        }

        if (level.getChunkSource().getGenerator() instanceof ChunkGeneratorExtension extension) {
            builder.tempScale = extension.settings().temperatureScale();
            builder.rainScale = extension.settings().rainfallScale();
        }

        if (worldLoad != null) {
            worldLoad.accept(level);
        }

        var access = ((OverworldClimateModelAccessor) builder.defaults);
        access.kubejs_tfc$SetClimateSeed(builder.climateSeed);
        access.kubejs_tfc$SetTemperatureScale(builder.tempScale);
        access.kubejs_tfc$UpdateNoise();
    }

    @Override
    public void onChunkLoad(WorldGenLevel level, ChunkAccess chunk, ChunkData chunkData) {
        chunkLoad.apply(level, chunk, chunkData);
    }

    @Override
    public void onSyncToClient(FriendlyByteBuf buffer) {
        buffer.writeLong(builder.climateSeed);
        buffer.writeFloat(builder.tempScale);
        buffer.writeFloat(builder.rainScale);
    }

    @Override
    public void onReceiveOnClient(FriendlyByteBuf buffer) {
        builder.climateSeed = buffer.readLong();
        builder.tempScale = buffer.readFloat();
        builder.rainScale = buffer.readFloat();
        var access = ((OverworldClimateModelAccessor) builder.defaults);
        access.kubejs_tfc$SetClimateSeed(builder.climateSeed);
        access.kubejs_tfc$SetTemperatureScale(builder.tempScale);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + builder.name + "]";
    }

    @Info("A callback which takes a LevelReader and a BlockPos and returns a number")
    @FunctionalInterface
    public interface LevelPos2FloatCallback {
        @Info(params = {
                @Param(name = "level", value = "The LevelReader"),
                @Param(name = "pos", value = "The position")
        })
        float getValue(LevelReader level, BlockPos pos);
    }

    @Info("A callback which takes a LevelReader, a BlockPos, a number, and a number and returns a number")
    @FunctionalInterface
    public interface TemperatureCallback {
        @Info(params = {
                @Param(name = "level", value = "The LevelReader"),
                @Param(name = "pos", value = "The position"),
                @Param(name = "calendarTicks", value = "The calendar tick during which the calculation is being made"),
                @Param(name = "daysInMonth", value = "The number of days in a month")
        })
        float getValue(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth);
    }

    @FunctionalInterface
    public interface LevelPosLong2FloatCallback {
        @Info(params = {
                @Param(name = "level", value = "The levelReader"),
                @Param(name = "pos", value = "The position"),
                @Param(name = "calendarTicks", value = "The calendar tick during which the calculation is being made")
        })
        float getValue(LevelReader level, BlockPos pos, long calendarTicks);
    }

    @FunctionalInterface
    public interface WindVectorCallback {
        @Info(params = {
                @Param(name = "level", value = "The level"),
                @Param(name = "pos", value = "The position"),
                @Param(name = "calendarTicks", value = "The calendar tick during which the calculation is being made")
        })
        Vec2 getValue(Level level, BlockPos pos, long calendarTicks);
    }

    @FunctionalInterface
    public interface OnChunkLoadCallback {
        @Info(params = {
                @Param(name = "level", value = "The level"),
                @Param(name = "chunk", value = "The chunk being loaded"),
                @Param(name = "chunkData", value = "Additional TFC data about the chunk, will be invalid if the level does not have a TFC-like generator")
        })
        void apply(WorldGenLevel level, ChunkAccess chunk, ChunkData chunkData);
    }

    public static class Builder {

        private final OverworldClimateModel defaults = new OverworldClimateModel();
        private final ResourceLocation name;
        private final KubeJSClimateModel model;
        private long climateSeed = 0L;

        private float tempScale = 20000F, rainScale = 20000F;

        public Builder(ResourceLocation name) {
            this.name = name;
            model = new KubeJSClimateModel(this);
        }

        @Info("Gets the temperature scale of the dimension, defaults to 20000 if it does not have a TFC-like chunk generator")
        public float getTemperatureScale() {
            return tempScale;
        }

        @Info("Gets the rainfall scale of the dimension, defaults to 20000 if it does not have a TFC-like chunk generator")
        public float getRainfallScale() {
            return rainScale;
        }

        @Info("Returns the climate seed being used")
        public long getClimateSeed() {
            return climateSeed;
        }

        @Info("Creates a new vector for use in wind calculations")
        public Vec2 vector(float x, float z) {
            return new Vec2(x, z);
        }

        @Info("Sets how the model will determine the current temperature at a given position and time")
        public void setCurrentTemperatureCalculation(TemperatureCallback callback) {
            model.currentTemperature = callback;
        }

        @Info("Sets how the model will determine the average temperature at a given position")
        public void setAverageTemperatureCalculation(LevelPos2FloatCallback callback) {
            model.averageTemp = callback;
        }

        @Info("Sets how the model will determine the average rainfall at a given position")
        public void setAverageRainfallCalculation(LevelPos2FloatCallback callback) {
            model.averageRain = callback;
        }

        @Info("Sets how the model will determine the fogginess at a given position and time")
        public void setAirFog(LevelPosLong2FloatCallback callback) {
            model.airFog = callback;
        }

        @Info("Sets how the model will determine the fogginess in water at a given position and time")
        public void setWaterFog(LevelPosLong2FloatCallback callback) {
            model.waterFog = callback;
        }

        @Info("Sets how the model will determine the wind strength at the given position and time")
        public void setWindVector(WindVectorCallback callback) {
            model.wind = callback;
        }

        @Info("Sets the model's behavior when loading into a world")
        @Generics(ServerLevel.class)
        public void setOnWorldLoad(Consumer<ServerLevel> callback) {
            model.worldLoad = callback;
        }

        @Info("Sets the model's behavior on chunk load")
        public void setOnChunkLoad(OnChunkLoadCallback callback) {
            model.chunkLoad = callback;
        }

        @Info("Adds a new Noise2D to the model, which can be retrieved later via the returned index")
        public int newNoise(Function<OpenSimplex2D, Noise2D> builder) {
            model.noiseBuilders.add(builder);
            model.noises.add(builder.apply(new OpenSimplex2D(0))); // Just so *something* exists in the case that the noises are loaded before a sync is made (fog on login...)
            return model.noiseBuilders.size() - 1;
        }

        @Info("Gets the noise at the specified index")
        public Noise2D noise(int index) {
            return model.noises.get(index);
        }

        @Info("Returns the callback used by TFC for its wind")
        public WindVectorCallback getTfcWind() {
            return defaults::getWindVector;
        }

        @Info("Returns the callback used by TFC for its chunk loading")
        public OnChunkLoadCallback getTfcChunkLoad() {
            return defaults::onChunkLoad;
        }

        @Info("Returns the callback used by TFC for its current temperature")
        public TemperatureCallback getTfcCurrentTemperature() {
            return defaults::getTemperature;
        }

        @Info("Returns the callback used by TFC for its average temperature")
        public LevelPos2FloatCallback getTfcAverageTemperature() {
            return defaults::getAverageTemperature;
        }

        @Info("Returns the callback used by TFC for its average rainfall")
        public LevelPos2FloatCallback getTfcAverageRainfall() {
            return defaults::getRainfall;
        }

        @Info("Returns the callback used by TFC for its air fog")
        public LevelPosLong2FloatCallback getTfcAirFog() {
            return defaults::getFogginess;
        }

        @Info("Returns the callback used by TFC for its water fog")
        public LevelPosLong2FloatCallback getTfcWaterFog() {
            return defaults::getWaterFogginess;
        }

        @Info("A getter for the model's current temperature")
        public float currentTemperature(LevelReader level, BlockPos pos, long calendarTicks) {
            return model.getTemperature(level, pos, calendarTicks, Calendars.get(level).getCalendarDaysInMonth());
        }

        @Info("A getter for the model's average temperature")
        public float averageTemperature(LevelReader level, BlockPos pos) {
            return model.getAverageTemperature(level, pos);
        }

        @Info("A getter for the model's average rainfall")
        public float averageRainfall(LevelReader level, BlockPos pos) {
            return model.getRainfall(level, pos);
        }

        @HideFromJS
        public KubeJSClimateModel build() {
            return model;
        }
    }
}