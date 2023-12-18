package com.notenoughmail.kubejs_tfc.util.implementation.ducks;

import net.dries007.tfc.util.calendar.Month;

public interface IDayMixin {

    void kubeJS_TFC$Add(Month month, int day, String name);

    void kubeJS_TFC$Remove(Month month, int day);

    void kubeJS_TFC$Clear();
}
