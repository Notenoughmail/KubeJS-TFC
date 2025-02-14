package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.helpers.ducks.IDayMixin;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.calendar.Day;
import net.dries007.tfc.util.calendar.Month;

@Info(value = "Add and remove birthdays from the in-game calendar")
@SuppressWarnings("unused")
public class BirthdayEventJS extends EventJS {

    private static final IDayMixin duck = ((IDayMixin) (Object) Day.MONDAY);

    @Info(value = "Adds a new birthday")
    public void add(Month month, int day, String name) {
        duck.kubejs_tfc$Add(month, day, name);
    }

    @Info(value = "Removes the birthday from the given month and day")
    public void remove(Month month, int day) {
        duck.kubejs_tfc$Remove(month, day);
    }

    // For when you want to be very mean
    @Info(value = "Removes all birthdays")
    public void removeAll() {
        duck.kubejs_tfc$Clear();
    }
}
