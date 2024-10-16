package com.notenoughmail.kubejs_tfc.util.implementation.attachment;

import com.notenoughmail.kubejs_tfc.util.implementation.bindings.MiscBindings;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.block.entity.InventoryAttachment;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.NativeJavaObject;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TFCInventoryAttachment extends InventoryAttachment {

    // {
    //     width: 5,
    //     height: 2,
    //     size: size => size.isSmallerThan('normal')
    // }
    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(
            "tfc:inventory",
            TypeDescJS.object()
                    .add("width", TypeDescJS.NUMBER, false)
                    .add("height", TypeDescJS.NUMBER, false)
                    .add("inputFilter", new PrimitiveDescJS("Ingredient"), true)
                    .add("size", new PrimitiveDescJS("Predicate<Size>"), true)
                    .add("weight", new PrimitiveDescJS("Predicate<Weight>"), true),
            map -> {
                final int width = ((Number) map.get("width")).intValue();
                final int height = ((Number) map.get("height")).intValue();
                final Ingredient inputFilter = map.containsKey("inputFilter") ? IngredientJS.of(map.get("inputFilter")) : null;
                final SizePredicate size = sizePredicate(map);
                final WeightPredicate weight = weightPredicate(map);
                return entity -> new TFCInventoryAttachment(entity, width, height, inputFilter, size, weight);
            }
            );

    @Nullable
    private static SizePredicate sizePredicate(Map<String, Object> map) {
        final Object possibleFunction = map.get("size");
        if (possibleFunction instanceof BaseFunction func) {
            return (SizePredicate) NativeJavaObject.createInterfaceAdapter(ScriptType.STARTUP.manager.get().context, SizePredicate.class, func);
        }
        return null;
    }

    @Nullable
    private static WeightPredicate weightPredicate(Map<String, Object> map) {
        final Object possibleFunction = map.get("weight");
        if (possibleFunction instanceof BaseFunction func) {
            return (WeightPredicate) NativeJavaObject.createInterfaceAdapter(ScriptType.STARTUP.manager.get().context, WeightPredicate.class, func);
        }
        return null;
    }

    @Nullable
    private final SizePredicate size;
    @Nullable
    private final WeightPredicate weight;

    public TFCInventoryAttachment(BlockEntityJS blockEntity, int width, int height, @Nullable Ingredient inputFilter, @Nullable SizePredicate size, @Nullable WeightPredicate weight) {
        super(blockEntity, width, height, inputFilter);
        this.size = size;
        this.weight = weight;
    }

    @Override
    public boolean canAddItem(ItemStack itemStack) {
        boolean allowed = true;
        if (size != null) {
            allowed &= size.test(MiscBindings.INSTANCE.getSize(itemStack));
        }
        if (weight != null) {
            allowed &= weight.test(MiscBindings.INSTANCE.getWeight(itemStack));
        }
        return allowed && super.canAddItem(itemStack);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        boolean allowed = true;
        if (size != null) {
            allowed &= size.test(MiscBindings.INSTANCE.getSize(itemStack));
        }
        if (weight != null) {
            allowed &= weight.test(MiscBindings.INSTANCE.getWeight(itemStack));
        }
        return allowed && super.canPlaceItem(i, itemStack);
    }

    @FunctionalInterface
    public interface SizePredicate {
        boolean test(Size size);
    }

    @FunctionalInterface
    public interface WeightPredicate {
        boolean test(Weight weight);
    }
}
