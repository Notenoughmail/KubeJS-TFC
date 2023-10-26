package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.data.*;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.*;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCDataEventJS extends EventJS {
    
    private final DataJsonGenerator generator;
    
    public TFCDataEventJS(DataJsonGenerator generator) {
        this.generator = generator;
    }

    public void addTFCItemDamageResistance(IngredientJS ingredient, String values) { // "p=20, c=50"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleResistances(values, json);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "item_damage_resistances"), json);
    }

    public void addTFCItemDamageResistance(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleResistances(values, json);
        generator.json(DataUtils.dataID(name, "tfc", "item_damage_resistances"), json);
    }

    public void addTFCEntityDamageResistance(String entityTag, String values) { // "p=20, c=50"
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(values, json);
        generator.json(DataUtils.dataIDFromObject(entityTag, "tfc", "entity_damage_resistances"), json);
    }

    public void addTFCEntityDamageResistance(String entityTag, String values, ResourceLocation name) {
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(values, json);
        generator.json(DataUtils.dataID(name, "tfc", "entity_damage_resistances"), json);
    }

    public void addTFCDrinkable(FluidStackIngredientJS fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        generator.json(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "drinkables"), data.toJson());
    }

    public void addTFCDrinkable(FluidStackIngredientJS fluidIngredient, Consumer<BuildDrinkableData> drinkableData, ResourceLocation name) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        generator.json(DataUtils.dataID(name, "tfc", "drinkables"), data.toJson());
    }

    public void addTFCFertilizer(IngredientJS ingredient, String values) { // "n=0.2, p=0.2, k=0.2"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleFertilizers(values, json);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "fertilizers"), json);
    }

    public void addTFCFertilizer(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleFertilizers(values, json);
        generator.json(DataUtils.dataID(name, "tfc", "fertilizers"), json);
    }

    public void addTFCFoodItem(IngredientJS ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildFoodItemData(ingredientJS);
        foodItemData.accept(data);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "food_items"), data.toJson());
    }

    public void addTFCFoodItem(IngredientJS ingredient, Consumer<BuildFoodItemData> foodItemData, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildFoodItemData(ingredientJS);
        foodItemData.accept(data);
        generator.json(DataUtils.dataID(name, "tfc", "food_items"), data.toJson());
    }

    public void addTFCFuel(IngredientJS ingredient, float temperature, int duration) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "fuels"), json);
    }

    public void addTFCFuel(IngredientJS ingredient, float temperature, int duration, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        generator.json(DataUtils.dataID(name, "tfc", "fuels"), json);
    }

    public void addTFCHeat(IngredientJS ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "item_heats"), DataUtils.buildHeat(ingredientJS, heatCapacity, forgingTemperature, weldingTemperature));
    }

    public void addTFCHeat(IngredientJS ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        generator.json(DataUtils.dataID(name, "tfc", "item_heats"), DataUtils.buildHeat(ingredientJS, heatCapacity, forgingTemperature, weldingTemperature));
    }

    public void addTFCItemSize(IngredientJS ingredient, String values) { // "s=tiny, weight=medium"
        var ingredientjS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientjS.toJson());
        DataUtils.handleItemSize(values, json);
        generator.json(DataUtils.dataIDFromObject(ingredientjS, "tfc", "item_sizes"), json);
    }

    public void addTFCItemSize(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleItemSize(values, json);
        generator.json(DataUtils.dataID(name, "tfc", "item_sizes"), json);
    }

    public void addTFCLampFuel(FluidStackIngredientJS fluidIngredient, BlockIngredientJS blockIngredient, int burnRate) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJsonNoAmount());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        generator.json(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "lamp_fuels"), json);
    }

    public void addTFCLampFuel(FluidStackIngredientJS fluidIngredient, BlockIngredientJS blockIngredient, int burnRate, ResourceLocation name) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJsonNoAmount());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        generator.json(DataUtils.dataID(name, "tfc", "lamp_fuels"), json);
    }

    public void addTFCMetal(String fluid, float meltTemperature, float heatCapacity, IngredientJS ingot, IngredientJS sheet, int tier) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, sheet, tier);
        generator.json(DataUtils.dataIDFromObject(fluid, "tfc", "metals"), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    public void addTFCMetal(String fluid, float meltTemperature, float heatCapacity, IngredientJS ingot, IngredientJS sheet, int tier, ResourceLocation name) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, sheet, tier);
        generator.json(DataUtils.dataID(name, "tfc", "metals"), json);
    }

    public void addTFCSupport(BlockIngredientJS blockIngredient, int up, int down, int horizontal) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        generator.json(DataUtils.dataIDFromObject(blockIngredient, "tfc", "supports"), json);
    }

    public void addTFCSupport(BlockIngredientJS blockIngredient, int up, int down, int horizontal, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        generator.json(DataUtils.dataID(name, "tfc", "supports"), json);
    }

    public void addTFCSluicing(IngredientJS ingredient, String lootTable) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("loot_table", lootTable);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "tfc", "sluicing"), json);
    }

    public void addTFCSluicing(IngredientJS ingredient, String lootTable, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("loot_table", lootTable);
        generator.json(DataUtils.dataID(name, "tfc", "sluicing"), json);
    }

    public void addTFCPanning(BlockIngredientJS blockIngredient, String lootTable, List<String> models) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        generator.json(DataUtils.dataIDFromObject(blockIngredient, "tfc", "panning"), json);
    }

    public void addTFCPanning(BlockIngredientJS blockIngredient, String lootTable, List<String> models, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        generator.json(DataUtils.dataID(name, "tfc", "panning"), json);
    }

    public void addTFCFauna(Consumer<PlacedFeatureProperties.Climate> climate, Consumer<BuildFaunaData> fauna, ResourceLocation name) {
        var climateObj = new PlacedFeatureProperties.Climate();
        climate.accept(climateObj);
        var faunaObj = new BuildFaunaData(climateObj);
        fauna.accept(faunaObj);
        generator.json(DataUtils.dataID(name, "tfc", "fauna"), faunaObj.toJson());
    }

    public void addTFCClimateRange(Consumer<BuildClimateRangeData> climateRange, ResourceLocation name) {
        var climateRageObj = new BuildClimateRangeData();
        climateRange.accept(climateRageObj);
        generator.json(DataUtils.dataID(name, "tfc", "climate_ranges"), climateRageObj.toJson());
    }

    public void addFLGreenhouse(BlockIngredientJS blockIngredient, int tier) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        generator.json(DataUtils.dataIDFromObject(blockIngredient, "firmalife", "greenhouse"), json);
    }

    public void addFLGreenhouse(BlockIngredientJS blockIngredient, int tier, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        generator.json(DataUtils.dataID(name, "firmalife", "greenhouse"), json);
    }

    public void addFLPlantable(IngredientJS ingredient, Consumer<BuildPlantableData> plantableData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildPlantableData(ingredientJS);
        plantableData.accept(data);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "firmalife", "plantable"), data.toJson());
    }

    public void addFLPlantable(IngredientJS ingredient, Consumer<BuildPlantableData> plantableData, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildPlantableData(ingredientJS);
        plantableData.accept(data);
        generator.json(DataUtils.dataID(name, "firmalife", "plantable"), data.toJson());
    }

    // Why the hell not
    public void addBeneathFertilizer(IngredientJS ingredient, String values) { // "d=0.2, f=0.5"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        generator.json(DataUtils.dataIDFromObject(ingredientJS, "beneath", "nether_fertilizers"), json);
    }

    public void addBeneathFertilizer(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        generator.json(DataUtils.dataID(name, "beneath", "nether_fertilizers"), json);
    }
}
