package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.recipes.ingredients.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

// TODO: JSDocs
public enum TFCIngredientBindings {
    @HideFromJS
    INSTANCE;

    public Ingredient heatable(@Nullable Integer min, @Nullable Integer max) {
        return HeatableIngredient.of(null, min == null ? Integer.MIN_VALUE : min, max == null ? Integer.MAX_VALUE : max);
    }

    public Ingredient heatable(Ingredient delegate, @Nullable Integer min, @Nullable Integer max) {
        return HeatableIngredient.of(delegate, min == null ? Integer.MIN_VALUE : min, max == null ? Integer.MAX_VALUE : max);
    }

    public Ingredient not() {
        return NotIngredient.alwaysTrue();
    }

    public Ingredient not(Ingredient delegate) {
        return NotIngredient.of(delegate);
    }

    public Ingredient fluid(FluidStackIngredient fluid) {
        return new FluidItemIngredient(null, fluid);
    }

    public Ingredient fluid(Ingredient delegate, FluidStackIngredient fluid) {
        return new FluidItemIngredient(delegate, fluid);
    }

    public Ingredient hasTrait(ResourceLocation trait) {
        return HasTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    public Ingredient hasTrait(Ingredient delegate, ResourceLocation trait) {
        return HasTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    public Ingredient lacksTrait(ResourceLocation trait) {
        return LacksTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    public Ingredient lacksTrait(Ingredient delegate, ResourceLocation trait) {
        return LacksTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    public Ingredient notRotten() {
        return NotRottenIngredient.of((Ingredient) null); // Cast because there are multiple static 'of' methods
    }

    public Ingredient notRotten(Ingredient delegate) {
        return NotRottenIngredient.of(delegate);
    }
}
