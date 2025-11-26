package io.github.notenoughmail.kubejstfc.implementation.attachments;

import dev.latvian.mods.kubejs.block.entity.*;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.bindings.CalendarBindings;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public class CalendarTrackingAttachment implements BlockEntityAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(KubeJSTFC.tfc("calendar_tracking"), Factory.class);

    public record Factory() implements BlockEntityAttachmentFactory {

        @Override
        public CalendarTrackingAttachment create(BlockEntityAttachmentInfo info, KubeBlockEntity entity) {
            return new CalendarTrackingAttachment(entity);
        }
    }

    private long calendarTick;
    private final KubeBlockEntity entity;
    private final Watch watch;

    public CalendarTrackingAttachment(KubeBlockEntity entity) {
        this.entity = entity;
        watch = new Watch() {
            @Override
            public void set() {
                set(CalendarBindings.INSTANCE.getCalendar(CalendarTrackingAttachment.this.entity).getCalendarTicks());
            }

            @Override
            public void set(long tick) {
                calendarTick = tick;
            }

            @Override
            public long get() {
                return calendarTick;
            }
        };
        watch.reset();
    }

    @Override
    public Watch getWrappedObject() {
        return watch;
    }

    @Override
    @Nullable
    public Tag serialize(HolderLookup.Provider registries) {
        return LongTag.valueOf(calendarTick);
    }

    @Override
    public void deserialize(HolderLookup.Provider registries, @Nullable Tag tag) {
        if (tag instanceof LongTag l) {
            calendarTick = l.getAsLong();
        }
    }

    public interface Watch {

        void set();

        void set(long calendarTick);

        long get();

        default void reset() {
            set(-1L);
        }
    }
}
