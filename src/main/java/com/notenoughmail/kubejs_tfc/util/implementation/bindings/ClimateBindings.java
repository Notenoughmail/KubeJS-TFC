package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.BiomeBasedClimateModel;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.openjdk.nashorn.internal.objects.NativeString;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public enum ClimateBindings {
    INSTANCE;

    @Nullable
    @Info(value = "Tries to get the climate model of the provided object. Returns null if it cannot do so")
    public ClimateModel getModel(Object o) {
        if (o instanceof ClimateModel climate) {
            return climate;
        } else if (o instanceof CharSequence || o instanceof ResourceLocation || o instanceof NativeString) {
            var model = Climate.create(new ResourceLocation(o.toString()));
            if (CommonConfig.debugMode.get() && model instanceof BiomeBasedClimateModel) {
                KubeJSTFC.LOGGER.warn("Object {} of type {} returned a biome-based climate model, this may mean that {} is not a registered climate model", o, o.getClass(), o);
            }
            return model;
        } else if (o instanceof Level level) {
            return Climate.model(level);
        } else if (o instanceof ClimateModelType type) {
            return type.create();
        } else if (o instanceof Entity entity) {
            return Climate.model(entity.level());
        }
        return null;
    }

    @Info(value = "Returns the name of the provided climate model")
    public ResourceLocation getName(ClimateModel model) {
        return Climate.getId(model);
    }

    @Info(value = "Returns the current temperature at the current level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public float getCurrentTemperature(Level level, BlockPos pos) {
        return Climate.getTemperature(level, pos);
    }

    @Info(value = "Returns the current temperature at the current level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public float getCurrentTemperature(Level level, int x, int y, int z) {
        return getCurrentTemperature(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns the temperature at the given level, position, and tick", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position"),
            @Param(name = "calendarTick", value = "The calendar tick")
    })
    public float getTemperature(Level level, BlockPos pos, long calendarTick) {
        return Climate.getTemperature(level, pos, calendarTick, Calendars.get(level).getCalendarDaysInMonth());
    }

    @Info(value = "Returns the temperature at the given level, position, and tick", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position"),
            @Param(name = "calendarTicks", value = "The calendar ticks")
    })
    public float getTemperature(Level level, int x, int y, int z, long calendarTicks) {
        return getTemperature(level, new BlockPos(x, y, z), calendarTicks);
    }

    @Info(value = "Returns the yearly average temperature at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public float getAverageTemperature(Level level, BlockPos pos) {
        return Climate.getAverageTemperature(level, pos);
    }

    @Info(value = "Returns the yearly average temperature at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public float getAverageTemperature(Level level, int x, int y, int z) {
        return getAverageTemperature(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns the yearly average rainfall at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public float getAverageRainfall(Level level, BlockPos pos) {
        return Climate.getRainfall(level, pos);
    }

    @Info(value = "Returns the yearly average rainfall at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public float getAverageRainfall(Level level, int x, int y, int z) {
        return getAverageRainfall(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns the current fogginess, in the range [0, 1], at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public float getFogginess(Level level, BlockPos pos) {
        return Climate.getFogginess(level, pos);
    }

    @Info(value = "Returns the current fogginess, in the range [0, 1], at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public float getFogginess(Level level, int x, int y, int z) {
        return getFogginess(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns the current water fogginess, in the range [0, 1], at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public float getWaterFogginess(Level level, BlockPos pos) {
        return Climate.getWaterFogginess(level, pos);
    }

    @Info(value = "Returns the current water fogginess, in the range [0, 1], at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public float getWaterFogginess(Level level, int x, int y, int z) {
        return getWaterFogginess(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns the Vec2 wind vector at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public Vec2 getWindVector(Level level, BlockPos pos) {
        return Climate.getWindVector(level, pos);
    }

    @Info(value = "Returns the Vec2 wind vector at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public Vec2 getWindVector(Level level, int x, int y, int z) {
        return getWindVector(level, new BlockPos(x, y, z));
    }

    @Info(value = "Returns true if the it is warm enough to rain at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "pos", value = "The position")
    })
    public boolean isWarmEnoughToRain(Level level, BlockPos pos) {
        return Climate.warmEnoughToRain(level, pos);
    }

    @Info(value = "Returns true if the it is warm enough to rain at the given level and position", params = {
            @Param(name = "level", value = "The level"),
            @Param(name = "x", value = "The x position"),
            @Param(name = "y", value = "The y position"),
            @Param(name = "z", value = "The z position")
    })
    public boolean isWarmEnoughToRain(Level level, int x, int y, int z) {
        return isWarmEnoughToRain(level, new BlockPos(x, y ,z));
    }
}