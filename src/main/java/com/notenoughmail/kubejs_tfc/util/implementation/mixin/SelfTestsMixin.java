package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.util.SelfTests;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.StringWriter;
import java.util.Collection;

@Mixin(value = SelfTests.class, remap = false)
public abstract class SelfTestsMixin {

    @Inject(method = "logErrors", at = @At("HEAD"), remap = false)
    private static <T> void kubejs_tfc$LogErrors(String error, Collection<T> errors, Logger logger, CallbackInfoReturnable<Boolean> cir) {
        if (CommonConfig.insertSelfTestsIntoKubeJSConsole.get() && !errors.isEmpty()) {
            final StringWriter message = new StringWriter();
            message.append(error.replace("{}", Integer.toString(errors.size())));
            errors.forEach(t -> {
                message.append("\n    ");
                // The defaults for these are varying degrees of awful
                if (t instanceof Fluid fluid) {
                    message.append(RegistryInfo.FLUID.getId(fluid).toString());
                } else if (t instanceof Item item) {
                    message.append(RegistryInfo.ITEM.getId(item).toString());
                } else if (t instanceof ItemStack stack) {
                    message.append(Integer.toString(stack.getCount())).append(' ').append(RegistryInfo.ITEM.getId(stack.getItem()).toString());
                } else {
                    message.append(String.valueOf(t));
                }
            });
            ConsoleJS.SERVER.error(message.toString());
        }
    }
}
