package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelReader;

// TODO: JSDocs
@SuppressWarnings("unused")
public enum CalendarBindings {
    @HideFromJS
    INSTANCE;

    public ICalendar getCalendar() {
        return Calendars.get();
    }

    public ICalendar getCalendar(boolean clientSide) {
        return Calendars.get(clientSide);
    }

    public ICalendar getCalendar(LevelReader level) {
        return Calendars.get(level);
    }

    public long getCalendarTicksInMonth(int daysInMonth) {
        return ICalendar.getCalendarTicksInMonth(daysInMonth);
    }

    public long getCalendarTicksInYear(int daysInMonth) {
        return ICalendar.getCalendarTicksInYear(daysInMonth);
    }

    public float getTotalMinutes(long time) {
        return ICalendar.getTotalMinutes(time);
    }

    public long getTotalHours(long time) {
        return ICalendar.getTotalHours(time);
    }

    public long getTotalDays(long time) {
        return ICalendar.getTotalDays(time);
    }

    public long getTotalMonths(long time, long daysInMonth) {
        return ICalendar.getTotalMonths(time, daysInMonth);
    }

    public long getTotalYears(long time, long daysInMonth) {
        return ICalendar.getTotalYears(time, daysInMonth);
     }


    public int getMinuteOfHour(long time) {
        return ICalendar.getMinuteOfHour(time);
    }

    public int getHourOfDay(long time) {
        return ICalendar.getHourOfDay(time);
    }

    public int getDayOfMonth(long time, long daysInMonth) {
        return ICalendar.getDayOfMonth(time, daysInMonth);
    }

    public float getFractionOfMonth(long time, long daysInMonth) {
        return ICalendar.getFractionOfMonth(time, daysInMonth);
    }

    public float getFractionOfYear(long time, long daysInMonth) {
        return ICalendar.getFractionOfYear(time, daysInMonth);
    }

    public Month getMonthOfYear(long time, long daysInMonth) {
        return ICalendar.getMonthOfYear(time, daysInMonth);
    }

    public MutableComponent getTimeAndDate(long time, long daysInMonth) {
        return ICalendar.getTimeAndDate(time, daysInMonth);
    }

    public MutableComponent getTimeAndDate(int hour, int minute, Month month, int day, long years) {
        return ICalendar.getTimeAndDate(hour, minute, month, day, years);
    }

    public MutableComponent getTimeDelta(long ticks, int daysInMonth) {
        return ICalendar.getTimeDelta(ticks, daysInMonth);
    }
}
