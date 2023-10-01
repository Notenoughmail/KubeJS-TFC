package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import net.dries007.tfc.util.events.LoggingEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class LoggingEventJS extends EventJS {

    private final LevelJS level;
    private final BlockContainerJS block;
    private final ItemStackJS axe;

    public LoggingEventJS(Level level, BlockPos pos, ItemStack axe) {
        this.level = KubeJS.PROXY.getLevel(level);
        this.block = this.level.getBlock(pos);
        this.axe = new ItemStackJS(axe);
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    public LevelJS getLevel() {
        return level;
    }

    public ItemStackJS getAxe() {
        return axe;
    }

    public BlockContainerJS getBlock() {
        return block;
    }
}