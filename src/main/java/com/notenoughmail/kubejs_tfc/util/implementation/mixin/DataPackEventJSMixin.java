package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.data.*;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.*;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@Mixin(value = DataPackEventJS.class, remap = false)
public abstract class DataPackEventJSMixin {

    @Shadow(remap = false)
    public abstract void addJson(ResourceLocation resourceLocation, JsonElement json);

    @RemapForJS("addTFCItemDamageResistance")
    @Unique
    public void kubeJS_TFC$ItemDamageResistance(IngredientJS ingredient, String values) { // "p=20, c=50"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleResistances(values, json);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "item_damage_resistances"), json);
    }

    @RemapForJS("addTFCItemDamageResistance")
    @Unique
    public void kubeJS_TFC$ItemDamageResistance(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleResistances(values, json);
        addJson(DataUtils.dataID(name, "tfc", "item_damage_resistances"), json);
    }

    @RemapForJS("addTFCEntityDamageResistance")
    @Unique
    public void kubeJS_TFC$EntityDamageResistance(String entityTag, String values) { // "p=20, c=50"
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(values, json);
        addJson(DataUtils.dataIDFromObject(entityTag, "tfc", "entity_damage_resistances"), json);
    }

    @RemapForJS("addTFCEntityDamageResistance")
    @Unique
    public void kubeJS_TFC$EntityDamageResistance(String entityTag, String values, ResourceLocation name) {
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(values, json);
        addJson(DataUtils.dataID(name, "tfc", "entity_damage_resistances"), json);
    }

