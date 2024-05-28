package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class StartFireEventJS extends PlayerEventJS {

    private final StartFireEvent event;

    public StartFireEventJS(StartFireEvent event) {
        this.event = event;
    }

    @Info(value = "Returns the level of the event")
    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    @Info(value = "Returns the level and position of the event")
    public BlockContainerJS getBlock() {
        return new BlockContainerJS(event.getLevel(), event.getPos());
    }

    @Info(value = "Returns the targeted face of the event")
    public Direction getTargetedFace() {
        return event.getTargetedFace();
    }

    @Info(value = "Returns the player that started the fire, may be null")
    @Override
    @Nullable
    public Player getEntity() {
        return event.getPlayer();
    }

    @Info(value = "Returns the item used to start the fire")
    public ItemStack getItem() {
        return event.getItemStack();
    }

    @Info(value = "Returns true if fire created is considered 'strong'")
    public boolean isStrong() {
        return event.isStrong();
    }
}