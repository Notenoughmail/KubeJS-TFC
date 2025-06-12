package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJSTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

// TODO: 1.3.0 | Investigate reviving May '24 aspirations: https://github.com/Notenoughmail/KubeJS-TFC/commit/7e3c826ba11ff2b11302e36db8d07921285e753c#diff-feb7a7ba6d79523a00c232e81174e611bbb33c9ba750f5a4f36164760814a013
public interface TickableAttachment extends BlockEntityAttachment {

    default void wrapScriptTicker(BlockEntityJS entity, boolean server) {
        final BlockEntityInfo info = entity.info;
        @Nullable
        final BlockEntityJSTicker user = server ? info.serverTicker : entity.info.clientTicker;
        final BlockEntityJSTicker ticker = new BlockEntityJSTicker(info, 0, 0, be -> {
            final Level level = be.getLevel();
            final BlockPos pos = be.getBlockPos();
            final BlockState state = be.getBlockState();

            tick(level, pos, state, be);
            if (user != null) {
                user.tick(level, pos, state, be);
            }

            // Adjust tick & cycle to only account for script ticking
            be.tick--;
            be.cycle--;
        }, server);
        if (server) {
            info.serverTicker = ticker;
        } else {
            info.clientTicker = ticker;
        }
    }

    void tick(Level level, BlockPos pos, BlockState state, BlockEntityJS be);
}
