package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.recipes.ingredients.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public enum TFCIngredientBindings {
    INSTANCE;

    @Info(value = "Creates an ingredient of type 'tfc:heatable'", params = {
            @Param(name = "min", value = "The minimum temperature °C of the ingredient, may be null to not have a minimum temperature"),
            @Param(name = "max", value = "The maximum temperature °C of the ingredient, may be null to not have a maximum temperature")
    })
    public Ingredient heatable(@Nullable Integer min, @Nullable Integer max) {
        return HeatableIngredient.of(null, min == null ? Integer.MIN_VALUE : min, max == null ? Integer.MAX_VALUE : max);
    }

    @Info(value = "Creates an ingredient of type 'tfc:heatable'", params = {
            @Param(name = "delegate", value = "The sub-ingredient of the heatable ingredient"),
            @Param(name = "min", value = "The minimum temperature °C of the ingredient, may be null to not have a minimum temperature"),
            @Param(name = "max", value = "The maximum temperature °C of the ingredient, may be null to not have a maximum temperature")
    })
    public Ingredient heatable(Ingredient delegate, @Nullable Integer min, @Nullable Integer max) {
        return HeatableIngredient.of(delegate, min == null ? Integer.MIN_VALUE : min, max == null ? Integer.MAX_VALUE : max);
    }

    @Info(value = "Creates an ingredient of type 'tfc:not'")
    public Ingredient not() {
        return NotIngredient.alwaysTrue();
    }

    @Info(value = "Creates an ingredient of type 'tfc:not'", params = @Param(name = "delegate", value = "The sub-ingredient of the not ingredient"))
    public Ingredient not(Ingredient delegate) {
        return NotIngredient.of(delegate);
    }

    @Info(value = "Creates an ingredient of type 'tfc:fluid_item'", params =@Param(name = "fluid", value = "The fluid stack ingredient of the ingredient"))
    public Ingredient fluid(FluidStackIngredient fluid) {
        return new FluidItemIngredient(null, fluid);
    }

    @Info(value = "Creates an ingredient of type 'tfc:fluid_item'", params = {
            @Param(name = "delegate", value = "The sub-ingredient of the fluid item ingredient"),
            @Param(name = "fluid", value = "The fluid stack ingredient of the ingredient")
    })
    public Ingredient fluid(Ingredient delegate, FluidStackIngredient fluid) {
        return new FluidItemIngredient(delegate, fluid);
    }

    @Info(value = "Creates an ingredient of type 'tfc:has_trait'", params = @Param(name = "trait", value = "The name of the trait that must be present"))
    public Ingredient hasTrait(ResourceLocation trait) {
        return HasTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    @Info(value = "Creates an ingredient of type 'tfc:has_trait'", params = {
            @Param(name = "delegate", value = "The sub-ingredient of the has trait ingredient"),
            @Param(name = "trait", value = "The name of the trait that must be present")
    })
    public Ingredient hasTrait(Ingredient delegate, ResourceLocation trait) {
        return HasTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    @Info(value = "Creates an ingredient of type 'tfc:lacks_trait", params = @Param(name = "trait", value = "The name of the trait that must not be present"))
    public Ingredient lacksTrait(ResourceLocation trait) {
        return LacksTraitIngredient.of(FoodTrait.getTraitOrThrow(trait));
    }

    @Info(value = "Creates an ingredient of type 'tfc:lacks_trait'", params = {
            @Param(name = "delegate", value = "The sub-ingredient of the lacks trait ingredient"),
            @Param(name = "trait", value = "The name of the trait that must not be present")
    })
    public Ingredient lacksTrait(Ingredient delegate, ResourceLocation trait) {
        return LacksTraitIngredient.of(delegate, FoodTrait.getTraitOrThrow(trait));
    }

    @Info(value = "Creates an ingredient of type 'tfc:not_rotten'")
    public Ingredient notRotten() {
        return NotRottenIngredient.of((Ingredient) null); // Cast because there are multiple static 'of' methods
    }

    @Info(value = "Creates an ingredient of type 'tfc:not_rotten'", params = @Param(name = "delegate", value = "The sub-ingredient of the not rotten ingredient"))
    public Ingredient notRotten(Ingredient delegate) {
        return NotRottenIngredient.of(delegate);
    }
}
