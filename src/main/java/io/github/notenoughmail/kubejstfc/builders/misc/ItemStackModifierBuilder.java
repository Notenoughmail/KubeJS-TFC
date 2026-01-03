package io.github.notenoughmail.kubejstfc.builders.misc;

import com.mojang.serialization.MapCodec;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@ReturnsSelf
public class ItemStackModifierBuilder extends BuilderBase<ItemStackModifierType<ItemStackModifierBuilder>> implements ItemStackModifier {

    public transient boolean inputDependent = false;
    public transient Applicator applicator = (s, i, c) -> s;

    public ItemStackModifierBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("Sets the applicator of the modifier")
    public ItemStackModifierBuilder applicator(SimpleApplicator applicator) {
        this.applicator = applicator;
        return this;
    }

    @Info("Sets the applicator of the modifier, has access to the input item")
    public ItemStackModifierBuilder applicatorWithInput(Applicator applicator) {
        this.applicator = applicator;
        inputDependent = true;
        return this;
    }

    @Info("Sets the applicator of the modifier, has access to the input item and inventory")
    public ItemStackModifierBuilder applicatorWithInventory(ApplicatorWithInventory applicator) {
        return applicatorWithInput(applicator);
    }

    @Override
    public ItemStack apply(ItemStack stack, ItemStack input, Context context) {
        return applicator.apply(stack, input, context);
    }

    @Override
    public boolean dependsOnInput() {
        return inputDependent;
    }

    @Override
    public ItemStackModifierType<ItemStackModifierBuilder> type() {
        return get();
    }

    @Override
    public ItemStackModifierType<ItemStackModifierBuilder> createObject() {
        return new ItemStackModifierType<>(
                MapCodec.unit(() -> this),
                StreamCodec.unit(this)
        );
    }

    @FunctionalInterface
    public interface SimpleApplicator extends Applicator {
        ItemStack apply(ItemStack stack, Context ctx);

        @Override
        default ItemStack apply(ItemStack stack, ItemStack input, Context ctx) {
            return apply(stack, ctx);
        }
    }

    @FunctionalInterface
    public interface Applicator {
        ItemStack apply(ItemStack stack, ItemStack input, Context ctx);
    }

    @FunctionalInterface
    public interface ApplicatorWithInventory extends Applicator {
        ItemStack apply(ItemStack stack, ItemStack input, Context ctx, Iterable<ItemStack> inventory);

        @Override
        default ItemStack apply(ItemStack stack, ItemStack input, Context ctx) {
            return apply(stack, input, ctx, RecipeHelpers.getCraftingInput());
        }
    }
}
