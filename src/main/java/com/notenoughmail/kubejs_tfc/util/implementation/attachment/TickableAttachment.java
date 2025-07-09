package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJSTicker;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.JavaAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TickableAttachment extends BlockEntityAttachment {

    static boolean getBool(String name, Map<String, Object> map, boolean defaultVal)  {
        return map.containsKey(name) ? (Boolean) JavaAdapter.convertResult(ScriptType.STARTUP.manager.get().context, map.get(name), Boolean.class) : defaultVal;
    }

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
