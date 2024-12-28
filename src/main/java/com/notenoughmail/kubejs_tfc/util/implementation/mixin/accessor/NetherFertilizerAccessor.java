package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfPresent(Beneath.MOD_ID)
@Mixin(value = NetherFertilizer.class, remap = false)
public interface NetherFertilizerAccessor {

    @Accessor(value = "values", remap = false)
    float[] kubejs_tfc$Values();
}
