package com.notenoughmail.kubejs_tfc.util.helpers.ducks;

import net.dries007.tfc.util.calendar.Month;

public interface IDayMixin {

    void kubejs_tfc$Add(Month month, int day, String name);

    void kubejs_tfc$Remove(Month month, int day);

    void kubejs_tfc$Clear();
}
