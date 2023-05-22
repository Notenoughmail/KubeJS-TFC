package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.IFluidBuilderMixin;
import dev.latvian.mods.kubejs.fluid.FluidBlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.blocks.HotWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;

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

                double x = pos.getX() + 0.5D;
                double y = pos.getY() + 1.0D;
                double z = pos.getZ() + 0.5D;

                if (random.nextInt(4) == 0)
                    level.addParticle(
                            ForgeRegistries.PARTICLE_TYPES.getValue(bubble) instanceof ParticleOptions bubbleOptions ? bubbleOptions : defaultBubbleAndWarn(bubble),
                            x + random.nextFloat() - random.nextFloat(), y, z + random.nextFloat() - random.nextFloat(), 0.0D, 0.0D, 0.0D);
                if (level.isEmptyBlock(pos.above()))
                    level.addParticle(
                            ForgeRegistries.PARTICLE_TYPES.getValue(steam) instanceof ParticleOptions steamOptions ? steamOptions : defaultSteamAndWarn(steam),
                            x, y, z, 0.0D, 0.0D, 0.0D);
            }

            @Override
            public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
            {
                if (level.random.nextInt(10) == 0 && entity instanceof LivingEntity living && living.getHealth() < living.getMaxHealth())
                {
                    living.heal(((IFluidBuilderMixin) builder).getHealAmount());
                }
            }

            private ParticleOptions defaultBubbleAndWarn(ResourceLocation particle) {
                KubeJSTFC.LOGGER.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions.", particle);
                return ParticleTypes.BUBBLE;
            }

            private ParticleOptions defaultSteamAndWarn(ResourceLocation particle) {
                KubeJSTFC.LOGGER.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions.", particle);
                return TFCParticles.STEAM.get();
            }
        };
    }
}
