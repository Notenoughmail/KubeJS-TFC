package com.notenoughmail.kubejs_tfc.recipe.firmalife;

import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.notenoughmail.kubejs_tfc.recipe.KnappingRecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

public class FirmaLifeRecipes {

    public static void add(RegisterRecipeHandlersEvent event) {
        event.register(FLRecipeSerializers.PUMPKIN_KNAPPING.getId(), KnappingRecipeJS::new);
    }
}
