package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.IFluidBuilderMixin;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.blocks.HotWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class HotWaterFluidBlockBuilder extends FluidBlockBuilder {

    private final FluidBuilder builder;

    public HotWaterFluidBlockBuilder(FluidBuilder b) {
        super(b);
        builder = b;
    }

    @Override
    public Block createObject() {
        return new HotWaterBlock(UtilsJS.cast(builder.flowingFluid), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()) {

            // Copied and modified from actual class
            @Override
            public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random random)
            {
                var bubble = ((IFluidBuilderMixin) builder).getBubbleParticle();
                var steam = ((IFluidBuilderMixin) builder).getSteamParticle();
                var hasBubble = ((IFluidBuilderMixin) builder).hasBubbleParticle();
                var hasSteam = ((IFluidBuilderMixin) builder).hasSteamParticle();

                double x = pos.getX() + 0.5D;
                double y = pos.getY() + 1.0D;
                double z = pos.getZ() + 0.5D;

                if (hasBubble && random.nextInt(4) == 0)
                    level.addParticle(RegistrationUtils.getOrLogErrorParticle(bubble, ParticleTypes.BUBBLE), x + random.nextFloat() - random.nextFloat(), y, z + random.nextFloat() - random.nextFloat(), 0.0D, 0.0D, 0.0D);
                if (hasSteam && level.isEmptyBlock(pos.above()))
                    level.addParticle(RegistrationUtils.getOrLogErrorParticle(steam, TFCParticles.STEAM.get()), x, y, z, 0.0D, 0.0D, 0.0D);
            }

            @Override
            public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
            {
                if (level.random.nextInt(10) == 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth())
                {
                    living.heal(((IFluidBuilderMixin) builder).getHealAmount());
                }
            }
        };
    }
}
