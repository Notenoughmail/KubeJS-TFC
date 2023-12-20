package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

// TODO: JSDoc
@Info(value = """
        This event is fired whenever a player closes a menu that is not the player's own inventory
        
        A full list of valid menus can be obtained by running the command '/kubejs dump_registry minecraft:menu'
        
        The implementation of this event is based on Oversized Item in Storage area, a 1.12 addon. It is licensed under the BSD License
        """)
public class ContainerLimiterEventJS extends EventJS {

    private final List<Slot> slotsToLimit;
    private final Level level;
    private final BlockPos spawnPos;

    public ContainerLimiterEventJS(List<Slot> slotsToLimit, Level level, BlockPos spawnPos) {
        this.slotsToLimit = slotsToLimit;
        this.level = level;
        this.spawnPos = spawnPos;
    }

    public void limit(Size size) {
        limit(size, true);
    }

    public void limit(Size size, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.hasItem()) {
                trySpitItemMax(slot, size, allowsEqual);
            }
        }
    }

    public void limit(Size size, int min, int max) {
        limit(size, min, max, true);
    }

    public void limit(Size size, int min, int max, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.index >= min && slot.index <= max && slot.hasItem()) {
                trySpitItemMax(slot, size, allowsEqual);
            }
        }
    }

    public void lowerLimit(Size size) {
        lowerLimit(size, true);
    }

    public void lowerLimit(Size size, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.hasItem()) {
                trySpitItemMin(slot, size, allowsEqual);
            }
        }
    }

    public void lowerLimit(Size size, int min, int max) {
        lowerLimit(size, min, max, true);
    }

    public void lowerLimit(Size size, int min, int max, boolean allowEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.index >= min && slot.index <= max && slot.hasItem()) {
                trySpitItemMin(slot, size, allowEqual);
            }
        }
    }

    private void trySpitItemMax(Slot slot, Size size, boolean allowEqual) {
        final ItemStack stack = slot.getItem();
        final Size stackSize = ItemSizeManager.get(stack).getSize(stack);
        if (allowEqual ? size.isSmallerThan(stackSize) : size.isEqualOrSmallerThan(stackSize)) {
            spitOutItem(slot, stack);
        }
    }

    private void trySpitItemMin(Slot slot, Size size, boolean allowEqual) {
        final ItemStack stack = slot.getItem();
        final Size stackSize = ItemSizeManager.get(stack).getSize(stack);
        if (allowEqual ? stackSize.isSmallerThan(size) : stackSize.isEqualOrSmallerThan(size)) {
            spitOutItem(slot, stack);
        }
    }

    private void spitOutItem(Slot slot, ItemStack stack) {
        float randX = level.random.nextFloat() * 0.8F;
        float randY = level.random.nextFloat() * 0.8F + 0.3F;
        float randZ = level.random.nextFloat() * 0.8F;
        final ItemEntity itemEntity = new ItemEntity(
                level,
                spawnPos.getX() + randX,
                spawnPos.getY() + randY,
                spawnPos.getZ() + randZ,
                stack
        );
        itemEntity.setPickUpDelay(30);
        level.addFreshEntity(itemEntity);
        slot.set(ItemStack.EMPTY);
        slot.setChanged();
    }
}
