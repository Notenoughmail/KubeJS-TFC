package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;

public record FluidStackIngredientJS(FluidIngredient ingredient, long amount) implements InputFluid {

    public static final FluidStackIngredientJS EMPTY = new FluidStackIngredientJS(FluidStackIngredient.EMPTY);

    public FluidStackIngredientJS(FluidStackIngredient cannonClass) {
        this(cannonClass.ingredient(), cannonClass.amount());
    }

    @Override
    public long kjs$getAmount() {
        return amount;
    }

    public FluidStackIngredient toCannonClass() {
        return new FluidStackIngredient(ingredient, (int) amount);
    }

    @Override
    public FluidLike kjs$copy(long amount) {
        return new FluidStackIngredientJS(ingredient, amount);
    }

    public JsonElement toJson() {
        return toCannonClass().toJson();
    }

    public JsonElement toJsonNoAmount() {
        return ingredient.toJson();
    }

    @Override
    public boolean matches(FluidLike other) {
        if (other instanceof FluidStackJS js) {
            return ingredient.test(js.getFluid());
        } else if (other instanceof FluidStackIngredientJS js) {
            return js.amount >= amount && js.ingredient.equals(ingredient);
        } else {
            return false;
        }
    }
}
