package io.github.notenoughmail.kubejstfc.implementation.custom.block;

import io.github.notenoughmail.kubejstfc.builders.fluid.SpringWaterBuilder;
import net.dries007.tfc.common.blocks.HotWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class KubeHotWaterBlock extends HotWaterBlock {

    private final SpringWaterBuilder builder;

    public KubeHotWaterBlock(SpringWaterBuilder builder, Properties properties) {
        super(builder, properties);
        this.builder = builder;
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource random) {
        final double x = pos.getX() + random.nextFloat();
        final double y = pos.getY();
        final double z = pos.getZ() + random.nextFloat();

        if (builder.bubbleParticle != null && random.nextInt(3) == 0) {
            level.addParticle(builder.bubbleParticle, x, y + random.nextFloat(), z, 0.0D, 0.04D, 0.0D);
        }
        if (builder.steamParticle != null && level.isEmptyBlock(pos.above())) {
            level.addParticle(builder.steamParticle, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.random.nextInt(10) == 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth()) {
            living.heal(builder.healingAmount);
        }
    }
}
