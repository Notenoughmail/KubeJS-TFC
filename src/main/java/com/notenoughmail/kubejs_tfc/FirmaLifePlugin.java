package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.notenoughmail.kubejs_tfc.recipe.KnappingRecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

/**
 * TODO:
 *  - {@link com.eerussianguy.firmalife.common.recipes.DryingRecipe Drying} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.SmokingRecipe Smoking}
 *  - {@link com.eerussianguy.firmalife.common.recipes.MixingBowlRecipe Mixing Bowl}
 *  - Pumpkin Knapping [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.OvenRecipe Oven}
 *  - {@link com.eerussianguy.firmalife.common.recipes.data.FLItemStackModifiers ISP Modifiers}
 *  - {@link com.eerussianguy.firmalife.common.items.FLFoodTraits Food Traits}
 *  - {@link com.eerussianguy.firmalife.common.blocks.greenhouse.Greenhouse.BlockType Greenhouse Blocks?}
 */

public class FirmaLifePlugin {

    public static void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(FLRecipeSerializers.PUMPKIN_KNAPPING.getId(), KnappingRecipeJS::new);
    }
}
