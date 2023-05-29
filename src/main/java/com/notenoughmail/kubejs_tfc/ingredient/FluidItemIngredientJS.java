package com.notenoughmail.kubejs_tfc.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.dries007.tfc.common.recipes.ingredients.FluidItemIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class FluidItemIngredientJS implements IngredientJS {

    @Nullable
    private final IngredientJS internalIngredient;
    private final FluidStackIngredientJS fluidIngredient;

    public FluidItemIngredientJS(@Nullable IngredientJS ingredient, FluidStackIngredientJS fluidIngredient) {
        internalIngredient = ingredient;
        this.fluidIngredient = fluidIngredient;
    }

    public IngredientJS withFluidAmount(int i) {
        fluidIngredient.withAmount(i);
        return this;
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        if (internalIngredient != null) {
            FluidItemIngredient testHelper = new FluidItemIngredient(
                    Ingredient.fromJson(internalIngredient.toJson()),
                    FluidStackIngredient.fromJson(fluidIngredient.toJson())
            );
            return testHelper.test(itemStackJS.getItemStack());
        }
        FluidItemIngredient testHelper = new FluidItemIngredient(
                null,
                FluidStackIngredient.fromJson(fluidIngredient.toJson())
        );
        return testHelper.test(itemStackJS.getItemStack());
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:fluid_item");
        json.add("fluid_ingredient", fluidIngredient.toJson());
        if (internalIngredient != null) {
            json.add("ingredient", internalIngredient.toJson());
        }
        return json;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("Ingredient.fluidItem(");
        builder.append(fluidIngredient);
        if (internalIngredient != null) {
            builder.append(", ");
            builder.append(internalIngredient);
        }
        builder.append(")");
        return builder.toString();
    }
}
