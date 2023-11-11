package com.notenoughmail.kubejs_tfc.util.implementation.wrapper;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ClimateWrapper {

    @Nullable
    public static ClimateModel getModel(Object o) {
        if (o instanceof ClimateModel climate ) {
            return climate;
        } else if (o instanceof CharSequence || o instanceof ResourceLocation) {
            var model = Climate.create(new ResourceLocation(o.toString()));
            if (model instanceof BiomeBasedClimateModel) {
                KubeJSTFC.LOGGER.warn("Object {} of type {} returned a biome-based climate model, this may be intentional; if so, this can be safely ignored", o, o.getClass());
            }
            return model;
        } else if (o instanceof Level level) {
            return Climate.model(level);
        } else if (o instanceof ClimateModelType type) {
            return type.create();
        }
        return null;
    }

    public static ResourceLocation getName(ClimateModel model) {
        return Climate.getId(model);
    }

    public static float getCurrentTemperature(BlockContainerJS block) {
        return getTemperature(block, Calendars.get(block.minecraftLevel).getCalendarTicks());
    }

    public static float getTemperature(BlockContainerJS block, long calendarTick) {
        return Climate.getTemperature(block.minecraftLevel, block.getPos(), calendarTick, Calendars.get(block.minecraftLevel).getCalendarDaysInMonth());
    }

    public static float getAverageTemperature(BlockContainerJS block) {
        return Climate.getAverageTemperature(block.minecraftLevel, block.getPos());
    }

    public static float getAverageRainfall(BlockContainerJS block) {
        return Climate.getRainfall(block.minecraftLevel, block.getPos());
    }

    public static float getFogginess(BlockContainerJS block) {
        return Climate.getFogginess(block.minecraftLevel, block.getPos());
    }

    public static float getWaterFoginess(BlockContainerJS block) {
        return Climate.getWaterFogginess(block.minecraftLevel, block.getPos());
    }

    public static boolean isWarmEnoughToRain(BlockContainerJS block) {
        return Climate.warmEnoughToRain(block.minecraftLevel, block.getPos());
    }
}