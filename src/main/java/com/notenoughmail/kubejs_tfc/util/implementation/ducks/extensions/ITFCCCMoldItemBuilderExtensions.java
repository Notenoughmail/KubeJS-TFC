package com.notenoughmail.kubejs_tfc.util.implementation.ducks.extensions;

import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;

import java.util.List;

public interface ITFCCCMoldItemBuilderExtensions {

    @Info(value = "Allows the mold item to be placed in a TFC Casting With Channels mold table")
    MoldItemBuilder tfcccAllowedInMoldTable();

    @Info(value = "Allows the mold item to be placed in a TFC Casting With Channels mold table", params = {
            @Param(name = "model", value = "A list of 14 strings, each 14 chars long, creates the default model for the item when in the mold table")
    })
    @Generics(value = String.class)
    MoldItemBuilder tfcccAllowedInMoldTable(List<String> model);
}
