package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class RockKnappingRecipeJS extends KnappingRecipeJS {

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 3) {
            throw new RecipeExceptionJS("Requires three arguments - result, pattern, and ingredient");
        }
        super.create(listJS);

        inputItems.add(parseIngredientItem(listJS.get(2)));
    }

    @Override
    public void deserialize() {
        super.deserialize();
        inputItems.add(parseIngredientItem(json.get("ingredient")));
    }

    @Override
    public void serialize() {
        super.serialize();
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }
}
