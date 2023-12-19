package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.schema;

import com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.js.VatRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public interface VatSchema {

    RecipeKey<InputItem> INPUT_ITEM = ItemComponents.INPUT.key("input_item").preferred("inputItem").optional(InputItem.EMPTY);
    RecipeKey<FluidStackIngredient> INPUT_FLUID = FluidIngredientComponent.STACK_INGREDIENT.key("input_fluid").preferred("inputFluid").optional(FluidStackIngredient.EMPTY);
    RecipeKey<ItemStackProviderJS> OUTPUT_ITEM = ItemProviderComponent.PROVIDER.key("output_item").preferred("outputItem").optional(ItemStackProviderJS.EMPTY);
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("output_fluid").preferred("outputFluid").optional(EmptyFluidStackJS.INSTANCE);
    RecipeKey<Integer> LENGTH = NumberComponent.INT.key("length").optional(600);
    RecipeKey<Float> TEMPERATURE = NumberComponent.FLOAT.key("temperature").optional(300F);
    RecipeKey<OutputItem> JAR = ItemComponents.OUTPUT.key("jar").optional(OutputItem.EMPTY);

    RecipeSchema SCHEMA = new RecipeSchema(VatRecipeJS.class, VatRecipeJS::new, INPUT_ITEM, INPUT_FLUID, OUTPUT_ITEM, OUTPUT_FLUID, LENGTH, TEMPERATURE, JAR)
            .constructor();
}
