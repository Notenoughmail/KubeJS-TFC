package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ducks.extensions.IFirmaLifeISPExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemStackProviderJS.class, remap = false)
public abstract class FirmaLifeISPMixin implements IFirmaLifeISPExtensions {

    @Shadow
    public abstract ItemStackProviderJS simpleModifier(String s);

    @Override
    public ItemStackProviderJS firmaLifeAddPiePan() {
        return simpleModifier("firmalife:add_pie_pan");
    }

    @Override
    public ItemStackProviderJS firmaLifeCopyDynamicFood() {
        return simpleModifier("firmalife:copy_dynamic_food");
    }

    @Override
    public ItemStackProviderJS firmaLifeEmptyPan() {
        return simpleModifier("firmalife:empty_pan");
    }
}
