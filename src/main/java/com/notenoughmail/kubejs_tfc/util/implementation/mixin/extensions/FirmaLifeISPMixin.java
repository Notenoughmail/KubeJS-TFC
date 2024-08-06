package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.eerussianguy.firmalife.FirmaLife;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IFirmaLifeISPExtensions;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@IfPresent(FirmaLife.MOD_ID)
@Mixin(value = ItemStackProviderJS.class, remap = false)
public abstract class FirmaLifeISPMixin implements IFirmaLifeISPExtensions {

    @Shadow
    public abstract ItemStackProviderJS simpleModifier(String s);

    @Override
    public ItemStackProviderJS kubeJS_TFC$firmaLifeAddPiePan() {
        return simpleModifier("firmalife:add_pie_pan");
    }

    @Override
    public ItemStackProviderJS kubeJS_TFC$firmaLifeCopyDynamicFood() {
        return simpleModifier("firmalife:copy_dynamic_food");
    }

    @Override
    public ItemStackProviderJS kubeJS_TFC$firmaLifeEmptyPan() {
        return simpleModifier("firmalife:empty_pan");
    }
}
