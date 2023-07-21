package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.server.ServerEventJS;
import net.dries007.tfc.util.events.ProspectedEvent;

public class ProspectedEventJS extends PlayerEventJS {

    private final ProspectedEvent event;

    public ProspectedEventJS(ProspectedEvent event) {
        this.event = event;
    }

    @Override
    public EntityJS getEntity() {
        return new EntityJS(event.getPlayer());
    }

    @Override
    public LevelJS getLevel() {
        return super.getLevel();
    }

    @Override
    public boolean canCancel() {
        return super.canCancel();
    }
}
