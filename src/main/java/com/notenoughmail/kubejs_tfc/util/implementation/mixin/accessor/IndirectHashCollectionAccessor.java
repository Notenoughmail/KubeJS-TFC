package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.util.collections.IndirectHashCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;
import java.util.Map;

@Mixin(value = IndirectHashCollection.class, remap = false)
public interface IndirectHashCollectionAccessor<K, R> {

    @Accessor(value = "indirectResultMap", remap = false)
    Map<K, Collection<R>> kubejs_tfc$InternalMap();
}
