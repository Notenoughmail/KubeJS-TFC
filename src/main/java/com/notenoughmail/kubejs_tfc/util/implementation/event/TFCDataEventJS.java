package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildClimateRangeData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildDrinkableData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFaunaData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.script.data.VirtualKubeJSDataPack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

// TODO: Ensure builders are up to spec, write JSDocs
@SuppressWarnings("unused")
public class TFCDataEventJS extends DataPackEventJS {

    public TFCDataEventJS(VirtualKubeJSDataPack pack, MultiPackResourceManager manager) {
        super(pack, manager);
    }

    @HideFromJS
    @Override
    public void add(ResourceLocation id, String content) {
        super.add(id, content);
    }

    @HideFromJS
    @Override
    public void addJson(ResourceLocation id, JsonElement json) {
        super.addJson(id, json);
    }

    public void addTFCItemDamageResistance(Ingredient ingredient, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) { // "p=20, c=50"
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_damage_resistances"), json);
    }

    public void addTFCItemDamageResistance(Ingredient ingredient, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataID(name, "tfc", "item_damage_resistances"), json);
    }

    public void addTFCEntityDamageResistance(String entityTag, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) { // "p=20, c=50"
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataIDFromObject(entityTag, "tfc", "entity_damage_resistances"), json);
    }

    public void addTFCEntityDamageResistance(String entityTag, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing, ResourceLocation name) {
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataID(name, "tfc", "entity_damage_resistances"), json);
    }

    public void addTFCDrinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "drinkables"), data.toJson());
    }

    public void addTFCDrinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData, ResourceLocation name) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "drinkables"), data.toJson());
    }

    public void addTFCFertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorous, @Nullable Number potassium) { // "n=0.2, p=0.2, k=0.2"
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleFertilizers(json, nitrogen, phosphorous, potassium);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "fertilizers"), json);
    }

    public void addTFCFertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorous, @Nullable Number potassium, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleFertilizers(json, nitrogen, phosphorous, potassium);
        addJson(DataUtils.dataID(name, "tfc", "fertilizers"), json);
    }

    public void addTFCFoodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "food_items"), data.toJson());
    }

    public void addTFCFoodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData, ResourceLocation name) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "food_items"), data.toJson());
    }

    public void addTFCFuel(Ingredient ingredient, float temperature, int duration) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "fuels"), json);
    }

    public void addTFCFuel(Ingredient ingredient, float temperature, int duration, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(DataUtils.dataID(name, "tfc", "fuels"), json);
    }

    public void addTFCHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature) {
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_heats"), DataUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    public void addTFCHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature, ResourceLocation name) {
        addJson(DataUtils.dataID(name, "tfc", "item_heats"), DataUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    public void addTFCItemSize(Ingredient ingredient, @Nullable Size size, @Nullable Weight weight) { // "s=tiny, weight=medium"
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleItemSize(json, size, weight);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_sizes"), json);
    }

    public void addTFCItemSize(Ingredient ingredient, @Nullable Size size, @Nullable Weight weight, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleItemSize(json, size, weight);
        addJson(DataUtils.dataID(name, "tfc", "item_sizes"), json);
    }

    public void addTFCLampFuel(FluidIngredient fluidIngredient, BlockIngredient blockIngredient, int burnRate) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJson());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "lamp_fuels"), json);
    }

    public void addTFCLampFuel(FluidIngredient fluidIngredient, BlockIngredient blockIngredient, int burnRate, ResourceLocation name) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJson());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataID(name, "tfc", "lamp_fuels"), json);
    }

    public void addTFCMetal(String fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(DataUtils.dataIDFromObject(fluid, "tfc", "metals"), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    public void addTFCMetal(String fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier, ResourceLocation name) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(DataUtils.dataID(name, "tfc", "metals"), json);
    }

    public void addTFCSupport(BlockIngredient blockIngredient, int up, int down, int horizontal) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "supports"), json);
    }

    public void addTFCSupport(BlockIngredient blockIngredient, int up, int down, int horizontal, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataID(name, "tfc", "supports"), json);
    }

    public void addTFCSluicing(Ingredient ingredient, String lootTable) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "sluicing"), json);
    }

    public void addTFCSluicing(Ingredient ingredient, String lootTable, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataID(name, "tfc", "sluicing"), json);
    }

    public void addTFCPanning(BlockIngredient blockIngredient, String lootTable, List<String> models) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "panning"), json);
    }

    public void addTFCPanning(BlockIngredient blockIngredient, String lootTable, List<String> models, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataID(name, "tfc", "panning"), json);
    }

    public void addTFCFauna(Consumer<PlacedFeatureProperties.Climate> climate, Consumer<BuildFaunaData> fauna, ResourceLocation name) {
        var climateObj = new PlacedFeatureProperties.Climate();
        climate.accept(climateObj);
        var faunaObj = new BuildFaunaData(climateObj);
        fauna.accept(faunaObj);
        addJson(DataUtils.dataID(name, "tfc", "fauna"), faunaObj.toJson());
    }

    public void addTFCClimateRange(Consumer<BuildClimateRangeData> climateRange, ResourceLocation name) {
        var climateRageObj = new BuildClimateRangeData();
        climateRange.accept(climateRageObj);
        addJson(DataUtils.dataID(name, "tfc", "climate_ranges"), climateRageObj.toJson());
    }

    /*
     * FirmaLife and Beneath aren't out for 1.20 yet, they may potentially change how they do things
    public void addFLGreenhouse(BlockIngredient blockIngredient, int tier) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "firmalife", "greenhouse"), json);
    }

    public void addFLGreenhouse(BlockIngredient blockIngredient, int tier, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataID(name, "firmalife", "greenhouse"), json);
    }

    public void addFLPlantable(Ingredient ingredient, Consumer<BuildPlantableData> plantableData) {
        var data = new BuildPlantableData(ingredient);
        plantableData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredient, "firmalife", "plantable"), data.toJson());
    }

    public void addFLPlantable(Ingredient ingredient, Consumer<BuildPlantableData> plantableData, ResourceLocation name) {
        var data = new BuildPlantableData(ingredient);
        plantableData.accept(data);
        addJson(DataUtils.dataID(name, "firmalife", "plantable"), data.toJson());
    }

    // Why the hell not
    public void addBeneathFertilizer(Ingredient ingredient, String values) { // "d=0.2, f=0.5"
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataIDFromObject(ingredient, "beneath", "nether_fertilizers"), json);
    }

    public void addBeneathFertilizer(Ingredient ingredient, String values, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataID(name, "beneath", "nether_fertilizers"), json);
    }
    */
}
