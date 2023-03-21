package com.notenoughmail.kubejs_tfc.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class ScrapingRecipeJS extends TFCRecipeJS {

    private String tex_in = "tfc:item/hide/large/soaked";
    private String tex_out = "tfc:item/hide/large/scraped";

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires 2 arguments - result and ingredient");
        }

        inputItems.add(parseIngredientItem(listJS.get(1)));

        outputItems.add(parseResultItem(listJS.get(0)));
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        inputItems.add(parseIngredientItem(json.get("ingredient")));
        tex_out = json.get("output_texture").getAsString();
        tex_in = json.get("input_texture").getAsString();
    }

    public ScrapingRecipeJS inputTexture(Object o) {
        tex_in = o.toString();
        save();
        return this;
    }

    public ScrapingRecipeJS outputTexture(Object o) {
        tex_out = o.toString();
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
            json.addProperty("output_texture", tex_out);
        }

        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
            json.addProperty("input_texture", tex_in);
        }
    }
}
