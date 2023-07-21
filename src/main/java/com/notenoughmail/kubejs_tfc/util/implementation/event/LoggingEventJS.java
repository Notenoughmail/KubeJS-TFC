package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.server.ServerEventJS;
import net.dries007.tfc.util.events.LoggingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class LoggingEventJS extends EventJS {

    private final LoggingEvent event;

    public LoggingEventJS(LoggingEvent event) {
        this.event = event;
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    public LevelAccessor getLevel() {
        return event.getLevel();
    }

    public BlockPos getPos() {
        return event.getPos();
    }

    public BlockState getState() {
        return event.getState();
    }

    public ItemStackJS getAxe() {
        return new ItemStackJS(event.getAxe());
    }
}