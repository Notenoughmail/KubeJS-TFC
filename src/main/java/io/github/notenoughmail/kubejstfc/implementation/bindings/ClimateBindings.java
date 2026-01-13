package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;

public enum ClimateBindings {
    INSTANCE;

    @Info("Gets the model of the level")
    public ClimateModel getModel(Level level) {
        return Climate.get(level);
    }

    @Info("Gets the current temperature at the level and position")
    public float getCurrentTemperature(Level level, BlockPos pos) {
        final ICalendar calendar = Calendars.get(level);
        return getModel(level).getInstantTemperature(level, pos, calendar.getCalendarTicks(), calendar.getCalendarDaysInMonth());
    }

    @Info("Gets the yearly average temperature at the given level and position")
    public float getAverageTemperature(Level level, BlockPos pos) {
        return getModel(level).getAverageTemperature(level, pos);
    }

    @Info("Gets the current rainfall at the given level and position")
    public float getCurrentRainfall(Level level, BlockPos pos) {
        return getModel(level).getInstantRainfall(level, pos);
    }

    @Info("Gets the yearly average rainfall at the given level and position")
    public float getAverageRainfall(Level level, BlockPos pos) {
        return getModel(level).getAverageRainfall(level, pos);
    }

    @Info("Gets the rainfall variance at the given level and position")
    public float getRainfallVariance(Level level, BlockPos pos) {
        return getModel(level).getRainfallVariance(level, pos);
    }

    @Info("Gets the current rainfall-equivalent groundwater at the given level and position")
    public float getCurrentGroundwater(Level level, BlockPos pos) {
        return getModel(level).getInstantGroundwater(level, pos);
    }

    @Info("Gets the yearly average rainfall-equivalent groundwater at the given level and position")
    public float getAverageGroundwater(Level level, BlockPos pos) {
        return getModel(level).getAverageGroundwater(level, pos);
    }

    @Info("Gets the current wind vector for the given level and position")
    public Vec2 getCurrentWind(Level level, BlockPos pos) {
        return getModel(level).getWind(level, pos);
    }

    @Info("Gets the fogginess at the given level and position, may or may not change with time")
    public float getFogginess(Level level, BlockPos pos) {
        return getModel(level).getFog(level, pos);
    }
}
