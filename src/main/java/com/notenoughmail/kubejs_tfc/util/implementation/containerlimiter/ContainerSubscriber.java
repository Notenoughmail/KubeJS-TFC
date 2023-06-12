package com.notenoughmail.kubejs_tfc.util.implementation.containerlimiter;

import com.mojang.datafixers.util.Pair;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = KubeJSTFC.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ContainerSubscriber {

    /**
     * The majority of this event's handling is based off of <i><a href="https://github.com/DoubleDoorDevelopment/OversizedItemInStorageArea">Oversized Item in Storage Area</a></i><br>
     * <i>Oversized Item in Storage Area</i> is licenced under the <a href="https://www.curseforge.com/minecraft/mc-mods/oversized-item-in-storage-area/comments#license">BSD Licence</a>
     */
    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        AbstractContainerMenu container = event.getContainer();
        MenuType<?> menuType;
        try {
            menuType = container.getType();
        } catch (UnsupportedOperationException ignored) {
            // Instead of returning null mojang throws an exception!
            return; // Do nothing as a menu is needed
        }

        if (event.getPlayer() instanceof ServerPlayer player && SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.containsKey(menuType.getRegistryName())) {
            Pair<Size, List<Pair<Integer, Integer>>> function = SemiFunctionalContainerLimiterEventJS.LIMITED_SIZES.get(menuType.getRegistryName());

            // Filter slots to only the ones that have items and (if present) within the ranges given
            NonNullList<Slot> containerSlots = container.slots; // This also gives the player's inventory slots because why not
            List<Slot> sanitizedSlots = new ArrayList<>();
            for (Slot slot : containerSlots) {
                // Do NOT touch the player inventory
                if (!(slot.container instanceof Inventory)) {
                    sanitizedSlots.add(slot);
                }
            }
            List<Slot> filteredSlots = new ArrayList<>();
            if (function.getSecond().isEmpty()) {
                sanitizedSlots.forEach(slot -> {
                    if (slot.hasItem()) {
                        filteredSlots.add(slot);
                    }
                });
            } else {
                for (Pair<Integer, Integer> range : function.getSecond()) {
                    sanitizedSlots.subList(Math.min(0, range.getFirst()), Math.min(sanitizedSlots.size() - 1, range.getSecond())).forEach(slot -> {
                        if (slot.hasItem()) {
                            filteredSlots.add(slot);
                        }
                    });
                }
            }

            Level level = player.level;
            BlockPos pos = player.getOnPos().above();
            for (Slot slot : filteredSlots) {
                ItemStack slotItem = slot.getItem();
                // If the item is bigger than the provided size, spit it out around the player
                if (!ItemSizeManager.get(slotItem).getSize(slotItem).isEqualOrSmallerThan(function.getFirst()) && !level.isClientSide()) {
                    float randX = level.random.nextFloat() * 0.8F;
                    float randY = level.random.nextFloat() * 0.8F + 0.3F;
                    float randZ = level.random.nextFloat() * 0.8F;
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX() + randX, pos.getY() + randY, pos.getZ() + randZ, slotItem);
                    itemEntity.setPickUpDelay(30);
                    level.addFreshEntity(itemEntity);
                    slot.set(ItemStack.EMPTY);
                    slot.setChanged();
                }
            }
        }
    }
}