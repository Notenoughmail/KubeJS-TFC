package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.eerussianguy.beneath.Beneath;
import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.event.TFCDataEventJS;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IBeneathDataExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@IfPresent(Beneath.MOD_ID)
@Mixin(value = TFCDataEventJS.class, remap = false)
public abstract class BeneathDataEventMixin implements IBeneathDataExtension {

    @Shadow public abstract void addJson(ResourceLocation id, JsonElement json);

    @Override
    public void KubeJS_TFC$AddJson(ResourceLocation id, JsonElement json) {
        addJson(id, json);
    }
}
