package com.notenoughmail.kubejs_tfc.recipe;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class KnappingRecipeJS extends TFCRecipeJS {

    private JsonArray knapPattern = new JsonArray();

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 1) {
            throw new RecipeExceptionJS("Requires 2 arguments - result and pattern");
        }

        json.addProperty("outside_slot_required", true);

        knapPattern = ListJS.orSelf(listJS.get(1)).toJson().getAsJsonArray();

        if (knapPattern.isEmpty()) {
            throw new RecipeExceptionJS("Pattern is empty!");
        }

        /*
         * For two days I have been rewriting this, because no matter what I did
         * the pattern stayed empty and I could not understand why, until I put
         * 'System.out.println("KUBEJS FUN TIMES: " + listJS.size());' at the
         * top of the method and find out ArrayList.size() is 1 indexed! And
         * since this is method is loosely copied from the shaped crafting, I had
         * it so everything was in 'if (listJS.size() < 2) {...}' because that's
         * what I saw on the GitHub, and I was only using the git because
         * downloading sources doesn't do anything! So fun!
         *     E V E R Y T H I N G KubeJS is obfuscated
         * With TFC I can just download the zip from the GitHub releases and set
         * it to be used as the source, works like a charm! Should I have looked
         * closer at the git, sure, but had I sources, I would've seen it earlier
         */
        for (int i = 0; i < knapPattern.size(); i++) {
            json.add("pattern", knapPattern.get(i));
        }

        outputItems.add(parseResultItem(listJS.get(0)));
    }

    @Override
    public void deserialize() {
        outputItems.add(parseResultItem(json.get("result")));
        knapPattern = json.get("pattern").getAsJsonArray();
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }

        // I guess?
        if (serializeInputs) {
            json.add("pattern", knapPattern);
        }
    }

    public KnappingRecipeJS outsideSlotNotRequired() {
        json.addProperty("outside_slot_required", false);
        save();
        return this;
    }

}
