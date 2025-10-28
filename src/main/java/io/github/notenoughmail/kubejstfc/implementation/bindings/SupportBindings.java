package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.util.data.Support;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public enum SupportBindings {
    INSTANCE;

    @Info("If the given position can start a collapse")
    public boolean canStartCollapse(LevelAccessor level, BlockPos pos) {
        return CollapseRecipe.canStartCollapse(level, pos);
    }

    @Info("Attempts to trigger a collapse and returns if one started")
    public boolean tryStartCollapse(Level level, BlockPos pos) {
        return CollapseRecipe.tryTriggerCollapse(level, pos);
    }

    @Info("Forces a collapse to start and returns if any blocks collapsed")
    public boolean forceCollapse(Level level, BlockPos pos) {
        return CollapseRecipe.startCollapse(level, pos);
    }

    @Info("Gets all positions in the given range that are unsupported")
    public Set<BlockPos> fundUnsupportedPositions(BlockGetter level, BlockPos from, BlockPos to) {
        return Support.findUnsupportedPositions(level, from, to);
    }

    @Info("If the given position is supported")
    public boolean isSupported(BlockGetter level, BlockPos pos) {
        return Support.isSupported(level, pos);
    }

    @Info("Gets an iterable view of all posiitons that could possibly be supported around the box defined by the min and max points")
    public Iterable<BlockPos> getMaximumSupportedAreaAround(BlockPos minPoint, BlockPos maxPoint) {
        return Support.getMaximumSupportedAreaAround(minPoint, maxPoint);
    }

    @Info("Gets the maximum support range used for checking if a position could be supported")
    public Support.SupportRange getCheckRange() {
        return Support.getSupportCheckRange();
    }

    @Info("Gets the support of the given block")
    @Nullable
    public Support get(BlockState state) {
        return Support.get(state);
    }

    @Info("Gets the suport at the given position")
    @Nullable
    public Support get(BlockGetter level, BlockPos pos) {
        return get(level.getBlockState(pos));
    }
}
