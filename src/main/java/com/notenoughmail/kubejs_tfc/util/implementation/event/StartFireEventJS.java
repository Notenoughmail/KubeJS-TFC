package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.Direction;

public class StartFireEventJS extends PlayerEventJS {

    private final StartFireEvent event;

    public StartFireEventJS(StartFireEvent event) {
        this.event = event;
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public LevelJS getLevel() {
        return levelOf(event.getLevel());
    }

    public BlockContainerJS getBlock() {
        return new BlockContainerJS(event.getLevel(), event.getPos());
    }

    public Direction getTargetedFace() {
        return event.getTargetedFace();
    }

    @Override
    public EntityJS getEntity() {
        try {
            return entityOf(event.getPlayer());
        } catch (Exception ignored) {
            return null;
        }
    }

    public ItemStackJS getItem() {
        return new ItemStackJS(event.getItemStack());
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