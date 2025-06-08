package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.RemapForJS;

@SuppressWarnings("unused")
public interface IFirmaLifeISPExtensions {

    @Info("Adds a 'firmalife:add_pie_pan' modifier to the ISP")
    @RemapForJS("firmaLifeAddPiePan")
    ItemStackProviderJS kubejs_tfc$FirmaLifeAddPiePan();

    @Info("Adds a 'firmalife:copy_dynamic_food' modifier to the ISP")
    @RemapForJS("firmaLifeCopyDynamicFood")
    ItemStackProviderJS kubejs_tfc$FirmaLifeCopyDynamicFood();

    @Info("Adds a 'firmalife:empty_pan' modifier to the ISP")
    @RemapForJS("firmaLifeEmptyPan")
    ItemStackProviderJS kubejs_tfc$FirmaLifeEmptyPan();
}
