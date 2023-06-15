package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.fluid.FluidStackJS;

import java.util.function.BiFunction;

public interface IRecipeJSExtension {

    default boolean tfcReplaceFluidInput(FluidStackIngredientJS i, FluidStackIngredientJS with, boolean exact) {
        return tfcReplaceFluidInput(i, with, exact, (in, original) -> in.withAmount(original.getAmount()));
    }

    default boolean tfcReplaceFluidInput(FluidStackIngredientJS i, FluidStackIngredientJS with, boolean exact, BiFunction<FluidStackIngredientJS, FluidStackIngredientJS, FluidStackIngredientJS> function) {
        return false;
    }

    default boolean tfcReplaceFluidOutput(FluidStackJS o, FluidStackJS with, boolean exact) {
        return tfcReplaceFluidOutput(o, with, exact, (out, original) -> out.withAmount(original.getAmount()));
    }

    default boolean tfcReplaceFluidOutput(FluidStackJS o, FluidStackJS with, boolean exact, BiFunction<FluidStackJS, FluidStackJS, FluidStackJS> function) {
        return false;
    }

    default boolean tfcReplaceBlockInput(BlockIngredientJS i, BlockIngredientJS with, boolean exact) {
        return false;
    }

    default boolean tfcReplaceItemProvider(ItemStackProviderJS out, ItemStackProviderJS with, boolean exact) {
        return tfcReplaceItemProvider(out, with, exact, (provider, original) -> ItemStackProviderJS.of(provider.getJsonStack(), original.getModifiers()));
    }

    default boolean tfcReplaceItemProvider(ItemStackProviderJS out, ItemStackProviderJS with, boolean exact, BiFunction<ItemStackProviderJS, ItemStackProviderJS, ItemStackProviderJS> function) {
        return false;
    }
}
