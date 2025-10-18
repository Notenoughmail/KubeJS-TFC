package io.github.notenoughmail.kubejstfc.util.mixin;

import io.github.notenoughmail.kubejstfc.implementation.extensions.ISPExtension;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStackProvider.class)
public abstract class ISPMixin implements ISPExtension {
}
