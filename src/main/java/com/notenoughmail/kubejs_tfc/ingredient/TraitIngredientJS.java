package com.notenoughmail.kubejs_tfc.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TraitIngredientJS implements IngredientJS {

    @Nullable
    private final IngredientJS internalIngredient;
    private final String trait;
    private final boolean hasTrait;

    public TraitIngredientJS(@Nullable IngredientJS ingredient, String trait, boolean hasTrait) {
        internalIngredient = ingredient;
        this.trait = trait;
        this.hasTrait = hasTrait;
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        if (internalIngredient != null) {
            return internalIngredient.test(itemStackJS);
        }
        return FoodCapability.get(itemStackJS.getItemStack()) != null;
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", hasTrait ? "tfc:has_trait" : "tfc:lacks_trait");
        json.addProperty("trait", trait);
        if (internalIngredient != null) {
            json.add("ingredient", internalIngredient.toJson());
        }
        return json;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("Ingredient.");
        builder.append(hasTrait ? "has" : "lacks");
        builder.append("Trait(");
        builder.append(trait);
        if (internalIngredient != null) {
            builder.append(", ");
            builder.append(internalIngredient);
        }
        builder.append(")");
        return builder.toString();
    }
}
