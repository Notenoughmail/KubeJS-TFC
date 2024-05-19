package com.notenoughmail.kubejs_tfc.util.implementation.custom.climate;

import com.notenoughmail.kubejs_tfc.util.helpers.ducks.IOpenSimplex2dMixin;
import com.notenoughmail.kubejs_tfc.event.RegisterClimateModelEventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KubeJSClimateModel implements ClimateModel {

    protected final ResourceLocation name;
    public LevelPos2FloatCallback averageTemperature;
    public LevelPos2FloatCallback averageRainfall;
    public TemperatureCallback currentTemperature;
    public LevelPosLong2FloatCallback airFog;
    public LevelPosLong2FloatCallback waterFog;
    public WindVectorCallback windVector;
    protected long climateSeed = 0L;
    private final List<OpenSimplex2D> noises = new ArrayList<>();


    public KubeJSClimateModel(ResourceLocation name, ClimateModel defaults) {
        this.name = name;
        averageTemperature = defaults::getAverageTemperature;
        averageRainfall = defaults::getRainfall;
        currentTemperature = defaults::getTemperature;
        airFog = defaults::getFogginess;
        waterFog = defaults::getWaterFogginess;
        windVector = (block, calendarTicks) -> defaults.getWindVector(block.getLevel(), block.getPos(), calendarTicks);
    }

    @Info(value = "Sets how the model will determine the current temperature at a given position and time")
    public void setCurrentTemperatureCalculation(TemperatureCallback callback) {
        currentTemperature = callback;
    }

    @Info(value = "Sets how the model will determine the average temperature at a given position")
    public void setAverageTemperatureCalculation(LevelPos2FloatCallback callback) {
        averageTemperature = callback;
    }

    @Info(value = "Sets how the model will determine the average rainfall at a given position")
    public void setAverageRainfallCalculation(LevelPos2FloatCallback callback) {
        averageRainfall = callback;
    }

    @Info(value = "Sets how the model will determine the fogginess at a given position and time")
    public void setAirFog(LevelPosLong2FloatCallback callback) {
        airFog = callback;
    }

    @Info(value = "Sets how the model will determine the fogginess in water at a given position and time")
    public void setWaterFog(LevelPosLong2FloatCallback callback) {
        waterFog = callback;
    }

    @Info(value = "Sets how the model will determine its wind vector at the given position and time")
    public void setWindVector(WindVectorCallback callback) {
        windVector = callback;
    }

    @Info(value = "Returns the climate seed being used")
    public long getClimateSeed() {
        return climateSeed; // Can be accessed, but not modified
    }

    @Info(value = "Returns a new OpenSimplex2D noise")
    public OpenSimplex2D getNewNoise() {
        var noise = new OpenSimplex2D(0);
        noises.add(noise);
        return noise;
    }

    @NotNull
    @Override
    public ClimateModelType type() {
        return RegisterClimateModelEventJS.CUSTOM_MODELS.get(name);
    }

    @HideFromJS
    @Override
    public float getTemperature(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
        return currentTemperature.getValue(level, pos, calendarTicks, daysInMonth);
    }

    @HideFromJS
    @Override
    public float getAverageTemperature(LevelReader level, BlockPos pos) {
        return averageTemperature.getValue(level, pos);
    }

    @HideFromJS
    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        return Mth.clamp(averageRainfall.getValue(level, pos), MINIMUM_RAINFALL, MAXIMUM_RAINFALL);
    }

    @HideFromJS
    @Override
    public float getFogginess(LevelReader level, BlockPos pos, long calendarTime) {
        return Mth.clamp(airFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @HideFromJS
    @Override
    public float getWaterFogginess(LevelReader level, BlockPos pos, long calendarTime) {
        return Mth.clamp(waterFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @NotNull
    @HideFromJS
    @Override
    public Vec2 getWindVector(Level level, BlockPos pos, long calendarTime) {
        return windVector.getValue(new BlockContainerJS(level, pos), calendarTime);
    }

    @HideFromJS
    @Override
    public void onWorldLoad(ServerLevel level) {
        climateSeed = LinearCongruentialGenerator.next(level.getSeed(), name.hashCode() * 4621445665421L);
        for (int i = 0 ; i < noises.size() ; i++) {
            ((IOpenSimplex2dMixin) noises.get(i)).kubejs_tfc$SetSeed(climateSeed + (35242456354313L * i));
        }
    }

    @HideFromJS
    @Override
    public void onSyncToClient(FriendlyByteBuf buffer) {
        buffer.writeLong(climateSeed);
    }

    @HideFromJS
    @Override
    public void onReceiveOnClient(FriendlyByteBuf buffer) {
        climateSeed = buffer.readLong();
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" + name + "}@" + Integer.toHexString(hashCode());
    }

    @Info(value = "A callback which takes a LevelReader and a BlockPos and returns a number")
    @FunctionalInterface
    public interface LevelPos2FloatCallback {
        @Info(params = {
                @Param(name = "level", value = "The LevelReader"),
                @Param(name = "pos", value = "The position")
        })
        float getValue(LevelReader level, BlockPos pos);
    }

    @Info(value = "A callback which takes a LevelReader, a BlockPos, a number, and a number and returns a number")
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
                @Param(name = "block", value = "The level and position"),
                @Param(name = "calendarTicks", value = "The calendar tick during which the calculation is being made")
        })
        Vec2 getValue(BlockContainerJS block, long calendarTicks);
    }
}