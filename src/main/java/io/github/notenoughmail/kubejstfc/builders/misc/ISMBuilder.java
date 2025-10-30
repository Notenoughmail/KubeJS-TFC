package io.github.notenoughmail.kubejstfc.builders.misc;

import com.mojang.serialization.MapCodec;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ISMBuilder extends BuilderBase<ItemStackModifierType<ISMBuilder>> implements ItemStackModifier {

    public transient boolean inputDependent = false;
    public transient Applicator applicator = (s, i, c) -> s;

    public ISMBuilder(ResourceLocation id) {
        super(id);
    }

    @Info("Sets the applicator of this modifier")
    public ISMBuilder applicator(Applicator applicator) {
        this.applicator = applicator;
        return this;
    }

    @Info("Sets the applicator of this modifier, has access to the inventory")
    public ISMBuilder applicatorWithInventory(ApplicatorWithInventory applicator) {
        inputDependent = true;
        return applicator(applicator);
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
    public ItemStackModifierType<ISMBuilder> type() {
        return get();
    }

    @Override
    public ItemStackModifierType<ISMBuilder> createObject() {
        return new ItemStackModifierType<>(
                MapCodec.unit(() -> this),
                StreamCodec.unit(this)
        );
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
