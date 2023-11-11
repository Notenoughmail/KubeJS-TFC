package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class StartFireEventJS extends PlayerEventJS {

    private final StartFireEvent event;

    public StartFireEventJS(StartFireEvent event) {
        this.event = event;
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(event.getLevel(), event.getPos());
    }

    public Direction getTargetedFace() {
        return event.getTargetedFace();
    }

    @Override
    @Nullable
    public Player getEntity() {
        return event.getPlayer();
    }

    public ItemStack getItem() {
        return event.getItemStack();
    }

    public StartFireEvent.FireResult getFireResult() {
        return event.getFireResult();
    }

    public void setFireResult(StartFireEvent.FireResult result) {
        event.setFireResult(result);
    }

    public boolean isStrong() {
        return event.isStrong();
    }
}