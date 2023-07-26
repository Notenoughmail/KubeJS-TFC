package com.notenoughmail.kubejs_tfc.util.implementation.wrapper;

import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.world.level.LevelReader;

public class CalendarWrapper implements ICalendar {

    public static ICalendar getCalendar() {
        return Calendars.get();
    }

    public static ICalendar getCalendar(boolean clientSide) {
        return Calendars.get(clientSide);
    }

    public static ICalendar getCalendar(LevelReader level) {
        return Calendars.get(level);
    }

    public static ICalendar getCalendar(LevelJS level) {
        return getCalendar(level.minecraftLevel);
    }

    // Do not use
    @HideFromJS
    @Override
    public long getTicks() {
        return 0;
    }

    @HideFromJS
    @Override
    public long getCalendarTicks() {
        return 0;
    }

    @HideFromJS
    @Override
    public int getCalendarDaysInMonth() {
        return 0;
    }
}
