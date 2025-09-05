package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapperProviderHolder;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = Context.class, remap = false)
public interface ContextAccessor {

    @Accessor(value = "typeWrappers", remap = false)
    void kubejs_tfc$SetTypeWrappers(TypeWrappers typeWrappers);

    @Accessor(value = "customScriptableWrappers", remap = false)
    List<CustomJavaToJsWrapperProviderHolder<?>> kubejs_tfc$GetReverseWrappers();
}
