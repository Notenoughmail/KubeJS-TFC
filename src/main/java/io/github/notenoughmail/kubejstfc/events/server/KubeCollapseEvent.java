package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.events.CollapseEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

@Info("Fires whenever a collapse happens for both real and fake collapses")
@SuppressWarnings("unused")
public class KubeCollapseEvent implements KubeLevelEvent {

    private final CollapseEvent event;

    public KubeCollapseEvent(CollapseEvent event) {
        this.event = event;
    }

    public BlockPos getCenterPos() {
        return event.getCenterPos();
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    @Info("Returns the maximum distance from the center block of collapsing blocks, or 0 if the collapse is fake")
    public double getRadiusSquared() {
        return event.getRadiusSquared();
    }

    @Info("Returns a list of `BlockPos`es where a block collapses or, if the collapse is fake, particles spawn")
    public List<BlockPos> getSecondaryPositions() {
        return event.getNextPositions();
    }

    public boolean isFake() {
        return event.isFake();
    }
}
