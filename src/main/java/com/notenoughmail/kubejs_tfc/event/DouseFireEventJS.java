package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@Info("""
        Fired whenever an attempt is made to douse a fire, can be cancelled to prevent the dousing action
        """)
@SuppressWarnings("unused")
public class DouseFireEventJS extends LevelEventJS {

    private BlockContainerJS block;

    private final DouseFireEvent event;

    public DouseFireEventJS(DouseFireEvent event) {
        this.event = event;
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    public BlockPos getPos() {
        return event.getPos();
    }

    public BlockContainerJS getBlock() {
        if (block == null) {
            block = new BlockContainerJS(event.getLevel(), event.getPos());
        }
        return block;
    }

    @Info(value = "Returns the bounds of the dousing action")
    public AABB getBounds() {
        return event.getBounds();
    }

    @Nullable
    public Player getPlayer() {
        return event.getPlayer();
    }
}
