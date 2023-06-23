package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.client.FLClientEvents;
import com.eerussianguy.firmalife.client.FLClientForgeEvents;
import com.eerussianguy.firmalife.client.FLClientSelfTests;
import com.eerussianguy.firmalife.common.FLEvents;
import com.eerussianguy.firmalife.common.FLForgeEvents;
import com.eerussianguy.firmalife.common.misc.FLInteractionManager;
import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.notenoughmail.kubejs_tfc.recipe.KnappingRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.SimpleRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.MixingBowlRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.OvenRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.firmalife.VatRecipeJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

/**
 * TODO:
 *  - {@link com.eerussianguy.firmalife.common.recipes.DryingRecipe Drying} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.SmokingRecipe Smoking} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.MixingBowlRecipe Mixing Bowl} [Y]
 *  - Pumpkin Knapping [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.OvenRecipe Oven} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.VatRecipe Vat} [Y]
 *  - {@link com.eerussianguy.firmalife.common.recipes.data.FLItemStackModifiers ISP Modifiers} [Y]
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

    public static void addClasses(ScriptType type, ClassFilter filter) {
        filter.allow("com.eerussianguy.firmalife");
        filter.deny("com.eerussianguy.firmalife.mixin");
        filter.deny("com.eerussianguy.firmalife.common.network");
        filter.deny(FLInteractionManager.class);
        filter.deny(FLClientEvents.class);
        filter.deny(FLClientForgeEvents.class);
        filter.deny(FLClientSelfTests.class);
        filter.deny(FLEvents.class);
        filter.deny(FLForgeEvents.class);
    }
}
