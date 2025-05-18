package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.notenoughmail.kubejs_tfc.util.implementation.attachment.HeatAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachment;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = HeatCapability.class, remap = false)
public abstract class HeatCapabilityMixin {

    @WrapOperation(method = "provideHeatTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), remap = false)
    private static LazyOptional<IHeatBlock> kubejs_tfc$ProvideToKubeHeat(BlockEntity instance, Capability<IHeatBlock> capability, Operation<LazyOptional<IHeatBlock>> original) {
        if (instance instanceof BlockEntityJS js) {
            for (BlockEntityAttachment attachment : js.attachments) {
                if (attachment instanceof HeatAttachment heat) {
                    return LazyOptional.of(() -> heat);
                }
            }
        }
        return original.call(instance, capability);
    }
}
