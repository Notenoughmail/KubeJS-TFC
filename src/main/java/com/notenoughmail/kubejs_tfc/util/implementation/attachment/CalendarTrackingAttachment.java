package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import com.notenoughmail.kubejs_tfc.util.implementation.bindings.CalendarBindings;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import net.minecraft.nbt.CompoundTag;

import java.util.function.LongSupplier;

public class CalendarTrackingAttachment implements BlockEntityAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:calendar",
            TypeDescJS.object()
                    .add("defaultDuration", TypeDescJS.NUMBER, true),
            map -> {
                final long length = map.containsKey("defaultDuration") ? ((Number) map.get("defaultDuration")).longValue() : -1L;
                return entity -> new CalendarTrackingAttachment(length, entity);
            }
    );

    private final long defaultDuration;
    private final LongSupplier timeGetter;
    private long calendarTick = -1L, duration;

    public CalendarTrackingAttachment(long defaultDuration, BlockEntityJS be) {
        this.defaultDuration = defaultDuration;
        timeGetter = () -> CalendarBindings.INSTANCE.getCalendar(be).getCalendarTicks();
        duration = defaultDuration;
    }

    public void startTiming(long duration) {
        this.duration = duration;
        calendarTick = timeGetter.getAsLong();
    }

    public void startTiming() {
        startTiming(defaultDuration);
    }

    public long getCalendarTick() {
        return calendarTick;
    }

    public boolean hasDurationElapsed() {
        return (timeGetter.getAsLong() - calendarTick) > duration;
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = BlockEntityAttachment.super.writeAttachment();
        tag.putLong("calendarTick", calendarTick);
        tag.putLong("duration", duration);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        BlockEntityAttachment.super.readAttachment(tag);
        calendarTick = tag.getLong("calendarTick");
        duration = tag.getLong("duration");
    }
}
