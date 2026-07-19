package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.component.Bowl;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public enum FoodBindings {
    INSTANCE;

    /**
     * This flag indicates the food is intended to be transiently non-decaying in a recipe context.
     * It is 'preserved' in the first creation of an item stack (deserialization from data) where
     * it gets demoted to {@link IFood#TRANSIENT_NEVER_DECAY_FLAG} which will clear on copy.
     * <p>
     * Intended for usage with recipes which do not support ISPs and have weird stack copying pathways
     * to have creation dates that are similar to the actual calendar time.
     */
    public static final int RECIPE_TRANSIENT_NON_DECAY_FLAG = -10;

    @Info("Gets the food capability of the stack if present")
    @Nullable
    public IFood get(ItemStack stack) {
        return FoodCapability.get(stack);
    }

    @Info("Gets the FoodDefinition applicable to the stack")
    @Nullable
    public FoodDefinition getDefinition(ItemStack stack) {
        return FoodCapability.getDefinition(stack);
    }

    @Info("If the stack has a food capability")
    public boolean has(ItemStack stack) {
        return FoodCapability.has(stack);
    }

    @Info("Applies the given food trait to the stack if possible")
    public ItemStack applyTrait(ItemStack stack, Holder<FoodTrait> trait) {
        return FoodCapability.applyTrait(stack, trait);
    }

    @Info("Removes the given food trait from the stack if present")
    public ItemStack removeTrait(ItemStack stack, Holder<FoodTrait> trait) {
        return FoodCapability.removeTrait(stack, trait);
    }

    @Info("If the stack has the given trait")
    public boolean hasTrait(ItemStack stack, Holder<FoodTrait> trait) {
        return FoodCapability.hasTrait(stack, trait);
    }

    @Info("If the stack is rotten")
    public boolean isRotten(ItemStack stack) {
        return FoodCapability.isRotten(stack);
    }

    @Info("Sets the stack to rotten")
    public ItemStack setRotten(ItemStack stack) {
        return FoodCapability.setRotten(stack);
    }

    @Info("Directly sets the creation date of the stack to the given calendar tick. Generally prefer using food traits")
    public ItemStack setCreationDate(ItemStack stack, long calendarTick) {
        return FoodCapability.setCreationDate(stack, calendarTick);
    }

    @Info("Sets the stack as non-decaying transiently, will not be preserved on stack copy")
    public ItemStack setTransientNonDecaying(ItemStack stack) {
        return FoodCapability.setTransientNonDecaying(stack);
    }

    @Info("Sets the stack as non-decaying transiently for use with non-TFC recipes, will only be preserved over one stack copy")
    public ItemStack setRecipeTransientNonDecaying(ItemStack stack) {
        return setCreationDate(stack, RECIPE_TRANSIENT_NON_DECAY_FLAG);
    }

    @Info("Sets the stack as non-decaying, optionally non-visible in tooltips")
    public ItemStack setNonDecaying(ItemStack stack, boolean invisible) {
        if (invisible) {
            return FoodCapability.setInvisibleNonDecaying(stack);
        } else {
            return FoodCapability.setNonDecaying(stack);
        }
    }

    @Info("Sets the `tfc:bowl` component of the stack as the bowl stack")
    public void setBowl(ItemStack stack, ItemStack bowlStack) {
        stack.set(TFCComponents.BOWL.get(), Bowl.of(bowlStack));
    }
}
