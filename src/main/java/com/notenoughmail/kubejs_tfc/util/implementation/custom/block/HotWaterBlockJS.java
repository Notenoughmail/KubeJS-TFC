package com.notenoughmail.kubejs_tfc.util.implementation.custom.block;

import com.notenoughmail.kubejs_tfc.fluid.HotWaterFluidBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import net.dries007.tfc.common.blocks.HotWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HotWaterBlockJS extends HotWaterBlock {

    private final HotWaterFluidBuilder builder;
    @Nullable
    private Optional<ParticleOptions> bubbleParticle;
    @Nullable
    private Optional<ParticleOptions> steamParticle;

    public HotWaterBlockJS(HotWaterFluidBuilder builder, Properties properties) {
        super(builder.flowingFluid, properties);
        this.builder = builder;
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource random) {
        final double x = pos.getX() + 0.5D;
        final double y = pos.getY() + 1.0D;
        final double z = pos.getZ() + 0.5D;

        if (builder.hasBubbles && random.nextInt(4) == 0) {
            if (bubbleParticle == null) {
                bubbleParticle = RegistryUtils.getOrLogErrorParticle(builder.bubbleParticle);
            }
            bubbleParticle.ifPresent(particle -> level.addParticle(particle, x + random.nextFloat() - random.nextFloat(), y, z + random.nextFloat() - random.nextFloat(), 0.0D, 0.0D, 0.0D));
        }
        if (builder.hasSteam && level.isEmptyBlock(pos.above())) {
            if (steamParticle == null) {
                steamParticle = RegistryUtils.getOrLogErrorParticle(builder.steamParticle);
            }
            steamParticle.ifPresent(particle -> level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D));
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.random.nextInt(10) == 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth()) {
            living.heal(builder.healingAmount);
        }
    }
}
