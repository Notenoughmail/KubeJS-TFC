package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum CalendarBindings {
    INSTANCE;

    public final int
        HOURS_IN_DAY = ICalendar.HOURS_IN_DAY,
        MONTHS_IN_YEAR = ICalendar.MONTHS_IN_YEAR,
        CALENDAR_TICKS_IN_HOUR = ICalendar.CALENDAR_TICKS_IN_HOUR,
        CALENDAR_TICKS_IN_DAY = ICalendar.CALENDAR_TICKS_IN_DAY;

    @Info("Gets the current calendar, using a best-effort guess")
    public ICalendar getCalendar() {
        return Calendars.get();
    }

    @Info("Gets the calendar of the level")
    public ICalendar getCalendar(LevelReader level) {
        return Calendars.get(level);
    }

    @Info("Gets the calendar relevant calendar")
    public ICalendar getCalendar(boolean isClient) {
        return Calendars.get(isClient);
    }

    @Info("Gets the calendar of the entity's level")
    public ICalendar getCalendar(Entity entity) {
        return getCalendar(entity.level());
    }

    @Info("Gets the calendar of the block entity's level, if present")
    public ICalendar getCalendar(BlockEntity be) {
        return be.hasLevel() ? getCalendar(be.getLevel()) : getCalendar();
    }

    @Info("Gets the HH:MM month day year formatted date time at the given calendar tick and number of days per month")
    public MutableComponent getTimeAndDate(long calendarTick, long daysInMonth) {
        return ICalendar.getTimeAndDate(calendarTick, daysInMonth);
    }

    @Info("Gets the HH:MM formatted time of day at the given calendar tick")
    public MutableComponent getDayTime(long calendarTick) {
        return ICalendar.getDayTime(calendarTick);
    }

    @Info("Gets a component describing the given calendar duration and number of days in a month")
    public MutableComponent getTimeDelta(long calendarTicks, int daysInMonth) {
        return ICalendar.getTimeDelta(calendarTicks, daysInMonth);
    }

    @Info("Gets the number of calendar days which have passed since the start of the world at the given calendar tick")
    public long getTotalCalendarDays(long calendarTick) {
        return ICalendar.getTotalCalendarDays(calendarTick);
    }

    @Info("Gets the month of year based on the calendar tick and number of days in a month")
    public Month getMonthOfYear(long calendarTick, long daysInMonth) {
        return ICalendar.getMonthOfYear(calendarTick, daysInMonth);
    }
}
