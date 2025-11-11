package io.github.notenoughmail.kubejstfc.implementation.custom.block;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public interface ICustomTorchBlock {

    static SimpleParticleType p() { return ParticleTypes.ASH; }

    default void handleFireDouse(DouseFireEvent event) {}
    default void handleFireStart(StartFireEvent event) {
        event.getLevel().getBlockEntity(event.getPos(), TFCBlockEntities.TICK_COUNTER.get()).ifPresent(TickCounterBlockEntity::resetCounter);
        event.setCanceled(true);
    }
    default int getTotalTicks() { return 0; }
}
