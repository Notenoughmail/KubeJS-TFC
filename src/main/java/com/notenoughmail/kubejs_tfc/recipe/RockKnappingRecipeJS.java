package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.util.ListJS;

public class RockKnappingRecipeJS extends KnappingRecipeJS{

    public void create(ListJS listJS) {
        super.create(listJS);

        inputItems.add(parseIngredientItem(listJS.get(2)));
    }

    public void deserialize() {
        super.deserialize();
        inputItems.add(parseIngredientItem(json.get("ingredient")));
    }

    public void serialize() {
        super.serialize();
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }
}
