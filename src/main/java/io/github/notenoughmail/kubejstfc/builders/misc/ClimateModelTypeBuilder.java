package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@ReturnsSelf
public class ClimateModelTypeBuilder extends BuilderBase<ClimateModelType<ClimateModelTypeBuilder.Model>> {

    public static final Map<ResourceLocation, BiFunction<Float, Boolean, ClimateModel>> modelFactories = new HashMap<>();

    public transient WindFunction wind = (m, l, p, c, d, w) -> Vec2.ZERO;
    public transient TimelessValueFunction fog = (m, l, p) -> 0F;
    public transient TimelessValueFunction avgTemp = (m, l, p) -> 0F;
    public transient TimelessValueFunction avgRain = (m, l, p) -> 0F;
    public transient TimelessValueFunction rainVariance = (m, l, p) -> 0F;
    public transient ThunderFunction isThundering = (m, l) -> false;
    public transient RainIntensityFunction rainIntensity = (m, l) -> -1F;
    public transient ValueFunction instTemp = (m, l, p, c, d) -> 0F;
    public transient ValueFunction instRain = (m, l, p, c, d) -> 0F;
    public transient TimelessValueFunction baseGroundwater = (m, l, p) -> 0F;

    public ClimateModelTypeBuilder(ResourceLocation id) {
        super(id);
        modelFactories.put(id, Model::new);
    }

    @Info("The wind calculation of this model")
    public ClimateModelTypeBuilder wind(WindFunction wind) {
        this.wind = wind;
        return this;
    }

    @Info("The fog calculation of this model")
    public ClimateModelTypeBuilder fog(TimelessValueFunction fog) {
        this.fog = fog;
        return this;
    }

    @Info("The fog calculation of this model")
    public ClimateModelTypeBuilder calendarFog(ValueFunction fog) {
        this.fog = (m, l, p) -> {
            final ICalendar calendar = Calendars.get(l);
            return fog.get(m, l, p, calendar.getCalendarTicks(), calendar.getCalendarDaysInMonth());
        };
        return this;
    }

    @Info("The yearly average temperature calculation of this model")
    public ClimateModelTypeBuilder averageTemperature(TimelessValueFunction temp) {
        avgTemp = temp;
        return this;
    }

    @Info("The yearly average rainfall calculation of this model")
    public ClimateModelTypeBuilder averageRainfall(TimelessValueFunction rain) {
        avgRain = rain;
        return this;
    }

    @Info("The rain variance of this model")
    public ClimateModelTypeBuilder rainVariance(TimelessValueFunction variance) {
        rainVariance = variance;
        return this;
    }

    @Info("The thunder calculation of this model")
    public ClimateModelTypeBuilder thunder(ThunderFunction thunder) {
        isThundering = thunder;
        return this;
    }

    @Info("The rain intensity calculation of this model")
    public ClimateModelTypeBuilder rainIntensity(RainIntensityFunction intensity) {
        rainIntensity = intensity;
        return this;
    }

    @Info("The current temperature calculation of this model")
    public ClimateModelTypeBuilder instantaneousTemperature(ValueFunction temp) {
        instTemp = temp;
        return this;
    }

    @Info("The temporally-local average rainfall calculation of this model")
    public ClimateModelTypeBuilder instantaneousRainfall(ValueFunction rain) {
        instRain = rain;
        return this;
    }

    @Info("The rainfall-equivalent groundwater calculation of this model")
    public ClimateModelTypeBuilder baseGroundwater(TimelessValueFunction groundwater) {
        baseGroundwater = groundwater;
        return this;
    }

    @Override
    public ClimateModelType<Model> createObject() {
        return new ClimateModelType<>(StreamCodec.of(
                (buf, model) -> {
                    buf.writeFloat(model.hemisphereScale());
                    buf.writeBoolean(model.supportsRain());
                },
                buf -> new Model(buf.readFloat(), buf.readBoolean())
        ));
    }

    @FunctionalInterface
    public interface ThunderFunction {
        boolean isThundering(ClimateModel model, long calendarTick);
    }

    @FunctionalInterface
    public interface RainIntensityFunction {
        float intensity(ClimateModel model, long calendarTick);
    }

    @FunctionalInterface
    public interface Wind {
        Vec2 blow(float x, float z);
    }

    @FunctionalInterface
    public interface WindFunction {

        Vec2 blow(ClimateModel model, Level level, BlockPos pos, long calendarTick, int daysInMonth, Wind wind);
    }

    @FunctionalInterface
    public interface ValueFunction {

        float get(ClimateModel model, LevelReader level, BlockPos pos, long calendarTick, int daysInMonth);

        default float clamp(ClimateModel model, LevelReader level, BlockPos pos, long calendarTick, int daysInMonth, float min, float max) {
            return Mth.clamp(get(model, level, pos, calendarTick, daysInMonth), min, max);
        }
    }

    @FunctionalInterface
    public interface TimelessValueFunction {

        float get(ClimateModel model, LevelReader level, BlockPos pos);

        default float clamp(ClimateModel model, LevelReader level, BlockPos pos, float min, float max) {
            return Mth.clamp(get(model, level, pos), min, max);
        }
    }

    public class Model implements ClimateModel {

        private final float hemisphereScale;
        private final boolean supportsRain;

        Model(float hemisphereScale, boolean supportsRain) {
            this.hemisphereScale = hemisphereScale;
            this.supportsRain = supportsRain;
        }

        @Override
        public ClimateModelType<Model> type() {
            return get();
        }

        @Override
        public float getAverageTemperature(LevelReader level, BlockPos pos) {
            return avgTemp.get(this, level, pos);
        }

        @Override
        public float getAverageRainfall(LevelReader level, BlockPos pos) {
            return avgRain.clamp(this, level, pos, 0F, Float.MAX_VALUE); // The javadocs *claim* this is limited to [0, 500], but looking around there doesn't appear to be anything that will break with higher values
        }

        @Override
        public float hemisphereScale() {
            return hemisphereScale;
        }

        @Override
        public float getTemperature(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return instTemp.get(this, level, pos, calendarTicks, daysInMonth);
        }

        @Override
        public float getRainfallVariance(LevelReader level, BlockPos pos) {
            return rainVariance.clamp(this, level, pos, -1F, 1F);
        }

        @Override
        public float getRainfall(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return instRain.clamp(this, level, pos, calendarTicks, daysInMonth, 0F, Float.MAX_VALUE);
        }

        // These are "rainfall-equivalent" groundwater values?
        @Override
        public float getBaseGroundwater(LevelReader level, BlockPos pos) {
            return baseGroundwater.clamp(this, level, pos, 0F, Float.MAX_VALUE);
        }

        @Override
        public float getAverageGroundwater(LevelReader level, BlockPos pos) {
            return getBaseGroundwater(level, pos) + getAverageRainfall(level, pos);
        }

        // The name of this method fills me with rage
        @Override
        public float getGroundwater(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return getBaseGroundwater(level, pos) + getRainfall(level, pos, calendarTicks, daysInMonth);
        }

        @Override
        public float getRain(long calendarTicks) {
            return rainIntensity.intensity(this, calendarTicks);
        }

        @Override
        public boolean getThunder(long calendarTicks) {
            return isThundering.isThundering(this, calendarTicks);
        }

        @Override
        public boolean supportsRain() {
            return supportsRain;
        }

        @Override
        public Vec2 getWind(Level level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return wind.blow(this, level, pos, calendarTicks, daysInMonth, Vec2::new);
        }

        @Override
        public float getFog(LevelReader level, BlockPos pos) {
            return fog.clamp(this, level, pos, 0F, 1F);
        }
    }
}
