package io.github.notenoughmail.kubejstfc.implementation.attachments;

import dev.latvian.mods.kubejs.block.entity.*;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SealableInventoryAttachment extends TFCInventoryAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(KubeJSTFC.id("sealable_inventory"), Factory.class);

    public record Factory(int width, int height, Optional<ItemPredicate> inputFilter, Optional<Predicate<Size>> sizeFilter, Optional<Predicate<Weight>> weightFilter, boolean canSeal, boolean requiresSeal, Holder<FoodTrait> trait) implements BlockEntityAttachmentFactory {
        @Override
        public SealableInventoryAttachment create(BlockEntityAttachmentInfo info, KubeBlockEntity entity) {
            return new SealableInventoryAttachment(entity, width, height, inputFilter.orElse(null), sizeFilter.orElse(null), weightFilter.orElse(null), canSeal, requiresSeal, trait);
        }

        @Override
        public List<BlockCapability<?, ?>> getCapabilities() {
            return List.of(Capabilities.ItemHandler.BLOCK);
        }
    }

    private boolean sealed;
    private final boolean canSeal;
    private final boolean requiresSeal;
    private final Holder<FoodTrait> trait;

    public SealableInventoryAttachment(KubeBlockEntity blockEntity, int width, int height, @Nullable ItemPredicate inputFilter, @Nullable Predicate<Size> sizeFilter, @Nullable Predicate<Weight> weightFilter, boolean canSeal, boolean requiresSeal, Holder<FoodTrait> trait) {
        super(blockEntity, width, height, inputFilter, sizeFilter, weightFilter);
        this.canSeal = canSeal;
        this.requiresSeal = requiresSeal;
        this.trait = trait;
    }

    @Override
    protected SealableInventory createInventory() {
        return new SealableInventory(this);
    }

    private void unPreserverAll() {
        for (int i = 0 ; i < inventory.getSlots() ; i++) {
            inventory.setStackInSlot(i, remove(inventory.getStackInSlot(i)));
        }
    }

    private void preserveAll() {
        for (int i = 0 ; i < inventory.getSlots() ; i++) {
            inventory.setStackInSlot(i, apply(inventory.getStackInSlot(i)));
        }
    }

    private ItemStack apply(ItemStack stack) {
        return FoodCapability.applyTrait(stack, trait);
    }

    private ItemStack remove(ItemStack stack) {
        return FoodCapability.removeTrait(stack, trait);
    }

    @Override
    public ListTag serialize(HolderLookup.Provider registries) {
        final ListTag tag = super.serialize(registries);
        assert tag != null;
        final CompoundTag sealTag = new CompoundTag();
        sealTag.putBoolean("sealed", sealed);
        tag.add(sealTag);
        return tag;
    }

    @Override
    public void deserialize(HolderLookup.Provider registries, Tag tag) {
        if (tag instanceof ListTag list) {
            final CompoundTag sealTag = Cast.to(list.removeLast());
            sealed = sealTag.getBoolean("sealed");
            super.deserialize(registries, list);
            if (sealed || !requiresSeal) {
                preserveAll();
            }
        }
    }

    @Override
    public void onRemove(ServerLevel level, KubeBlockEntity blockEntity, BlockState newState) {
        unPreserverAll();
        super.onRemove(level, blockEntity, newState);
    }

    public class SealableInventory extends Wrapped {

        public SealableInventory(InventoryAttachment attachment) {
            super(attachment);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            final IItemSize size = ItemSizeManager.get(stack);
            return super.isItemValid(slot, stack) &&
                    (sizeFilter == null || sizeFilter.test(size.getSize(stack))) &&
                    (weightFilter == null || weightFilter.test(size.getWeight(stack)));
        }

        @Override
        public boolean kjs$isMutable() {
            return !sealed;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (sealed) return ItemStack.EMPTY;
            return remove(super.extractItem(slot, amount, true));
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (sealed) return stack;
            return super.insertItem(slot, requiresSeal ? stack : apply(stack), simulate);
        }

        @Info("Seals the inventory if not already")
        public void seal() {
            if (canSeal) {
                sealed = true;
                preserveAll();
            }
            blockEntity.sync();
        }

        @Info("Unseals the inventory, if not already")
        public void unseal() {
            sealed = false;
            if (requiresSeal && canSeal) {
                unPreserverAll();
            }
            blockEntity.sync();
        }

        @Info("Toggles the sealed state of the inventory. Returns the sealed state of the inventory after toggling")
        public boolean toggleSeal() {
            if (canSeal) {
                if (sealed) {
                    unseal();
                } else {
                    seal();
                }
            } else {
                sealed = false;
            }
            blockEntity.sync();
            return sealed;
        }

        @Info("Gets the sealed state of the inventory")
        public boolean isSealed() {
            return sealed;
        }
    }
}
