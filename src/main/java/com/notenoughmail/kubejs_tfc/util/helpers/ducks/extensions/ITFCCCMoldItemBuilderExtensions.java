package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.RemapForJS;

@SuppressWarnings("unused")
public interface ITFCCCMoldItemBuilderExtensions {

    @Info("Allows the mold item to be placed in a TFC Casting With Channels mold table")
    @RemapForJS("tfcccAllowedInMoldTable")
    MoldItemBuilder kubejs_tfc$TFCCCAllowedInMoldTable();

    @Info(value = "Allows the mold item to be placed in a TFC Casting With Channels mold table", params = {
            @Param(name = "model", value = "A list of 14 strings, each 14 chars long, creates the default model for the item when in the mold table")
    })
    @RemapForJS("tfcccAllowedInMoldTable")
    MoldItemBuilder kubejs_tfc$TFCCCAllowedInMoldTable(String[] model);
}
