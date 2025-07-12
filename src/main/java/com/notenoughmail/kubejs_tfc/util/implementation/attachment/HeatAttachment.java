package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
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

import java.util.Map;

public class HeatAttachment implements TickableAttachment, IHeatBlock {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:heat",
            TypeDescJS.object()
                    .add("temperatureCallback", new PrimitiveDescJS("QuadFunction").withGenerics(new PrimitiveDescJS("BlockEntityJS"), TypeDescJS.NUMBER, TypeDescJS.NUMBER, TypeDescJS.NUMBER, TypeDescJS.NUMBER).or(TypeDescJS.NUMBER))
                    .add("providesHeat", TypeDescJS.BOOLEAN, true),
            map -> {
                final TempCallback temp = getTemp(map);
                final boolean provide = TickableAttachment.getBool("providesHeat", map, false);
                return entity -> new HeatAttachment(temp, provide, entity);
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

    private float temperature = 0F;
    private long lastCalendarTick;
    private final TempCallback temp;
    private final boolean provide;

    public HeatAttachment(TempCallback temp, boolean provide, BlockEntityJS entity) {
        this.temp = temp;
        this.provide = provide;
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

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntityJS be) {
        final long currentTick = Calendars.get(level).getCalendarTicks();
        final float t = temp.get(be, temperature, currentTick, currentTick - lastCalendarTick);
        if (t >= 0F) {
            temperature = t;
        }
        if (provide) {
            HeatCapability.provideHeatTo(level, pos.relative(Direction.UP), temperature);
        }
        lastCalendarTick = currentTick;
        be.save();
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = TickableAttachment.super.writeAttachment();
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
