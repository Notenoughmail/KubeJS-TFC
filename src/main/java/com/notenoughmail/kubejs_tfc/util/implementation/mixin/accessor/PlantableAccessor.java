package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.util.Plantable;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfPresent(FirmaLife.MOD_ID)
@Mixin(value = Plantable.class, remap = false)
public interface PlantableAccessor {

    @Accessor(value = "textures", remap = false)
    ResourceLocation[] kubejs_tfc$Textures();

    @Accessor(value = "specials", remap = false)
    ResourceLocation[] kubejs_tfc$Specials();
}
