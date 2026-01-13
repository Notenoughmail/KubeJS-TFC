package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

// I really wanted this to not be a raw port of the old event, but alas MC (& to an extent Neo) is simply not built so kindly
@Info("""
        This event is fired whenever a player closes or opens a menu that is not the player's own inventory
        
        A full list of valid menus can be obtained by running the command `/kubejs dump_registry minecraft:menu`
        
        The implementation of this event is based on Oversized Item in Storage area, a 1.12 addon. It is licensed under the BSD License
        """)
public record KubeLimitContainerEvent(
        @HideFromJS List<Slot> slotsToLimit,
        @HideFromJS Level level,
        @HideFromJS BlockPos spawnPos
) implements KubeEvent {

    @Info("Limits the entire container to the given size, disallowing items with a size greater than the given size")
    public void limit(Size size) {
        limit(size, true);
    }

    @Info(value = "Limits the entire container to the given size", params = {
            @Param(name = "size", value = "The size which the container is limited to"),
            @Param(name = "allowsEqual", value = "If items with a size equal to the provided `size` should be allowed")
    })
    public void limit(Size size, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.hasItem()) {
                trySpitItemMax(slot, size, allowsEqual);
            }
        }
    }

    @Info(value = "Limits the given slot range to the provided size, disallowing items with a size greater than the given size", params = {
            @Param(name = "size", value = "The size the slots are limited to"),
            @Param(name = "min", value = "The index of the start of the slots to be limited"),
            @Param(name = "max", value = "The index of the end of the lots to be limited")
    })
    public void limit(Size size, int min, int max) {
        limit(size, min, max, true);
    }

    @Info(value = "Limits the given slot range to the provided size", params = {
            @Param(name = "size", value = "The size the slots are limited to"),
            @Param(name = "min", value = "The index of the start of the slots to be limited"),
            @Param(name = "max", value = "The index of the end of the lots to be limited"),
            @Param(name = "allowsEqual", value = "If items with a size equal to the provided `size` should be allowed")
    })
    public void limit(Size size, int min, int max, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.index >= min && slot.index <= max && slot.hasItem()) {
                trySpitItemMax(slot, size, allowsEqual);
            }
        }
    }

    @Info("Limits items in the container to a size equal to or greater than the provided size")
    public void lowerLimit(Size size) {
        lowerLimit(size, true);
    }

    @Info(value = "Limits items in the container to a size", params = {
            @Param(name = "size", value = "The minimum size items in the container may be"),
            @Param(name = "allowsEqual", value = "If items with a size equal to the provided `size` should be allowed")
    })
    public void lowerLimit(Size size, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.hasItem()) {
                trySpitItemMin(slot, size, allowsEqual);
            }
        }
    }

    @Info(value = "Limits items in the provided slot range to a size equal to or greater than the provided size", params = {
            @Param(name = "size", value = "The minimum size items in the container may be"),
            @Param(name = "min", value = "The index of the start of the slots to be limited"),
            @Param(name = "max", value = "The index of the end of the lots to be limited")
    })
    public void lowerLimit(Size size, int min, int max) {
        lowerLimit(size, min, max, true);
    }

    @Info(value = "Limits items in the provided slot range to a size greater than the provided size", params = {
            @Param(name = "size", value = "The minimum size items in the container may be"),
            @Param(name = "min", value = "The index of the start of the slots to be limited"),
            @Param(name = "max", value = "The index of the end of the lots to be limited"),
            @Param(name = "allowsEqual", value = "If items with a size equal to the provided `size` should be allowed")
    })
    public void lowerLimit(Size size, int min, int max, boolean allowsEqual) {
        for (Slot slot : slotsToLimit) {
            if (slot.index >= min && slot.index <= max && slot.hasItem()) {
                trySpitItemMin(slot, size, allowsEqual);
            }
        }
    }

    private void trySpitItemMax(Slot slot, Size size, boolean allowsEqual) {
        final ItemStack stack = slot.getItem();
        final Size stackSize = ItemSizeManager.get(stack).getSize(stack);
        if (allowsEqual ? size.isSmallerThan(stackSize) : size.isEqualOrSmallerThan(stackSize)) {
            spitOutItem(slot, stack);
        }
    }

    private void trySpitItemMin(Slot slot, Size size, boolean allowsEqual) {
        final ItemStack stack = slot.getItem();
        final Size stackSize = ItemSizeManager.get(stack).getSize(stack);
        if (allowsEqual ? stackSize.isSmallerThan(size) : stackSize.isEqualOrSmallerThan(size)) {
            spitOutItem(slot, stack);
        }
    }

    private void spitOutItem(Slot slot, ItemStack stack) {
        final ItemEntity itemEntity = new ItemEntity(
                level,
                spawnPos.getX(),
                spawnPos.getY() + 0.3F,
                spawnPos.getZ(),
                stack
        );
        itemEntity.setPickUpDelay(30);
        level.addFreshEntity(itemEntity);
        slot.set(ItemStack.EMPTY);
        slot.setChanged();
    }
}
