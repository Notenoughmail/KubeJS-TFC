package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.LoggingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Info("""
        Fires when a tree is about to be felled by an axe. Cancelling causes the block itself to drop with no side effects
        """)
@SuppressWarnings("unused")
public class LoggingEventJS extends EventJS {

    private final Level level;
    private BlockContainerJS block;
    private final LoggingEvent event;

    public LoggingEventJS(Level level, LoggingEvent event) {
        this.level = level;
        this.event = event;
    }

    public Level getLevel() {
        return level;
    }

    public ItemStack getAxe() {
        return event.getAxe();
    }

    public BlockPos getPos() {
        return event.getPos();
    }

    public BlockContainerJS getBlock() {
        if (block == null) {
            block = new BlockContainerJS(level, event.getPos());
        }
        return block;
    }
}