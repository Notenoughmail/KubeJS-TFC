package com.notenoughmail.kubejs_tfc.util.implementation.custom.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.entity.LampBlockEntityJS;
import net.dries007.tfc.common.blockentities.LampBlockEntity;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.fluids.FluidHelpers;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.advancements.TFCAdvancements;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

// Override references to TFCBlockEntities#LAMP
public class LampBlockJS extends LampBlock {

    public LampBlockJS(ExtendedProperties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        level.getBlockEntity(pos, RegistryUtils.getLamp().get()).ifPresent(LampBlockEntity::checkHasRanOut);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.getBlockEntity(pos, RegistryUtils.getLamp().get()).ifPresent(TickCounterBlockEntity::resetCounter);
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public InteractionResult use(BlockState originalState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        // As implemented as of TFC 3.2.0
        final @Nullable LampBlockEntityJS lamp = level.getBlockEntity(pos, RegistryUtils.getLamp().get()).orElse(null);
        if (lamp != null)
        {
            lamp.checkHasRanOut();

            final BlockState state = level.getBlockState(pos);
            final ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty() && player.isShiftKeyDown() && state.getValue(LIT))
            {
                // Quench by shift-clicking with an empty hand
                Helpers.playSound(level, pos, SoundEvents.FIRE_EXTINGUISH);
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                lamp.resetCounter();
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else if (FluidHelpers.transferBetweenBlockEntityAndItem(stack, lamp, player, hand))
            {
                lamp.markForSync();
                if (lamp.getFuel() != null && lamp.getFuel().getBurnRate() == -1 && player instanceof ServerPlayer serverPlayer)
                {
                    TFCAdvancements.LAVA_LAMP.trigger(serverPlayer);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}
