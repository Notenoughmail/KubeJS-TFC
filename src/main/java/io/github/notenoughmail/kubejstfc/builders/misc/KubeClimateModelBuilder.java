package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
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
import java.util.function.LongPredicate;

// TODO 2.0.0 | This needs to change
public class KubeClimateModelBuilder extends BuilderBase<ClimateModelType<KubeClimateModelBuilder.Model>> {

    public static final Map<String, BiFunction<Float, Boolean, ClimateModel>> modelInstances = new HashMap<>();

    public transient WindFunction wind = (m, l, p, c, d) -> Vec2.ZERO;
    public transient TimelessValueFunction fog = (m, l, p) -> 0F;
    public transient TimelessValueFunction avgTemp = (m, l, p) -> 0F;
    public transient TimelessValueFunction avgRain = (m, l, p) -> 0F;
    public transient TimelessValueFunction rainVariance = (m, l, p) -> 0F;
    public transient LongPredicate isThundering = l -> false;
    public transient Long2FloatFunction rainIntensity = l -> 0F;
    public transient ValueFunction temp = (m, l, p, c, d) -> 0F;
    public transient ValueFunction variableRain = (m, l, p, c, d) -> 0F;
    public transient TimelessValueFunction baseGroundwater = (m, l, p) -> 0F;

    public KubeClimateModelBuilder(ResourceLocation id) {
        super(id);
        modelInstances.put(id.toString(), Model::new);
    }

    @Info("The wind calculation of this model")
    public KubeClimateModelBuilder wind(WindFunction wind) {
        this.wind = wind;
        return this;
    }

    @Info("The fog calculation of this model")
    public KubeClimateModelBuilder fog(TimelessValueFunction fog) {
        this.fog = fog;
        return this;
    }

    @Info("The fog calculation of this model")
    public KubeClimateModelBuilder calendarFog(ValueFunction fog) {
        this.fog = (m, l, p) -> {
            final ICalendar calendar = Calendars.get(l);
            return fog.get(m, l, p, calendar.getCalendarTicks(), calendar.getCalendarDaysInMonth());
        };
        return this;
    }

    @Info("The yearly average temperature calculation of this model")
    public KubeClimateModelBuilder averageTemperature(TimelessValueFunction temp) {
        avgTemp = temp;
        return this;
    }

    @Info("The yearly average rainfall calculation of this model")
    public KubeClimateModelBuilder averageRainfall(TimelessValueFunction rain) {
        avgRain = rain;
        return this;
    }

    @Info("The rain variance of this model")
    public KubeClimateModelBuilder rainVariance(TimelessValueFunction variance) {
        rainVariance = variance;
        return this;
    }

    @Info("The thunder calculation of this model")
    public KubeClimateModelBuilder thunder(LongPredicate thunder) {
        isThundering = thunder;
        return this;
    }

    @Info("The rain intensity calculation of this model")
    public KubeClimateModelBuilder rainIntensity(Long2FloatFunction intensity) {
        rainIntensity = intensity;
        return this;
    }

    @Info("The current temperature calculation of this model")
    public KubeClimateModelBuilder currentTemperature(ValueFunction temp) {
        this.temp = temp;
        return this;
    }

    @Info("The temporally-local average rainfall calculation of this model")
    public KubeClimateModelBuilder variableRainfall(ValueFunction rain) {
        variableRain = rain;
        return this;
    }

    @Info("The rainfall-equivalent groundwater calculation of this model")
    public KubeClimateModelBuilder baseGroundwater(TimelessValueFunction groundwater) {
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
    public interface WindFunction {

        Vec2 blow(ClimateModel model, Level level, BlockPos pos, long calendarTicks, int daysInMonth);
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
            return temp.get(this, level, pos, calendarTicks, daysInMonth);
        }

        @Override
        public float getRainfallVariance(LevelReader level, BlockPos pos) {
            return rainVariance.clamp(this, level, pos, -1F, 1F);
        }

        @Override
        public float getRainfall(LevelReader level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return variableRain.clamp(this, level, pos, calendarTicks, daysInMonth, 0F, Float.MAX_VALUE);
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
            return rainIntensity.get(calendarTicks);
        }

        @Override
        public boolean getThunder(long calendarTicks) {
            return isThundering.test(calendarTicks);
        }

        @Override
        public boolean supportsRain() {
            return supportsRain;
        }

        @Override
        public Vec2 getWind(Level level, BlockPos pos, long calendarTicks, int daysInMonth) {
            return wind.blow(this, level, pos, calendarTicks, daysInMonth);
        }

        @Override
        public float getFog(LevelReader level, BlockPos pos) {
            return fog.clamp(this, level, pos, 0F, 1F);
        }
    }
}
