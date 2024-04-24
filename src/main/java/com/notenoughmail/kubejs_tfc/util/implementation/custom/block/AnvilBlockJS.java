package com.notenoughmail.kubejs_tfc.util.implementation.custom.block;

import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;

public class AnvilBlockJS extends AnvilBlock {

    // Gotta replace the reference to TFC's anvil with our own!
    public static InteractionResult interactWithAnvilJS(Level level, BlockPos pos, Player player, InteractionHand hand)
    {
        final AnvilBlockEntity anvil = level.getBlockEntity(pos, RegistryUtils.getAnvil().get()).orElse(null);
        if (anvil == null)
        {
            return InteractionResult.PASS;
        }
        final IItemHandler inventory = anvil.getCapability(Capabilities.ITEM).resolve().orElse(null);
        if (inventory == null)
        {
            return InteractionResult.PASS;
        }
        if (player.isShiftKeyDown())
        {
            final ItemStack playerStack = player.getItemInHand(hand);
            if (playerStack.isEmpty()) // Extraction requires held item to be empty
            {
                for (int slot : AnvilBlockEntity.SLOTS_BY_HAND_EXTRACT)
                {
                    final ItemStack anvilStack = inventory.getStackInSlot(slot);
                    if (!anvilStack.isEmpty())
                    {
                        // Give the item to player in the main hand
                        ItemStack result = inventory.extractItem(slot, 1, false);
                        player.setItemInHand(hand, result);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            else if (Helpers.isItem(playerStack, TFCTags.Items.HAMMERS)) // Attempt welding with a hammer in hand
            {
                final InteractionResult weldResult = anvil.weld(player);
                if (weldResult == InteractionResult.SUCCESS)
                {
                    // Welding occurred
                    if (level instanceof ServerLevel server)
                    {
                        final double x = pos.getX() + Mth.nextDouble(level.random, 0.2, 0.8);
                        final double z = pos.getZ() + Mth.nextDouble(level.random, 0.2, 0.8);
                        final double y = pos.getY() + Mth.nextDouble(level.random, 0.8, 1.0);
                        server.sendParticles(TFCParticles.SPARK.get(), x, y, z, 8, 0, 0, 0, 0.2f);
                    }
                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.6f, 1.0f);
                    return InteractionResult.SUCCESS;
                }
                else if (weldResult == InteractionResult.FAIL)
                {
                    // Welding was attempted, but failed for some reason - player was alerted and action was consumed.
                    // Returning fail here causes the off hand to still attempt to be used?
                    return InteractionResult.SUCCESS;
                }
            }
            else
            {
                // Try and insert an item
                final ItemStack insertStack = playerStack.copy();
                for (int slot : AnvilBlockEntity.SLOTS_BY_HAND_INSERT)
                {
                    final ItemStack resultStack = inventory.insertItem(slot, insertStack, false);
                    if (insertStack.getCount() > resultStack.getCount())
                    {
                        // At least one item was inserted (and so remainder < attempt)
                        player.setItemInHand(hand, resultStack);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        else
        {
            // Not shifting, so attempt to open the anvil gui
            if (player instanceof ServerPlayer serverPlayer)
            {
                Helpers.openScreen(serverPlayer, anvil.anvilProvider(), pos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private final Component defaultName;

    public AnvilBlockJS(ExtendedProperties properties, int tier, Component defaultName) {
        super(properties, tier);
        this.defaultName = defaultName;
    }

    public Component getDefaultName() {
        return defaultName;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return interactWithAnvilJS(level, pos, player, hand);
    }
}
