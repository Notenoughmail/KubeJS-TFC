package com.notenoughmail.kubejs_tfc.util.implementation;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.event.RegisterClimateModelEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;

public class KubeJSClimateModel implements ClimateModel {

    protected final ResourceLocation name;
    public LevelPos2FloatCallback averageTemperature = (level, pos) -> 0.0F;
    public LevelPos2FloatCallback averageRainfall = (level, pos) -> 0.0F;
    public TemperatureCallback currentTemperature = (level, pos, ticks, days) -> 0.0F;
    public LevelPosLong2FloatCallback airFog = (level, pos, ticks) -> 0.0F;
    public LevelPosLong2FloatCallback waterFog = (level, pos, ticks) -> 1.0F;
    protected long climateSeed = 0L;

    public KubeJSClimateModel(ResourceLocation name) {
        this.name = name;
    }

    public void setCurrentTemperatureCalculation(TemperatureCallback callback) {
        currentTemperature = callback;
    }

    public void setAverageTemperatureCalculation(LevelPos2FloatCallback callback) {
        averageTemperature = callback;
    }

    public void setAverageRainfallCalculation(LevelPos2FloatCallback callback) {
        averageRainfall = callback;
    }

    public void setAirFog(LevelPosLong2FloatCallback callback) {
        airFog = callback;
    }

    public void setWaterFog(LevelPosLong2FloatCallback callback) {
        waterFog = callback;
    }

    public long getClimateSeed() {
        return climateSeed; // Can be accessed, but not modified
    }

    @HideFromJS
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
        if (airFog == null) {
            return ClimateModel.super.getFogginess(level, pos, calendarTime);
        }
        return Mth.clamp(airFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @HideFromJS
    @Override
    public float getWaterFogginess(LevelReader level, BlockPos pos, long calendarTime) {
        if (waterFog == null) {
            return ClimateModel.super.getWaterFogginess(level, pos, calendarTime);
        }
        return Mth.clamp(waterFog.getValue(level, pos, calendarTime), 0.0F, 1.0F);
    }

    @HideFromJS
    @Override
    public void onWorldLoad(ServerLevel level) {
        climateSeed = LinearCongruentialGenerator.next(level.getSeed(), name.hashCode() * 4621445665421L);
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

    @HideFromJS
    public boolean isValid() {
        if (averageTemperature == null) {
            error("average temperature");
            return false;
        }
        if (currentTemperature == null) {
            error("current temperature");
            return false;
        }
        if (averageRainfall == null) {
            error("average rainfall");
            return false;
        }
        return true;
    }

    protected void error(String object) {
        KubeJSTFC.LOGGER.error("The climate model {} does not specify an {}, it will not be registered!", name, object);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{'" + name + "'}@" + Integer.toHexString(hashCode());
    }

    @FunctionalInterface
    public interface LevelPos2FloatCallback {
        float getValue(LevelReader level, BlockPos pos);
    }

    @FunctionalInterface
    public interface TemperatureCallback {
        float getValue(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth);
    }

    @FunctionalInterface
    public interface LevelPosLong2FloatCallback {
        float getValue(LevelReader level, BlockPos pos, long calendarTicks);
    }
}