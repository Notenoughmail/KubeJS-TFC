package io.github.notenoughmail.kubejstfc.implementation.attachments;

import dev.latvian.mods.kubejs.block.entity.*;
import dev.latvian.mods.kubejs.item.ItemPredicate;
import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TFCInventoryAttachment extends InventoryAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(Helpers.identifier("inventory"), Factory.class);

    public record Factory(int width, int height, Optional<ItemPredicate> inputFilter, Optional<Predicate<Size>> sizeFilter, Optional<Predicate<Weight>> weightFilter) implements BlockEntityAttachmentFactory {

        @Override
        public TFCInventoryAttachment create(BlockEntityAttachmentInfo info, KubeBlockEntity entity) {
            return new TFCInventoryAttachment(entity, width, height, inputFilter.orElse(null), sizeFilter.orElse(null), weightFilter.orElse(null));
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

    public TFCInventoryAttachment(KubeBlockEntity blockEntity, int width, int height, @Nullable ItemPredicate inputFilter, @Nullable Predicate<Size> sizeFilter, @Nullable Predicate<Weight> weightFilter) {
        super(blockEntity, width, height, inputFilter);
        this.sizeFilter = sizeFilter;
        this.weightFilter = weightFilter;
    }

    @Override
    protected Wrapped createInventory() {
        return new Wrapped(this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                final IItemSize size = ItemSizeManager.get(stack);
                return super.isItemValid(slot, stack) &&
                        (sizeFilter == null || sizeFilter.test(size.getSize(stack))) &&
                        (weightFilter == null || weightFilter.test(size.getWeight(stack)));
            }
        };
    }
}
