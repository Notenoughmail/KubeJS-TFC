package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.items.ProspectResult;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

@Info("""
        Fired when a prospecting event is fired and is purely informational
        """)
@SuppressWarnings("unused")
public class KubeProspectEvent implements KubePlayerEvent {

    private final ProspectedEvent event;

    public KubeProspectEvent(ProspectedEvent event) {
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