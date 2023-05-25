package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.IIngredientWrapperMixin;
import dev.latvian.mods.kubejs.bindings.IngredientWrapper;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Left empty because the methods <strong>must</strong> be static which isn't allowed in mixins, so the logic is done in the interface
 */
@Mixin(value = IngredientWrapper.class, remap = false)
public abstract class IngredientWrapperMixin implements IIngredientWrapperMixin {
}
