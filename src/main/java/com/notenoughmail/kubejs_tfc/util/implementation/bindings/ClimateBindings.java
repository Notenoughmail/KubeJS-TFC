package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.BiomeBasedClimateModel;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nullable;

// TODO: JSDocs
@SuppressWarnings("unused")
public enum ClimateBindings {
    @HideFromJS
    INSTANCE;

    @Nullable
    public ClimateModel getModel(Object o) {
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

    public ResourceLocation getName(ClimateModel model) {
        return Climate.getId(model);
    }

    public float getCurrentTemperature(BlockContainerJS block) {
        return getTemperature(block, Calendars.get(block.getLevel()).getCalendarTicks());
    }

    public float getTemperature(BlockContainerJS block, long calendarTick) {
        return Climate.getTemperature(block.getLevel(), block.getPos(), calendarTick, Calendars.get(block.getLevel()).getCalendarDaysInMonth());
    }

    public float getAverageTemperature(BlockContainerJS block) {
        return Climate.getAverageTemperature(block.getLevel(), block.getPos());
    }

    public float getAverageRainfall(BlockContainerJS block) {
        return Climate.getRainfall(block.getLevel(), block.getPos());
    }

    public float getFogginess(BlockContainerJS block) {
        return Climate.getFogginess(block.getLevel(), block.getPos());
    }

    public float getWaterFogginess(BlockContainerJS block) {
        return Climate.getWaterFogginess(block.getLevel(), block.getPos());
    }

    public Vec2 getWindVector(BlockContainerJS block) {
        return Climate.getWindVector(block.getLevel(), block.getPos());
    }

    public boolean isWarmEnoughToRain(BlockContainerJS block) {
        return Climate.warmEnoughToRain(block.getLevel(), block.getPos());
    }
}