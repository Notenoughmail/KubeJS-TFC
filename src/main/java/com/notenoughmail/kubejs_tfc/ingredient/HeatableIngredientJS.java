package com.notenoughmail.kubejs_tfc.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import org.jetbrains.annotations.Nullable;

public class HeatableIngredientJS implements IngredientJS {

    @Nullable
    private final IngredientJS internalIngredient;
    @Nullable
    private Integer minTemp;
    @Nullable
    private Integer maxTemp;

    public HeatableIngredientJS(@Nullable IngredientJS ingredient) {
        internalIngredient = ingredient;
    }

    public HeatableIngredientJS(@Nullable IngredientJS ingredient, int min, int max) {
        internalIngredient = ingredient;
        minTemp = min;
        maxTemp = max;
    }

    @Override
    public boolean test(ItemStackJS itemStackJS) {
        if (internalIngredient != null) {
            internalIngredient.test(itemStackJS);
        }
        return HeatCapability.get(itemStackJS.getItemStack()) != null;
    }

    public IngredientJS mintemp(int i) {
        minTemp = i;
        return this;
    }

    public IngredientJS maxTemp(int i) {
        maxTemp = i;
        return this;
    }

    public IngredientJS temps(int min, int max) {
        minTemp = min;
        maxTemp = max;
        return this;
    }

    @Override
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", "tfc:heatable");
        if (minTemp != null) {
            json.addProperty("min_temp", minTemp);
        }
        if (maxTemp != null) {
            json.addProperty("max_temp", maxTemp);
        }
        if (internalIngredient != null) {
            json.add("ingredient", internalIngredient.toJson());
        }
        return json;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("Ingredient.heatable(");
        int i = 0;
        if (internalIngredient != null) {
            i++;
            builder.append(internalIngredient);
        }
        if (minTemp != null) {
            if (i > 0) {
                builder.append(", ");
            }
            i++;
            builder.append("min: ");
            builder.append(minTemp);
        }
        if (maxTemp != null) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append("max: ");
            builder.append(maxTemp);
        }
        builder.append(")");
        return builder.toString();
    }
}
