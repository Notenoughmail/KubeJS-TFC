package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.MapJS;

public class AlloyPartComponent implements RecipeComponent<AlloyPartComponent.AlloyPart> {

    public static RecipeComponent<AlloyPart[]> ALLOY = new AlloyPartComponent().asArray();

    @Override
    public String componentType() {
        return "AlloyPart";
    }

    @Override
    public Class<?> componentClass() {
        return AlloyPart.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, AlloyPart value) {
        return value.toJson();
    }

    @Override
    public AlloyPart read(RecipeJS recipe, Object from) {
        if (from instanceof AlloyPart part) {
            return part;
        } else if (from instanceof JsonObject json) {
            return AlloyPart.fromJson(json);
        } else {
            final JsonObject json = MapJS.json(from);
            if (json != null) {
                return AlloyPart.fromJson(json);
            } else {
                throw new RecipeExceptionJS("Could not parse " + from + " into an AlloyPart!");
            }
        }
    }

    public record AlloyPart(String metal, double min, double max) {

        public JsonObject toJson() {
            final JsonObject json = new JsonObject();
            json.addProperty("metal", metal);
            json.addProperty("min", min);
            json.addProperty("max", max);
            return json;
        }

        public static AlloyPart fromJson(JsonObject json) {
            return new AlloyPart(json.get("metal").getAsString(), json.get("min").getAsDouble(), json.get("max").getAsDouble());
        }
    }
}
