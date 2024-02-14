package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.ducks.IDayMixin;
import net.dries007.tfc.util.calendar.Day;
import net.dries007.tfc.util.calendar.Month;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(value = Day.class, remap = false)
public abstract class DayMixin implements IDayMixin {

    @Shadow(remap = false)
    @Final
    private static Map<String, String> BIRTHDAYS;

    @Override
    public void kubeJS_TFC$Add(Month month, int day, String name) {
        BIRTHDAYS.put(month.name() + day, name);
    }

    @Override
    public void kubeJS_TFC$Remove(Month month, int day) {
        BIRTHDAYS.remove(month.name() + day);
    }

    @Override
    public void kubeJS_TFC$Clear() {
        BIRTHDAYS.clear();
    }
}
