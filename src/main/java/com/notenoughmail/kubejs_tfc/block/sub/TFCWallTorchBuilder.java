package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCTorchBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCWallTorchBlock;
import net.dries007.tfc.util.events.DouseFireEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TFCWallTorchBuilder extends ExtendedPropertiesShapedBlockBuilder {

    private final TFCTorchBlockBuilder parent;

    public TFCWallTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
        super(i);
        noItem();
        this.parent = parent;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.TICK_COUNTER, this);
    }

    @Override
    public Block createObject() {
        return new Impl(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks()
                .blockEntity(TFCBlockEntities.TICK_COUNTER);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        return super.textureAll(tex);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {}

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        super.generateBlockModelJsons(generator);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        super.generateBlockStateJson(bs);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        super.generateDataJsons(generator);
    }

    private class Impl extends TFCWallTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
            final Direction dir = pState.getValue(FACING);
            final double
                    x = pPos.getX() + 0.5D + 0.27D * (double) dir.getStepX(),
                    y = pPos.getY() + 0.92D,
                    z = pPos.getZ() + 0.5D + 0.27D * (double) dir.getStepZ();
            parent.smokeParticle.get().ifPresent(p -> pLevel.addParticle(p, x, y, z, 0.0D, 0.0D, 0.0D));
            parent.flameParticle.get().ifPresent(p -> pLevel.addParticle(p, x, y, z, 0.0D, 0.0D, 0.0D));
        }

        @Override
        public void handleFireDouse(DouseFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.deadWall.get().withPropertiesOf(event.getState()));
            event.setCanceled(true);
        }

        @Override
        public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
            return parent.get().use(state, world, pos, player, hand, result);
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
            TFCTorchBlockBuilder.randomTick(level, pos, parent.deadWall.get().withPropertiesOf(state), parent.decayLength);
        }

        @Override
        public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
            parent.get().setPlacedBy(level, pos, state, placer, stack);
        }
    }
}
