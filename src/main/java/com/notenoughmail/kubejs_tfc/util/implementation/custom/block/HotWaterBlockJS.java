package com.notenoughmail.kubejs_tfc.util.implementation.custom.block;

import com.notenoughmail.kubejs_tfc.fluid.HotWaterFluidBuilder;
import net.dries007.tfc.common.blocks.HotWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HotWaterBlockJS extends HotWaterBlock {

    private final HotWaterFluidBuilder builder;

    public HotWaterBlockJS(HotWaterFluidBuilder builder, Properties properties) {
        super(builder.flowingFluid, properties);
        this.builder = builder;
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource random) {
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 1.0D;
        final double z = pos.getZ() + 0.5D;

        if (builder.bubbleParticle.get().isPresent() && random.nextInt(4) == 0) {
            level.addParticle(builder.bubbleParticle.get().get(), x + random.nextFloat() - random.nextFloat(), y, z + random.nextFloat() - random.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
        if (builder.steamParticle.get().isPresent() && level.isEmptyBlock(pos.above())) {
            level.addParticle(builder.steamParticle.get().get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.random.nextInt(10) == 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth()) {
            living.heal(builder.healingAmount);
        }
    }
}
