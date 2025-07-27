package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.Lazy;
import dev.latvian.mods.rhino.JavaAdapter;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class SealableInventoryAttachment extends TFCInventoryAttachment implements InventoryKJS {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:sealable_inventory",
            TypeDescJS.object()
                    .add("width", TypeDescJS.NUMBER, false)
                    .add("height", TypeDescJS.NUMBER, false)
                    .add("inputFilter", new PrimitiveDescJS("Ingredient"), true)
                    .add("size", new PrimitiveDescJS("Predicate<Size>"), true)
                    .add("weight", new PrimitiveDescJS("Predicate<Weight>"), true)
                    .add("requiresSeal", TypeDescJS.BOOLEAN, true)
                    .add("canSeal", TypeDescJS.BOOLEAN, true)
                    .add("trait", new PrimitiveDescJS("FoodTrait"), true),
            map -> {
                final int width = ((Number) map.get("width")).intValue();
                final int height = ((Number) map.get("height")).intValue();
                final Ingredient inputFilter = map.containsKey("inputFilter") ? IngredientJS.of(map.get("inputFilter")) : null;
                final SizePredicate size = sizePredicate(map);
                final WeightPredicate weight = weightPredicate(map);
                final boolean requiresSealing = TickableAttachment.getBool("requiresSeal", map, true);
                final boolean canSeal = TickableAttachment.getBool("canSeal", map, true);
                if (requiresSealing && !canSeal) throw new IllegalArgumentException("Inventory requires sealing to apply trait, yet sealing was disabled!");
                final ResourceLocation traitId = map.containsKey("trait") ? (ResourceLocation) JavaAdapter.convertResult(ScriptType.STARTUP.manager.get().context, map.get("trait"), ResourceLocation.class): FoodTrait.getId(FoodTraits.PRESERVED);
                final Supplier<FoodTrait> trait = Lazy.of(() -> FoodTrait.getTraitOrThrow(traitId)); // Defer to allow addons to register traits after this happens. I honestly have no idea when this code runs, early I presume
                return entity -> new SealableInventoryAttachment(entity, width, height, inputFilter, size, weight, requiresSealing, canSeal, trait);
            }
    );

    private boolean sealed;
    private final boolean canSeal;
    public final boolean requiresSeal;
    public final Supplier<FoodTrait> trait;

    public SealableInventoryAttachment(BlockEntityJS blockEntity, int width, int height, @Nullable Ingredient inputFilter, @Nullable SizePredicate size, @Nullable WeightPredicate weight, boolean requiresSeal, boolean canSeal, Supplier<FoodTrait> trait) {
        super(blockEntity, width, height, inputFilter, size, weight);
        this.requiresSeal = requiresSeal;
        this.canSeal = canSeal;
        this.trait = trait;
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
            unPreserveAll();
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

    @Info("Returns the sealed state of the inventory")
    public boolean isSealed() { return sealed; }

    @Override
    public boolean kjs$isMutable() {
        return !sealed;
    }

    // Preservation
    private void unPreserveAll() {
        for (int i = 0 ; i < getContainerSize() ; i++) {
            super.setItem(i, remove(super.removeItemNoUpdate(i)));
        }
    }

    private void preserveAll() {
        for (int i = 0 ; i < getContainerSize() ; i++) {
            super.setItem(i, apply(super.removeItemNoUpdate(i)));
        }
    }

    private ItemStack apply(ItemStack s) {
        return FoodCapability.applyTrait(s, trait.get());
    }

    private ItemStack remove(ItemStack s) {
        return FoodCapability.removeTrait(s, trait.get());
    }

    // Permission
    @Override
    public boolean canAddItem(ItemStack itemStack) {
        return !sealed && super.canAddItem(itemStack);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return !sealed && super.canPlaceItem(i, itemStack);
    }

    @Override
    public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
        return !sealed && super.canTakeItem(pTarget, pIndex, pStack);
    }

    // Remove items
    @Override
    public void onRemove(BlockState newState) {
        unPreserveAll();
        super.onRemove(newState);
    }

    @Override
    public List<ItemStack> removeAllItems() {
        if (sealed) return List.of();
        unPreserveAll();
        return super.removeAllItems();
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        if (sealed) return ItemStack.EMPTY;
        return remove(super.removeItem(pIndex, pCount));
    }

    @Override
    public ItemStack removeItemType(Item pItem, int pAmount) {
        if (sealed) return ItemStack.EMPTY;
        return remove(super.removeItemType(pItem, pAmount));
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        if (sealed) return ItemStack.EMPTY;
        return remove(super.removeItemNoUpdate(pIndex));
    }

    // Add items
    @Override
    public ItemStack addItem(ItemStack pStack) {
        if (!requiresSeal) {
            return super.addItem(apply(pStack));
        } else {
            if (sealed) {
                return pStack;
            }
            return super.addItem(pStack);
        }
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        if (!requiresSeal) {
            super.setItem(pIndex, apply(pStack));
        } else {
            if (!sealed) {
                super.setItem(pIndex, pStack);
            }
        }
    }

    // This one is weird as it doesn't actually *remove* the item
    @Override
    public ItemStack getItem(int pIndex) {
        return super.getItem(pIndex);
    }

    @Override
    public CompoundTag writeAttachment() {
        final CompoundTag tag = super.writeAttachment();
        tag.putBoolean("sealed", sealed);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        super.readAttachment(tag);
        sealed = tag.getBoolean("sealed");
        if (sealed || !requiresSeal) {
            preserveAll();
        }
    }
}
