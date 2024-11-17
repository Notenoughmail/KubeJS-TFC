package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.eerussianguy.beneath.Beneath;
import com.notenoughmail.kubejs_tfc.event.TFCDataEventJS;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IBeneathDataExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import org.spongepowered.asm.mixin.Mixin;

@IfPresent(Beneath.MOD_ID)
@Mixin(value = TFCDataEventJS.class, remap = false)
public abstract class BeneathDataEventMixin implements IBeneathDataExtension {
}
