package io.github.notenoughmail.kubejstfc.util.mixin;

import io.github.notenoughmail.kubejstfc.implementation.extensions.ICalendarExtension;
import net.dries007.tfc.util.calendar.ICalendar;
import org.spongepowered.asm.mixin.Mixin;

/**
 * <b>Purpose:</b><p>
 * Give scripts safer access to calendar transactions
 */
@Mixin(ICalendar.class)
public interface ICalendarMixin extends ICalendarExtension {
}
