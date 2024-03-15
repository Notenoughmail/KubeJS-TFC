package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.notenoughmail.kubejs_tfc.util.helpers.ducks.IDayMixin;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.calendar.Day;
import net.dries007.tfc.util.calendar.Month;

public class BirthdayEventJS extends EventJS {

    private static final IDayMixin duck = ((IDayMixin) (Object) Day.MONDAY);

    @Info(value = "Adds a new birthday")
    public void add(Month month, int day, String name) {
        duck.kubeJS_TFC$Add(month, day, name);
    }

    @Info(value = "Removes the birthday from the given month and day")
    public void remove(Month month, int day) {
        duck.kubeJS_TFC$Remove(month, day);
    }

    // For when you want to be very mean
    @Info(value = "Removes all birthdays")
    public void removeAll() {
        duck.kubeJS_TFC$Clear();
    }
}
