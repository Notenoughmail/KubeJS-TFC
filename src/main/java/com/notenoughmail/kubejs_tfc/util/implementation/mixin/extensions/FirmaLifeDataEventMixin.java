package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.eerussianguy.firmalife.FirmaLife;
import com.notenoughmail.kubejs_tfc.event.TFCDataEventJS;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IFirmaLifeDataExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import org.spongepowered.asm.mixin.Mixin;

@IfPresent(FirmaLife.MOD_ID)
@Mixin(value = TFCDataEventJS.class, remap = false)
public abstract class FirmaLifeDataEventMixin implements IFirmaLifeDataExtension {
}
