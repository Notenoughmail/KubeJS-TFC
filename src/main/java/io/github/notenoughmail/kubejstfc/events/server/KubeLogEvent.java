package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.LoggingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Info("""
        Fires when a tree is about to be felled by an axe. Cancelling causes the block itself to drop with no side effects
        """)
@SuppressWarnings("unused")
public class KubeLogEvent implements KubeLevelEvent {

    private final Level level;
    private final LoggingEvent event;

    public KubeLogEvent(Level level, LoggingEvent event) {
        this.level = level;
        this.event = event;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public ItemStack getAxe() {
        return event.getAxe();
    }

    public BlockPos getPos() {
        return event.getPos();
    }
}