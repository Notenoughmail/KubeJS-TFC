package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendarTickable;
import net.minecraft.world.level.Level;

// UNUSED
public interface ICalendarTickableJS extends ICalendarTickable {

    @Override
    default void checkForCalendarUpdate() {
        final Level level = getEntity().getLevel();
        if (level != null && !level.isClientSide()) {
            final long thisTick = Calendars.SERVER.getTicks();
            final long lastTick = getLastCalendarUpdateTick();
            final long tickDelta = thisTick - lastTick;
            if (lastTick != Integer.MIN_VALUE && tickDelta != 1) {
                onCalendarUpdate(tickDelta - 1);
            }
            setLastCalendarUpdateTick(thisTick);
            markDirty();
        }
    }

    @Override
    default void markDirty() {
        getEntity().setChanged();
    }

    @HideFromJS
    BlockEntityJS getEntity();
}
