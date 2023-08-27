package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraft.world.level.block.Block;

public class ProspectedEventJS extends PlayerEventJS {

    private final ProspectedEvent event;

    public ProspectedEventJS(ProspectedEvent event) {
        this.event = event;
    }

    @Override
    public EntityJS getEntity() {
        return entityOf(event.getPlayer());
    }

    public Block getBlock() {
        return event.getBlock();
    }

    public ProspectResult getProspectResult() {
        return event.getType();
    }
}