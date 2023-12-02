package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.ThinSpikeBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.OverworldClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

// TODO: JSDoc
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
    private final List<AABB> tipShape;
    private VoxelShape cachedTipShape;
    private VoxelShape cachedBaseShape;

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
        tipShape = new ArrayList<>();
    }

    public ThinSpikeBlockBuilder drips() {
        drips = true;
        return this;
    }

    public ThinSpikeBlockBuilder dripChance(float chance) {
        dripChance = chance;
        return this;
    }

    public ThinSpikeBlockBuilder dripTemp(float temp) {
        dripTemp = temp;
        return this;
    }

    public ThinSpikeBlockBuilder melts() {
        melts = true;
        return this;
    }

    public ThinSpikeBlockBuilder meltChance(float chance) {
        meltChance = chance;
        return this;
    }

    public ThinSpikeBlockBuilder meltTemp(float temp) {
        meltTemp = temp;
        return this;
    }

    public ThinSpikeBlockBuilder dripParticle(ResourceLocation particle) {
        dripParticle = particle;
        return this;
    }

    public ThinSpikeBlockBuilder meltFluid(Object o) {
        var archiFluidStack = FluidStackJS.of(o).getFluidStack();
        meltFluid = new FluidStack(archiFluidStack.getFluid(), (int) archiFluidStack.getAmount());
        return this;
    }

    public ThinSpikeBlockBuilder tipModel(String s) {
        tipModel = s;
        return this;
    }

    public ThinSpikeBlockBuilder tipBox(double x0, double y0, double z0, double x1, double y1, double z1, boolean scale16) {
        if (scale16) {
            tipShape.add(new AABB(x0 / 16D, y0 / 16D, z0 / 16D, x1 / 16D, y1 / 16D, z1 / 16D));
        } else {
            tipShape.add(new AABB(x0, y0, z0, x1, y1, z1));
        }

        return this;
    }

    public ThinSpikeBlockBuilder tipBox(double x0, double y0, double z0, double x1, double y1, double z1) {
        return tipBox(x0, y0, z0, x1, y1, z1, true);
    }

    private VoxelShape getTipShape() {
        if (tipShape.isEmpty()) {
            return ThinSpikeBlock.TIP_SHAPE;
        }
        if (cachedTipShape == null) {
            cachedTipShape = BlockBuilder.createShape(tipShape);
        }
        return cachedTipShape;
    }

    private VoxelShape getBaseShape() {
        if (customShape.isEmpty()) {
            return ThinSpikeBlock.PILLAR_SHAPE;
        }
        if (cachedBaseShape == null) {
            cachedBaseShape = BlockBuilder.createShape(customShape);
        }
        return cachedBaseShape;
    }

    @Override
    public ThinSpikeBlock createObject() {
        return new ThinSpikeBlock(melts ? createProperties().randomTicks() : createProperties()) {

            @Override
            public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                if (drips) {
                    final float temperature = Climate.getTemperature(level, pos);
                    if (state.getValue(TIP) && state.getValue(FLUID).getFluid() == Fluids.EMPTY && temperature > dripTemp && random.nextFloat() < dripChance) {
                        if (random.nextFloat() < dripChance) { // Weird but TFC does it
                            spawnParticle(level, pos, state, RegistryUtils.getOrLogErrorParticle(dripParticle, ParticleTypes.DRIPPING_DRIPSTONE_WATER));
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


            @Override
            public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
            {
                return state.getValue(TIP) ? getTipShape() : getBaseShape();
            }
        };
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson != null) {
            generator.json(newID("blockstates/", ""), blockstateJson);
        } else {
            var blockModelLoc = newID("block/", "").toString();
            generator.blockState(id, m -> {
                m.variant("tip=true", v -> v.model(blockModelLoc + "_tip"));
                m.variant("tip=false", v -> v.model(blockModelLoc));
            });
        }

        var texture = id.getNamespace() + ":block/" + id.getPath();
        if (modelJson != null) {
            generator.json(newID("models/block", ""), modelJson);
        } else {
            generator.blockModel(id, m -> {
                m.parent("tfc:block/thin_spike");
                m.texture("0", texture);
                m.texture("particle", texture);
            });
        }
        if (!tipModel.isEmpty()) {
            generator.blockModel(newID("", "_tip"), m -> m.parent(tipModel));
        } else {
            generator.blockModel(newID("", "_tip"), m -> {
                m.parent("tfc:block/thin_spike_tip");
                m.texture("0", texture);
                m.texture("particle", texture);
            });
        }

        if (itemBuilder != null) {
            generator.itemModel(itemBuilder.id, m -> {
                if (!model.isEmpty()) {
                    m.parent(model);
                } else {
                    m.parent("item/generated");
                }

                if (itemBuilder.textureJson.size() == 0) {
                    itemBuilder.texture(newID("item/", "").toString());
                }
                m.textures(itemBuilder.textureJson);
            });
        }
    }
}
