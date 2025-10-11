package io.github.notenoughmail.kubejstfc.implementation.attachments;

import dev.latvian.mods.kubejs.block.entity.*;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import dev.latvian.mods.kubejs.util.Cast;
import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.util.Helpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TFCInventoryAttachment extends InventoryAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(Helpers.identifier("inventory"), Factory.class);

    public record Factory(int width, int height, Optional<ItemPredicate> inputFilter, Optional<Predicate<Size>> sizeFilter, Optional<Predicate<Weight>> weightFilter) implements BlockEntityAttachmentFactory {

        @Override
        public BlockEntityAttachment create(BlockEntityAttachmentInfo info, KubeBlockEntity entity) {
            return new TFCInventoryAttachment(entity, width, height, inputFilter.orElse(null), sizeFilter.orElse(null), weightFilter.orElse(null), a -> a.new Inv(a));
        }

        @Override
        public List<BlockCapability<?, ?>> getCapabilities() {
            return List.of(Capabilities.ItemHandler.BLOCK);
        }
    }

    @Nullable
    public final Predicate<Size> sizeFilter;
    @Nullable
    public final Predicate<Weight> weightFilter;
    public final Inv inventory;

    public TFCInventoryAttachment(KubeBlockEntity blockEntity, int width, int height, @Nullable ItemPredicate inputFilter, @Nullable Predicate<Size> sizeFilter, @Nullable Predicate<Weight> weightFilter, Function<TFCInventoryAttachment, Inv> invMaker) {
        super(blockEntity, width, height, inputFilter);
        this.sizeFilter = sizeFilter;
        this.weightFilter = weightFilter;
        inventory = makeInv();
    }

    protected Inv makeInv() {
        return new Inv(this);
    }

    @Override
    public Object getWrappedObject() {
        return inventory;
    }

    @Override
    public <CAP, SRC> @Nullable CAP getCapability(BlockCapability<CAP, SRC> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return Cast.to(inventory);
        }

        return null;
    }

    @Override
    public void onRemove(ServerLevel level, KubeBlockEntity blockEntity, BlockState newState) {
        assert blockEntity.getLevel() != null;
        Containers.dropContents(blockEntity.getLevel(), blockEntity.getBlockPos(), inventory.stacks());
    }

    public class Inv extends Wrapped {

        public Inv(TFCInventoryAttachment attachment) {
            super(attachment);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            final IItemSize size = ItemSizeManager.get(stack);
            return super.isItemValid(slot, stack) &&
                    (sizeFilter == null || sizeFilter.test(size.getSize(stack))) &&
                    (weightFilter == null || weightFilter.test(size.getWeight(stack)));
        }
    }
}
