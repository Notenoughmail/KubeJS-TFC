package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.desc.OrDescJS;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.NativeJavaObject;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Predicate;

public class HeatAttachment implements BlockEntityAttachment, TickableAttachment, IHeatBlock {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:heat",
            TypeDescJS.object()
                    .add("temperatureCallback", new OrDescJS(new TypeDescJS[]{ TypeDescJS.NUMBER, new PrimitiveDescJS("QuadFunction<BlockEntityJS, number, number, number, number>")}))
                    .add("providesHeatTo", new OrDescJS(new TypeDescJS[]{ new PrimitiveDescJS("Direction").asArray(), new PrimitiveDescJS("Predicate<Direction>") }), true),
            map -> {
                final TempCallback temp = getTemp(map);
                final Direction[] dirs = getDirs(map);
                return entity -> new HeatAttachment(temp, dirs, entity);
            }
    );

    private static TempCallback getTemp(Map<String, Object> map) {
        final Object obj = map.get("temperatureCallback");
        if (obj instanceof BaseFunction func) {
            return (TempCallback) NativeJavaObject.createInterfaceAdapter(ScriptType.STARTUP.manager.get().context, TempCallback.class, func);
        } else if (obj instanceof Number num) {
            final float val = num.floatValue();
            return (be, t, c, d) -> val;
        } else {
            return (be, t, c, d) -> 0F;
        }
    }

    @SuppressWarnings("unchecked")
    private static Direction[] getDirs(Map<String, Object> map) {
        final Object val = map.get("providesHeatTo");
        if (val == null) {
            return new Direction[] { Direction.UP };
        } else if (val instanceof BaseFunction func) {
            final Predicate<Direction> predicate = (Predicate<Direction>) NativeJavaObject.createInterfaceAdapter(ScriptType.STARTUP.manager.get().context, Predicate.class, func);
            return Direction.stream().filter(predicate).toArray(Direction[]::new);
        } else {
            return ListJS.orSelf(val).stream().map(v -> {
                if (v instanceof Direction dir) {
                    return dir;
                } else {
                    try {
                        return Direction.valueOf(v.toString().toUpperCase(Locale.ROOT));
                    } catch (Exception e) {
                        return null;
                    }
                }
            }).filter(Objects::nonNull).toArray(Direction[]::new);
        }
    }

    private float temperature = 0F;
    private long lastCalendarTick;
    private final TempCallback temp;
    private final Direction[] dirs;

    public HeatAttachment(TempCallback temp, Direction[] dirs, BlockEntityJS entity) {
        this.temp = temp;
        this.dirs = dirs;
        lastCalendarTick = Calendars.get().getCalendarTicks();
        wrapScriptTicker(entity, true);
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    // TODO: Investigate strange things happening when 'pipe' attachment chains are broken are relinked
    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntityJS be) {
        final long currentTick = Calendars.get(level).getCalendarTicks();
        final float t = temp.get(be, temperature, currentTick, currentTick - lastCalendarTick);
        if (t >= 0F) {
            temperature = t;
        }
        for (Direction dir : dirs) {
            HeatCapability.provideHeatTo(level, pos.relative(dir), temperature);
        }
        lastCalendarTick = currentTick;
        be.sync();
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = BlockEntityAttachment.super.writeAttachment();
        tag.putFloat("temperature", temperature);
        tag.putLong("lastCalendarTick", lastCalendarTick);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        temperature = tag.getFloat("temperature");
        lastCalendarTick = tag.getLong("lastCalendarTick");
    }

    @FunctionalInterface
    public interface TempCallback {
        float get(BlockEntityJS be, float currentTemperature, long calendarTick, long calendarTicksSinceLastUpdate);
    }
}