    @RemapForJS("addTFCDrinkable")
    @Unique
    public void kubeJS_TFC$Drinkable(FluidStackIngredientJS fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "drinkables"), data.toJson());
    }

    @RemapForJS("addTFCDrinkable")
    @Unique
    public void kubeJS_TFC$Drinkable(FluidStackIngredientJS fluidIngredient, Consumer<BuildDrinkableData> drinkableData, ResourceLocation name) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "drinkables"), data.toJson());
    }

    @RemapForJS("addTFCFertilizer")
    @Unique
    public void kubeJS_TFC$Fertilizer(IngredientJS ingredient, String values) { // "n=0.2, p=0.2, k=0.2"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleFertilizers(values, json);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "fertilizers"), json);
    }

    @RemapForJS("addTFCFertilizer")
    @Unique
    public void kubeJS_TFC$Fertilizer(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleFertilizers(values, json);
        addJson(DataUtils.dataID(name, "tfc", "fertilizers"), json);
    }

    @RemapForJS("addTFCFoodItem")
    @Unique
    public void kubeJS_TFC$FoodItem(IngredientJS ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildFoodItemData(ingredientJS);
        foodItemData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "food_items"), data.toJson());
    }

    @RemapForJS("addTFCFoodItem")
    @Unique
    public void kubeJS_TFC$FoodItem(IngredientJS ingredient, Consumer<BuildFoodItemData> foodItemData, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildFoodItemData(ingredientJS);
        foodItemData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "food_items"), data.toJson());
    }

    @RemapForJS("addTFCFuel")
    @Unique
    public void kubeJS_TFC$Fuel(IngredientJS ingredient, float temperature, int duration) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "fuels"), json);
    }

    @RemapForJS("addTFCFuel")
    @Unique
    public void kubeJS_TFC$Fuel(IngredientJS ingredient, float temperature, int duration, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(DataUtils.dataID(name, "tfc", "fuels"), json);
    }

    @RemapForJS("addTFCHeat")
    @Unique
    public void kubeJS_TFC$Heat(IngredientJS ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "item_heats"), DataUtils.buildHeat(ingredientJS, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @RemapForJS("addTFCHeat")
    @Unique
    public void kubeJS_TFC$Heat(IngredientJS ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        addJson(DataUtils.dataID(name, "tfc", "item_heats"), DataUtils.buildHeat(ingredientJS, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @RemapForJS("addTFCItemSize")
    @Unique
    public void kubeJS_TFC$ItemSize(IngredientJS ingredient, String values) { // "s=tiny, weight=medium"
        var ingredientjS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientjS.toJson());
        DataUtils.handleItemSize(values, json);
        addJson(DataUtils.dataIDFromObject(ingredientjS, "tfc", "item_sizes"), json);
    }

    @RemapForJS("addTFCItemSize")
    @Unique
    public void kubeJS_TFC$ItemSize(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleItemSize(values, json);
        addJson(DataUtils.dataID(name, "tfc", "item_sizes"), json);
    }

    @RemapForJS("addTFCLampFuel")
    @Unique
    public void kubeJS_TFC$LampFuel(FluidStackIngredientJS fluidIngredient, BlockIngredientJS blockIngredient, int burnRate) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJsonNoAmount());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "lamp_fuels"), json);
    }

    @RemapForJS("addTFCLampFuel")
    @Unique
    public void kubeJS_TFC$LampFuel(FluidStackIngredientJS fluidIngredient, BlockIngredientJS blockIngredient, int burnRate, ResourceLocation name) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJsonNoAmount());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataID(name, "tfc", "lamp_fuels"), json);
    }

    @RemapForJS("addTFCMetal")
    @Unique
    public void kubeJS_TFC$Metal(String fluid, float meltTemperature, float heatCapacity, @Nullable IngredientJS ingot, @Nullable IngredientJS sheet, int tier) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, sheet, tier);
        addJson(DataUtils.dataIDFromObject(fluid, "tfc", "metals"), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    @RemapForJS("addTFCMetal")
    @Unique
    public void kubeJS_TFC$Metal(String fluid, float meltTemperature, float heatCapacity, @Nullable IngredientJS ingot, @Nullable IngredientJS sheet, int tier, ResourceLocation name) {
        var json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, sheet, tier);
        addJson(DataUtils.dataID(name, "tfc", "metals"), json);
    }

    @RemapForJS("addTFCSupport")
    @Unique
    public void kubeJS_TFC$Support(BlockIngredientJS blockIngredient, int up, int down, int horizontal) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "supports"), json);
    }

    @RemapForJS("addTFCSupport")
    @Unique
    public void kubeJS_TFC$Support(BlockIngredientJS blockIngredient, int up, int down, int horizontal, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataID(name, "tfc", "supports"), json);
    }

    @RemapForJS("addTFCSluicing")
    @Unique
    public void kubeJS_TFC$Sluicing(IngredientJS ingredient, String lootTable) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "tfc", "sluicing"), json);
    }

    @RemapForJS("addTFCSluicing")
    @Unique
    public void kubeJS_TFC$Sluicing(IngredientJS ingredient, String lootTable, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataID(name, "tfc", "sluicing"), json);
    }

    @RemapForJS("addTFCPanning")
    @Unique
    public void kubeJS_TFC$Panning(BlockIngredientJS blockIngredient, String lootTable, List<String> models) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "panning"), json);
    }

    @RemapForJS("addTFCPanning")
    @Unique
    public void kubeJS_TFC$Panning(BlockIngredientJS blockIngredient, String lootTable, List<String> models, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataID(name, "tfc", "panning"), json);
    }

    @RemapForJS("addTFCFauna")
    @Unique
    public void kubeJS_TFC$Fauna(Consumer<PlacedFeatureProperties.Climate> climate, Consumer<BuildFaunaData> fauna, ResourceLocation name) {
        var climateObj = new PlacedFeatureProperties.Climate();
        climate.accept(climateObj);
        var faunaObj = new BuildFaunaData(climateObj);
        fauna.accept(faunaObj);
        addJson(DataUtils.dataID(name, "tfc", "fauna"), faunaObj.toJson());
    }

    @RemapForJS("addTFCClimateRange")
    @Unique
    public void kubeJS_TFC$ClimateRange(Consumer<BuildClimateRangeData> climateRange, ResourceLocation name) {
        var climateRageObj = new BuildClimateRangeData();
        climateRange.accept(climateRageObj);
        addJson(DataUtils.dataID(name, "tfc", "climate_ranges"), climateRageObj.toJson());
    }

    @RemapForJS("addFLGreenhouse")
    @Unique
    public void kubeJS_TFC$Greenhouse(BlockIngredientJS blockIngredient, int tier) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "firmalife", "greenhouse"), json);
    }

    @RemapForJS("addFLGreenhouse")
    @Unique
    public void kubeJS_TFC$Greenhouse(BlockIngredientJS blockIngredient, int tier, ResourceLocation name) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataID(name, "firmalife", "greenhouse"), json);
    }

    @RemapForJS("addFLPlantable")
    @Unique
    public void kubeJS_TFC$Plantable(IngredientJS ingredient, Consumer<BuildPlantableData> plantableData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildPlantableData(ingredientJS);
        plantableData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "firmalife", "plantable"), data.toJson());
    }

    @RemapForJS("addFLPlantable")
    @Unique
    public void kubeJS_TFC$Plantable(IngredientJS ingredient, Consumer<BuildPlantableData> plantableData, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildPlantableData(ingredientJS);
        plantableData.accept(data);
        addJson(DataUtils.dataID(name, "firmalife", "plantable"), data.toJson());
    }

    // Why the hell not
    @RemapForJS("addBeneathFertilizer")
    @Unique
    public void kubeJS_TFC$NetherFertilizer(IngredientJS ingredient, String values) { // "d=0.2, f=0.5"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataIDFromObject(ingredientJS, "beneath", "nether_fertilizers"), json);
    }

    @RemapForJS("addBeneathFertilizer")
    @Unique
    public void kubeJS_TFC$NetherFertilizer(IngredientJS ingredient, String values, ResourceLocation name) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataID(name, "beneath", "nether_fertilizers"), json);
    }

    //================================WORLDGEN================================ (misery)

    @RemapForJS("buildTFCGeode")
    @Unique
    public void kubeJS_TFC$buildTFCGeode(String name, Consumer<BuildGeodeProperties> geode, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildGeodeProperties();
        geode.accept(properties);
        addJson(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCBoulder")
    @Unique
    public void kubeJS_TFC$buildTFCBoulder(String name, Consumer<BuildBoulderProperties> boulder, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildBoulderProperties();
        boulder.accept(properties);
        addJson(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCThinSpike")
    @Unique
    public void kubeJS_TFC$buildTFCThinSpike(String name, Consumer<BuildThinSpikeProperties> spike, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildThinSpikeProperties();
        spike.accept(properties);
        addJson(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCVein")
    @Unique
    public void kubeJS_TFC$buildTFCVein(String name, Consumer<BuildVeinProperties> vein) {
        var properties = new BuildVeinProperties(name);
        vein.accept(properties);
        addJson(DataUtils.configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCIfThen")
    @Unique
    public void kubeJS_TFC$buildTFCIfThen(String name, String if_, String then, Consumer<PlacedFeatureProperties> placement) {
        var json = new JsonObject();
        json.addProperty("type", "tfc:if_then");
        var config = new JsonObject();
        config.addProperty("if", if_);
        config.addProperty("then", then);
        json.add("config", config);
        addJson(DataUtils.configuredFeatureName(name), json);
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(DataUtils.placedFeatureName(name), place.toJson());
    }
}
