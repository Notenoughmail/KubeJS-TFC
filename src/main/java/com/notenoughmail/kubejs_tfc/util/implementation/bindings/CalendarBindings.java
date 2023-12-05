package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelReader;

@SuppressWarnings("unused")
public enum CalendarBindings {
    INSTANCE;

    @Info(value = "Returns the best guess on the appropriate calendar")
    public ICalendar getCalendar() {
        return Calendars.get();
    }

    @Info(value = "Returns the client or server calendar based on the provided boolean", params = @Param(name = "isClientSide", value = "If the calendar is client or server"))
    public ICalendar getCalendar(boolean isClientSide) {
        return Calendars.get(isClientSide);
    }

    @Info(value = "Returns the level's calendar")
    public ICalendar getCalendar(LevelReader level) {
        return Calendars.get(level);
    }

    @Info(value = "Returns the number of calendar ticks in a month based on the number of days in a month")
    public long getCalendarTicksInMonth(int daysInMonth) {
        return ICalendar.getCalendarTicksInMonth(daysInMonth);
    }

    @Info(value = "Returns the number of calendar ticks in a year based on the number of days in a month")
    public long getCalendarTicksInYear(int daysInMonth) {
        return ICalendar.getCalendarTicksInYear(daysInMonth);
    }

    @Info(value = "Returns the number of minutes for the provided number of ticks")
    public float getTotalMinutes(long time) {
        return ICalendar.getTotalMinutes(time);
    }

    @Info(value = "Returns the number of hours for the provided number of ticks")
    public long getTotalHours(long time) {
        return ICalendar.getTotalHours(time);
    }

    @Info(value = "Returns the number of days for the provided number of ticks")
    public long getTotalDays(long time) {
        return ICalendar.getTotalDays(time);
    }

    @Info(value = "Returns the number months for the provided number of ticks and number of days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public long getTotalMonths(long time, long daysInMonth) {
        return ICalendar.getTotalMonths(time, daysInMonth);
    }

    @Info(value = "Returns the number of years for the provided number of ticks and number of days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public long getTotalYears(long time, long daysInMonth) {
        return ICalendar.getTotalYears(time, daysInMonth);
     }

    @Info(value = "Returns the minute of the hour for the given number of ticks")
    public int getMinuteOfHour(long time) {
        return ICalendar.getMinuteOfHour(time);
    }

    @Info(value = "Returns the hour of the day for the given number of ticks")
    public int getHourOfDay(long time) {
        return ICalendar.getHourOfDay(time);
    }

    @Info(value = "Returns the day of the month for the given number of ticks and days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public int getDayOfMonth(long time, long daysInMonth) {
        return ICalendar.getDayOfMonth(time, daysInMonth);
    }

    @Info(value = "Returns the fraction of the month for the provided number of ticks and days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public float getFractionOfMonth(long time, long daysInMonth) {
        return ICalendar.getFractionOfMonth(time, daysInMonth);
    }

    @Info(value = "Returns ths fraction of the year for the provided number of ticks and days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public float getFractionOfYear(long time, long daysInMonth) {
        return ICalendar.getFractionOfYear(time, daysInMonth);
    }

    @Info(value = "Returns the month of the year for the provided number of ticks and days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "Th number of days in a month")
    })
    public Month getMonthOfYear(long time, long daysInMonth) {
        return ICalendar.getMonthOfYear(time, daysInMonth);
    }

    @Info(value = "Returns a text component describing the given number of ticks and days in a month", params = {
            @Param(name = "time", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public MutableComponent getTimeAndDate(long time, long daysInMonth) {
        return ICalendar.getTimeAndDate(time, daysInMonth);
    }

    @Info(value = "Returns a text component describing the given hour, minute, month, day, and year", params = {
            @Param(name = "hour", value = "The number of hours"),
            @Param(name = "minute", value = "The number of minutes"),
            @Param(name = "month", value = "The month"),
            @Param(name = "day", value = "The day of the month"),
            @Param(name = "years", value = "The number of years")
    })
    public MutableComponent getTimeAndDate(int hour, int minute, Month month, int day, long years) {
        return ICalendar.getTimeAndDate(hour, minute, month, day, years);
    }

    @Info(value = "Returns a text component describing the time delta of the given number of ticks and number of days in a month", params = {
            @Param(name = "ticks", value = "The number of ticks"),
            @Param(name = "daysInMonth", value = "The number of days in a month")
    })
    public MutableComponent getTimeDelta(long ticks, int daysInMonth) {
        return ICalendar.getTimeDelta(ticks, daysInMonth);
    }
}
