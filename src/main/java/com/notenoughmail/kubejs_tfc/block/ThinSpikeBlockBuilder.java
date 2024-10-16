package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ThinSpikeBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.OverworldClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
public class ThinSpikeBlockBuilder extends BlockBuilder {

    private float dripChance;
    private float meltChance;
    private boolean drips;
    private float dripTemp;
    private boolean melts;
    private float meltTemp;
    private ResourceLocation dripParticle;
    private FluidStack meltFluid;
    private String tipModel;

    public ThinSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        drips = false;
        dripChance = 0.15f;
        dripTemp = OverworldClimateModel.ICICLE_DRIP_TEMPERATURE;
        melts = false;
        meltChance = 1f / 60f; // By default, TFC uses Random#nextInt(60) == 0, this is effectively equivalent;
        meltTemp = OverworldClimateModel.ICICLE_MELT_TEMPERATURE;
        dripParticle = new ResourceLocation("minecraft", "dripping_dripstone_water");
        meltFluid = new FluidStack(Fluids.WATER, 100);
        tipModel = "";
    }

    @Info(value = "Makes the block drip particles")
    public ThinSpikeBlockBuilder drips() {
        drips = true;
        return this;
    }

    @Info(value = "Sets the chance, in the range [0, 1], the block will drip per tick")
    public ThinSpikeBlockBuilder dripChance(float chance) {
        dripChance = chance;
        return this;
    }

    @Info(value = "Sets the temperature above which the block must be to begin dripping")
    public ThinSpikeBlockBuilder dripTemp(float temp) {
        dripTemp = temp;
        return this;
    }

    @Info(value = "Allows the block to melt under certain situations")
    public ThinSpikeBlockBuilder melts() {
        melts = true;
        return this;
    }

    @Info(value = "Sets the chance, in the range [0, 1], that the block will drip per random tick")
    public ThinSpikeBlockBuilder meltChance(float chance) {
        meltChance = chance;
        return this;
    }

    @Info(value = "Sets the temperature above which the block can melt")
    public ThinSpikeBlockBuilder meltTemp(float temp) {
        meltTemp = temp;
        return this;
    }

    @Info(value = "The registry name of a particle that will drip from the block")
    public ThinSpikeBlockBuilder dripParticle(ResourceLocation particle) {
        dripParticle = particle;
        return this;
    }

    @Info(value = "The fluid the block melts into")
    public ThinSpikeBlockBuilder meltFluid(FluidStackJS fluid) {
        meltFluid = new FluidStack(fluid.getFluid(), (int) fluid.getAmount());
        return this;
    }

    @Info(value = "Sets the model of the tip state")
    public ThinSpikeBlockBuilder tipModel(String s) {
        tipModel = s;
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return texture("0", tex);
    }

    @Override
    public ThinSpikeBlock createObject() {
        return new ThinSpikeBlock(melts ? createProperties().randomTicks() : createProperties()) {

            @Nullable
            private Optional<ParticleOptions> particle;

            @Override
            public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                if (drips) {
                    final float temperature = Climate.getTemperature(level, pos);
                    if (state.getValue(TIP) && state.getValue(FLUID).getFluid() == Fluids.EMPTY && temperature > dripTemp && random.nextFloat() < dripChance) {
                        if (random.nextFloat() < dripChance) { // Weird but TFC does it
                            if (particle == null) {
                                particle = RegistryUtils.getParticleOrLogError(dripParticle);
                            }
                            particle.ifPresent(options -> spawnParticle(level, pos, state, options));
                        }
                    }
                }
            }

            /**
             * Modified from {@link net.dries007.tfc.common.blocks.IcicleBlock#spawnDripParticle(Level, BlockPos, BlockState)} to accept arbitrary particles
             */
            private static void spawnParticle(Level level, BlockPos pos, BlockState state, ParticleOptions particle) {
                Vec3 offset = state.getOffset(level, pos);
                level.addParticle(particle, pos.getX() + 0.5D + offset.x, ((pos.getY() + 1) - 0.6875F) - 0.0625D, pos.getZ() + 0.5D + offset.z, 0.0D, 0.0D, 0.0D);
            }

            /**
             * Copied wholesale from {@link net.dries007.tfc.common.blocks.IcicleBlock#randomTick(BlockState, ServerLevel, BlockPos, RandomSource)} and modified to accept custom values
             */
            @Override
            public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
            {
                if (melts) {
                    final float temperature = Climate.getTemperature(level, pos);
                    if (state.getValue(TIP) && state.getValue(FLUID).getFluid() == Fluids.EMPTY && temperature > meltTemp && random.nextFloat() < meltChance)
                    {
                        // Melt, shrink the icicle, and possibly fill a fluid handler beneath
                        level.removeBlock(pos, false);

                        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().setWithOffset(pos, 0, 1, 0);

                        final BlockState stateAbove = level.getBlockState(mutable);
                        if (Helpers.isBlock(stateAbove, this))
                        {
                            level.setBlock(mutable, stateAbove.setValue(TIP, true), Block.UPDATE_ALL);
                        }

                        for (int i = 0; i < 5; i++)
                        {
                            mutable.move(0, -1, 0);
                            BlockState stateAt = level.getBlockState(mutable);
                            if (!stateAt.isAir()) // if we hit a non-air block, we won't be returning
                            {
                                BlockEntity blockEntity = level.getBlockEntity(mutable);
                                if (blockEntity != null)
                                {
                                    blockEntity.getCapability(Capabilities.FLUID, Direction.UP).ifPresent(cap -> cap.fill(meltFluid, IFluidHandler.FluidAction.EXECUTE));
                                }
                                return;
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");
        }

        if (itemBuilder.textureJson.size() == 0) {
            itemBuilder.texture(newID("item/", "").toString());
        }
        m.textures(itemBuilder.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        if (model.isEmpty()) {
            generator.blockModel(id, m -> {
                m.parent("tfc:block/thin_spike");
                m.textures(textures);
            });
        } else {
            generator.blockModel(id, m -> m.parent(model));
        }

        if (!tipModel.isEmpty()) {
            generator.blockModel(newID("", "_tip"), m -> m.parent(tipModel));
        } else {
            generator.blockModel(newID("", "_tip"), m -> {
                m.parent("tfc:block/thin_spike_tip");
                m.textures(textures);
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String blockModelLoc = newID("block/", "").toString();
        bs.simpleVariant("tip=true", blockModelLoc + "_tip");
        bs.simpleVariant("tip=false", blockModelLoc);
    }
}
