package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.helpers.ducks.IDayMixin;
import net.dries007.tfc.util.calendar.Day;
import net.dries007.tfc.util.calendar.Month;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * <b>Purpose:</b><p>
 * Enable adding/removing birthdays to the in-game calendar screen
 */
@Mixin(value = Day.class, remap = false)
public abstract class DayMixin implements IDayMixin {

    @Shadow(remap = false)
    @Final
    private static Map<String, String> BIRTHDAYS;

    @Override
    public void kubejs_tfc$Add(Month month, int day, String name) {
        BIRTHDAYS.put(month.name() + day, name);
    }

    @Override
    public void kubejs_tfc$Remove(Month month, int day) {
        BIRTHDAYS.remove(month.name() + day);
    }

    @Override
    public void kubejs_tfc$Clear() {
        BIRTHDAYS.clear();
    }
}
