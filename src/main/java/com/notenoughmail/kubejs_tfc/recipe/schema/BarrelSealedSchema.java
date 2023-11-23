package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.SealedBarrelRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface BarrelSealedSchema {

    RecipeKey<ItemStackProviderJS> OUTPUT_ITEM = ItemProviderComponent.PROVIDER.key("output_item").optional(ItemStackProviderJS.EMPTY).preferred("outputItem");
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("output_fluid").optional(EmptyFluidStackJS.INSTANCE).preferred("outputFluid");
    RecipeKey<InputItem> INPUT_ITEM = ItemComponents.INPUT.key("input_item").optional(InputItem.EMPTY).preferred("inputItem");
    RecipeKey<FluidStackIngredient> INPUT_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("input_fluid").optional(FluidStackIngredient.EMPTY).preferred("inputFluid");
    RecipeKey<String> SOUND = StringComponent.ID.key("sound").optional("minecraft:block.brewing_stand.brew");
    RecipeKey<Integer> DURATION = NumberComponent.ANY_INT.key("duration");
    RecipeKey<ItemStackProviderJS> ON_SEAL = ItemProviderComponent.PROVIDER.key("on_seal").optional(ItemStackProviderJS.EMPTY).preferred("onSeal");
    RecipeKey<ItemStackProviderJS> ON_UNSEAL = ItemProviderComponent.PROVIDER.key("on_unseal").optional(ItemStackProviderJS.EMPTY).preferred("onUnseal");

    RecipeSchema SCHEMA = new RecipeSchema(SealedBarrelRecipeJS.class, SealedBarrelRecipeJS::new, DURATION, OUTPUT_ITEM, OUTPUT_FLUID, INPUT_ITEM, INPUT_FLUID, SOUND, ON_SEAL, ON_UNSEAL)
            .constructor(DURATION); // Everything is optional, delegate to methods
}
