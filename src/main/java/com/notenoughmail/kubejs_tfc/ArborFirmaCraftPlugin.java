package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.recipe.arborfirmacraft.TreeTapRecipeJS;
import com.therighthon.afc.common.recipe.AFCRecipeSerializers;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

public class ArborFirmaCraftPlugin {

    public static void registerRecipes(RegisterRecipeHandlersEvent event) {
        event.register(AFCRecipeSerializers.TREE_TAPPING.getId(), TreeTapRecipeJS::new);
    }
}