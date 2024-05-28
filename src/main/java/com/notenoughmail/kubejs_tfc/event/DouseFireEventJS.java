package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class DouseFireEventJS extends LevelEventJS {

    private final Level level;
    private final BlockContainerJS block;
    private final AABB bounds;
    @Nullable
    private final Player player;

    public DouseFireEventJS(DouseFireEvent event) {
        level = event.getLevel();
        block = new BlockContainerJS(level, event.getPos());
        bounds = event.getBounds();
        player = event.getPlayer();
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public BlockContainerJS getBlock() {
        return block;
    }

    @Info(value = "Returns the bounds of the dousing action")
    public AABB getBounds() {
        return bounds;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }
}
