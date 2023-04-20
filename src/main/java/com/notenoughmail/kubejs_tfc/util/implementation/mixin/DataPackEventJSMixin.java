package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;

// The number of iterations of trying to get a 'tfc.data' event type only to settle for this is astounding
@Mixin(DataPackEventJS.class)
public abstract class DataPackEventJSMixin {

    @Shadow
    public abstract void addJson(ResourceLocation resourceLocation, JsonElement json);

    @Unique
    public void addTFCFuel(Object ingredient, float temperature, int duration) {
        var ingredientJS = IngredientJS.of(ingredient).unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(dataID("fuels/" + ingredientToName(ingredientJS)), json);
    }

    @Unique
    public void addTFCHeat(Object ingredient, float... values) {
        var ingredientJS = IngredientJS.of(ingredient).unwrapStackIngredient().get(0);
        if (values.length < 1) {
            throw new IllegalArgumentException("Heat data for " + ingredientJS.toString() + "does not define a heat capacity");
        }
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("heat_capacity", values[0]);
        if (values.length >= 2) {
            json.addProperty("forging_temperature", values[1]);
        }
        if (values.length >= 3) {
            json.addProperty("welding_temperature", values[2]);
        }
        addJson(dataID("item_heats/" + ingredientToName(ingredientJS)), json);
    }

    @Unique
    public void addTFCLampFuel(Object fluidIngredient, Object blockIngredient, int burnRate) {
        var fluidIngredientJS = FluidStackIngredientJS.of(fluidIngredient);
        var blockIngredientJS = BlockIngredientJS.of(blockIngredient);
        var json = new JsonObject();
        json.add("fluid", fluidIngredientJS.toJsonNoAmount());
        json.add("valid_lamps", blockIngredientJS.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(dataID("lamp_fuels/" + ingredientToName(fluidIngredientJS)), json);
    }

    @Unique
    public void addTFCMetal(String fluid, float meltTemp, float heatCap, Object ingotIngredient, Object sheetIngredient) {
        addTFCMetal(fluid, meltTemp, heatCap, ingotIngredient, sheetIngredient, 0);
    }

    @Unique
    public void addTFCMetal(String fluid, float meltTemp, float heatCap, Object ingotIngredient, Object sheetIngredient, int tier) {
        var ingotIngredientJS = IngredientJS.of(ingotIngredient).unwrapStackIngredient().get(0);
        var sheetIngredientJS = IngredientJS.of(sheetIngredient).unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.addProperty("tier", tier);
        json.addProperty("fluid", fluid);
        json.addProperty("melt_temperature", meltTemp);
        json.addProperty("specific_heat_capacity", heatCap);
        json.add("ingots", ingotIngredientJS.toJson());
        json.add("sheets", sheetIngredientJS.toJson());
        addJson(dataID("metals/" + ingredientToName(fluid)), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    @Unique
    public void addTFCSupport(Object blockIngredient, int up, int down, int horizontal) {
        var blockIngredientJS = BlockIngredientJS.of(blockIngredient);
        var json = new JsonObject();
        json.add("ingredient", blockIngredientJS.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(dataID("supports/" + ingredientToName(blockIngredientJS)), json);
    }

    private ResourceLocation dataID(String location) {
        return new ResourceLocation("kubejs_tfc", "tfc/" + location);
    }

    private String ingredientToName(Object in) {
        return in.toString().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_") // Make the string not explode when parsed (yes I know there are a few more allowed chars, but it doesn't matter)
                .replaceAll("_+", "_") // Remove duplicate underscores
                .replaceAll("^_", "") // Remove leading underscores
                .replaceAll("_$", ""); // Remove trailing underscores
    }
}
