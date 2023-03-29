package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.notenoughmail.kubejs_tfc.recipe.KnappingRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.SimpleRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.MixingBowlRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.OvenRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.VatRecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

/**
 * TODO:
 *  - {@link com.eerussianguy.firmalife.common.recipes.DryingRecipe Drying} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.SmokingRecipe Smoking} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.MixingBowlRecipe Mixing Bowl} [Y]
 *  - Pumpkin Knapping [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.OvenRecipe Oven} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.VatRecipe Vat} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.data.FLItemStackModifiers ISP Modifiers}
 *  - {@link com.eerussianguy.firmalife.common.items.FLFoodTraits Food Traits}
 *  - {@link com.eerussianguy.firmalife.common.blocks.greenhouse.Greenhouse.BlockType Greenhouse Blocks?}
 */

public class FirmaLifePlugin {

    public static void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(FLRecipeSerializers.PUMPKIN_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(FLRecipeSerializers.DRYING.getId(), SimpleRecipeJS::new);
        event.register(FLRecipeSerializers.SMOKING.getId(), SimpleRecipeJS::new);
        event.register(FLRecipeSerializers.OVEN.getId(), OvenRecipeJS::new);
        event.register(FLRecipeSerializers.MIXING_BOWL.getId(), MixingBowlRecipeJS::new);
        event.register(FLRecipeSerializers.VAT.getId(), VatRecipeJS::new);
    }
}
