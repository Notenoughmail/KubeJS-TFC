package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

public class ProspectedEventJS extends PlayerEventJS {

    private final ProspectedEvent event;

    public ProspectedEventJS(ProspectedEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getPlayer();
    }

    public Block getBlock() {
        return event.getBlock();
    }

    public ProspectResult getProspectResult() {
        return event.getType();
    }
}