package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LoggingEventJS extends EventJS {

    private final Level level;
    private final BlockContainerJS block;
    private final ItemStack axe;

    public LoggingEventJS(Level level, BlockPos pos, ItemStack axe) {
        this.level = level;
        this.block = new BlockContainerJS(this.level, pos);
        this.axe = axe;
    }

    public Level getLevel() {
        return level;
    }

    public ItemStack getAxe() {
        return axe;
    }

    public BlockContainerJS getBlock() {
        return block;
    }
}