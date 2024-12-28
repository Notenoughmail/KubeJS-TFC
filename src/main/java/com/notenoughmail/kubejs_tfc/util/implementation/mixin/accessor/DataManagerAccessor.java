package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import com.google.common.collect.BiMap;
import net.dries007.tfc.util.DataManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DataManager.class, remap = false)
public interface DataManagerAccessor<T> {

    @Accessor(value = "types", remap = false)
    BiMap<ResourceLocation, T> kubejs_tfc$Types();
}
