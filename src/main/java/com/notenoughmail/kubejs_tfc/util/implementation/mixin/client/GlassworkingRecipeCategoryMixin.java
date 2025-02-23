package com.notenoughmail.kubejs_tfc.util.implementation.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.notenoughmail.kubejs_tfc.util.implementation.CustomGlassOperations;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.dries007.tfc.compat.jei.category.GlassworkingRecipeCategory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GlassworkingRecipeCategory.class, remap = false)
public abstract class GlassworkingRecipeCategoryMixin {

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;"), remap = false)
    private ImmutableMap<GlassOperation, ItemStack> kubejs_tfc$test(ImmutableMap<GlassOperation, ItemStack> original) {
        if (CustomGlassOperations.isEmpty()) {
            return original;
        } else {
            ImmutableMap.Builder<GlassOperation, ItemStack> builder = new ImmutableMap.Builder<>();
            builder.putAll(original);
            CustomGlassOperations.addDisplays(builder::put);
            return builder.build();
        }
    }
}
