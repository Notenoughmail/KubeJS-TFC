package com.notenoughmail.kubejs_tfc.util.implementation.wrapper;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.recipes.ingredients.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

// TODO: JSDocs
public class TFCIngredientWrapper {

    public static Ingredient heatable(int min, int max) {
        return HeatableIngredient.of(null, min, max);
    }

    public static Ingredient heatable(Ingredient delegate, int min, int max) {
        return HeatableIngredient.of(delegate, min, max);
    }

    public static Ingredient not() {
        return NotIngredient.alwaysTrue();
    }

    public static Ingredient not(Ingredient delegate) {
        return NotIngredient.of(delegate);
    }

    public static Ingredient fluid(FluidStackIngredient fluid) {
        return new FluidItemIngredient(null, fluid);
    }

    public static Ingredient fluid(Ingredient delegate, FluidStackIngredient fluid) {
        return new FluidItemIngredient(delegate, fluid);
    }

    public static Ingredient hasTrait(ResourceLocation trait) {
        return HasTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    public static Ingredient hasTrait(Ingredient delegate, ResourceLocation trait) {
        return HasTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    public static Ingredient lacksTrait(ResourceLocation trait) {
        return LacksTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    public static Ingredient lacksTrait(Ingredient delegate, ResourceLocation trait) {
        return LacksTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    public static Ingredient notRotten() {
        return NotRottenIngredient.of((Ingredient) null); // Cast because there are multiple static 'of' methods
    }

    public static Ingredient notRotten(Ingredient delegate) {
        return NotRottenIngredient.of(delegate);
    }
}
