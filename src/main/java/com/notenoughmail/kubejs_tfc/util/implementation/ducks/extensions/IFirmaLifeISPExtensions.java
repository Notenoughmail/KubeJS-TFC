package com.notenoughmail.kubejs_tfc.util.implementation.ducks.extensions;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.typings.Info;

public interface IFirmaLifeISPExtensions {

    @Info(value = "Adds a 'firmalife:add_pie_pan' modifier to the ISP")
    ItemStackProviderJS firmaLifeAddPiePan();

    @Info(value = "Adds a 'firmalife:copy_dynamic_food' modifier to the ISP")
    ItemStackProviderJS firmaLifeCopyDynamicFood();

    @Info(value = "Adds a 'firmalife:empty_pan' modifier to the ISP")
    ItemStackProviderJS firmaLifeEmptyPan();
}
