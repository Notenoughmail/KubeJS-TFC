package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class AlloyRecipeJS extends TFCRecipeJS {

    private JsonArray contents = new JsonArray();
    private String resultMetal;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires at least two arguments - result and contents");
        }

        resultMetal = listJS.get(0).toString();

        var metals = ListJS.of(listJS.get(1));
        contents = buildMetals(metals);

        if (contents.size() < 2) {
            throw new RecipeExceptionJS("Contents must have at least two members");
        }
    }


    @Override
    public void deserialize() {
        resultMetal = json.get("result").toString();
        contents.add(json.get("contents"));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.addProperty("result", resultMetal);
        }

        if (serializeInputs) {
            json.add("contents", contents);
        }
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("[['");
        builder.append(contents.get(0).getAsJsonObject().get("metal").getAsString());
        builder.append("', ");
        builder.append(contents.get(0).getAsJsonObject().get("min").getAsDouble());
        builder.append(", ");
        builder.append(contents.get(0).getAsJsonObject().get("max").getAsDouble());
        builder.append("]");
        for (int i = 1 ; i < contents.size() ; i++) {
            builder.append(", ['");
            var member = contents.get(i).getAsJsonObject();
            builder.append(member.get("metal").getAsString());
            builder.append("', ");
            builder.append(member.get("min").getAsDouble());
            builder.append(", ");
            builder.append(member.get("max").getAsDouble());
            builder.append("]");
        }
        builder.append("] -> [");
        builder.append(resultMetal);
        builder.append("]");
        return builder.toString();
    }
}
