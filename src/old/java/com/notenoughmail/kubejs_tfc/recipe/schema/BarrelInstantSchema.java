package com.notenoughmail.kubejs_tfc.recipe.schema;

import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.InstantBarrelRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface BarrelInstantSchema {

    RecipeKey<ItemStackProviderJS> OUTPUT_ITEM = ItemProviderComponent.PROVIDER.key("output_item").optional(ItemStackProviderJS.EMPTY).preferred("outputItem");
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("output_fluid").optional(EmptyFluidStackJS.INSTANCE).preferred("outputFluid");
    RecipeKey<FluidStackIngredient> INPUT_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("input_fluid").optional(FluidStackIngredient.EMPTY).preferred("inputFluid");
    RecipeKey<InputItem> INPUT_ITEM = ItemComponents.INPUT.key("input_item").optional(InputItem.EMPTY).preferred("inputItem");
    RecipeKey<String> SOUND = StringComponent.ID.key("sound").optional("minecraft:block.brewing_stand.brew");

    RecipeSchema SCHEMA = new RecipeSchema(InstantBarrelRecipeJS.class, InstantBarrelRecipeJS::new, OUTPUT_ITEM, OUTPUT_FLUID, INPUT_FLUID, INPUT_ITEM, SOUND)
            .constructor(); // Make everything through methods because everything is optional (sorta)

}
