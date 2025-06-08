package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import dev.latvian.mods.kubejs.recipe.special.KubeJSCraftingRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = KubeJSCraftingRecipe.class, remap = false)
public interface KubeJSCraftingRecipeAccessor {

    @Nullable
    @Invoker(value = "getPlayer")
    static Player kubejs_tfc$getPlayer(AbstractContainerMenu menu) {
        return null;
    }
}
