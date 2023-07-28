package com.notenoughmail.kubejs_tfc.recipe.arborfirmacraft;

import com.notenoughmail.kubejs_tfc.recipe.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;

public class TreeTapRecipeJS extends TFCRecipeJS {

    public float minTemp = -50.0F;
    public float maxTemp = 50.0F;
    public boolean requiresNatural = true;
    public boolean springOnly = false;

    @Override
    public void create(ListJS listJS) {
        if (listJS.size() < 2) {
            throw new RecipeExceptionJS("Requires two arguments - result and ingredient");
        }

        outputFluids.add(FluidStackJS.of(listJS.get(0)));

        blockIngredient = BlockIngredientJS.of(listJS.get(1));

        if (listJS.size() > 2 && listJS.get(2) instanceof Boolean bool) {
            requiresNatural = bool;
        }
    }

    @Override
    public void deserialize() {
        blockIngredient = BlockIngredientJS.fromJson(json.get("input_block"));
        if (json.has("result_fluid")) {
            outputFluids.add(FluidStackJS.fromJson(json.get("result_fluid")));
        }
        if (json.has("minimum_temperature")) {
            minTemp = json.get("minimum_temperature").getAsFloat();
        }
        if (json.has("maximum_temperature")) {
            maxTemp = json.get("maximum_temperature").getAsFloat();
        }
        if (json.has("require_natural_log")) {
            requiresNatural = json.get("requires_natural_log").getAsBoolean();
        }
        if (json.has("spring_only")) {
            springOnly = json.get("spring_only").getAsBoolean();
        }
    }

    public TreeTapRecipeJS minTemp(float min) {
        minTemp = min;
        save();
        return this;
    }

    public TreeTapRecipeJS maxTemp(float max) {
        maxTemp = max;
        save();
        return this;
    }

    public TreeTapRecipeJS requiresNatural(boolean require) {
        requiresNatural = require;
        save();
        return this;
    }

    public TreeTapRecipeJS onlyInSpring() {
        return onlyInSpring(true);
    }

    public TreeTapRecipeJS onlyInSpring(boolean onlySpring) {
        springOnly = onlySpring;
        save();
        return this;
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result_fluid", outputFluids.get(0).toJson());
        }
        if (serializeInputs) {
            json.add("input_block", blockIngredient.toJson());
            json.addProperty("minimum_temperature", minTemp);
            json.addProperty("maximum_temperature", maxTemp);
            json.addProperty("requires_natural_log", requiresNatural);
            json.addProperty("spring_only", springOnly);
        }
    }

    @Override
    public String getFromToString() {
        return (requiresNatural ? "Natural " : "") + blockIngredient + (springOnly ? " --Spring" : " ") + "-> " + outputFluids;
    }
}