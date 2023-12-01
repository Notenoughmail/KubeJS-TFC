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
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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
        // KubeJSTFC.LOGGER.info(id.toString() + " | " + json.toString());
        super.addJson(id, json);
    }

    @Info(value = "Adds an item damage resistance to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient this resistance applies to"),
            @Param(name = "piercing", value = "The piercing value of this resistance, may be null to not specify a value"),
            @Param(name = "slashing", value = "The slashing value of this resistance, may be null to not specify a value"),
            @Param(name = "crushing", value = "the crushing value of this resistance, may be null to not specify a value")
    })
    public void itemDamageResistance(Ingredient ingredient, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) { // "p=20, c=50"
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_damage_resistances"), json);
    }

    @Info(value = "Adds an item damage resistance to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient this resistance applies to"),
            @Param(name = "piercing", value = "The piercing value of this resistance, may be null to not specify a value"),
            @Param(name = "slashing", value = "The slashing value of this resistance, may be null to not specify a value"),
            @Param(name = "crushing", value = "the crushing value of this resistance, may be null to not specify a value"),
            @Param(name = "name", value = "The name of the damage resistance")
    })
    public void itemDamageResistance(Ingredient ingredient, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataID(name, "tfc", "item_damage_resistances"), json);
    }

    @Info(value = "Adds an entity damage resistance to the specified entity tag", params = {
            @Param(name = "entityTag", value = "The entity tag to apply the damage resistances to"),
            @Param(name = "piercing", value = "The piercing value of this resistance, may be null to not specify a value"),
            @Param(name = "slashing", value = "The slashing value of this resistance, may be null to not specify a value"),
            @Param(name = "crushing", value = "the crushing value of this resistance, may be null to not specify a value")
    })
    public void entityDamageResistance(String entityTag, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) {
        final JsonObject json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataIDFromObject(entityTag, "tfc", "entity_damage_resistances"), json);
    }
    @Info(value = "Adds an entity damage resistance to the specified entity tag", params = {
            @Param(name = "entityTag", value = "The entity tag to apply the damage resistances to"),
            @Param(name = "piercing", value = "The piercing value of this resistance, may be null to not specify a value"),
            @Param(name = "slashing", value = "The slashing value of this resistance, may be null to not specify a value"),
            @Param(name = "crushing", value = "the crushing value of this resistance, may be null to not specify a value"),
            @Param(name = "name", value = "The name of the damage resistance")
    })
    public void entityDamageResistance(String entityTag, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.addProperty("entity", entityTag);
        DataUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(DataUtils.dataID(name, "tfc", "entity_damage_resistances"), json);
    }

    @Info(value = "Defines that a fluid is directly drinkable", params = {
            @Param(name = "fluidIngredient", value = "The fluids this drinkable applies to"),
            @Param(name = "drinkableData", value = "The drinkable properties that are applied to the fluid ingredient")
    })
    @Generics(value = BuildDrinkableData.class)
    public void drinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "drinkables"), data.toJson());
    }

    @Info(value = "Defines that a fluid is directly drinkable", params = {
            @Param(name = "fluidIngredient", value = "The fluids this drinkable applies to"),
            @Param(name = "drinkableData", value = "The drinkable properties that are applied to the fluid ingredient"),
            @Param(name = "name", value = "The name of the drinkable data")
    })
    @Generics(value = BuildDrinkableData.class)
    public void drinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData, ResourceLocation name) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "drinkables"), data.toJson());
    }

    @Info(value = "Adds a fertilizer definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fertilizer data applies to"),
            @Param(name = "nitrogen", value = "The nitrogen value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "phosphorous", value = "The phosphorous value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "potassium", value = "The potassium value of the fertilizer, amy be null to not define a value, defaults to 0")
    })
    public void fertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorous, @Nullable Number potassium) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleFertilizers(json, nitrogen, phosphorous, potassium);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "fertilizers"), json);
    }

    @Info(value = "Adds a fertilizer definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fertilizer data applies to"),
            @Param(name = "nitrogen", value = "The nitrogen value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "phosphorous", value = "The phosphorous value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "potassium", value = "The potassium value of the fertilizer, amy be null to not define a value, defaults to 0"),
            @Param(name = "name", value = "The name of the fertilizer data")
    })
    public void fertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorous, @Nullable Number potassium, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleFertilizers(json, nitrogen, phosphorous, potassium);
        addJson(DataUtils.dataID(name, "tfc", "fertilizers"), json);
    }

    @Info(value = "Adds a food definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the food definition applies to"),
            @Param(name = "foodItemData", value = "The food item properties that are applied to the ingredient")
    })
    @Generics(value = BuildFoodItemData.class)
    public void foodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "food_items"), data.toJson());
    }

    @Info(value = "Adds a food definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the food definition applies to"),
            @Param(name = "foodItemData", value = "The food item properties that are applied to the ingredient"),
            @Param(name = "name", value = "The name of the food item data")
    })
    @Generics(value = BuildFoodItemData.class)
    public void foodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData, ResourceLocation name) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(DataUtils.dataID(name, "tfc", "food_items"), data.toJson());
    }

    @Info(value = "Adds a fuel definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fuel definition applies to"),
            @Param(name = "temperature", value = "The temperature °C that the fuel burns at"),
            @Param(name = "duration", value = "The number of ticks the fuel burns for"),
            @Param(name = "purity", value = "The purity of the fuel, may be null to not specify a value")
    })
    public void fuel(Ingredient ingredient, float temperature, int duration, @Nullable Float purity) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        if (purity != null) {
            json.addProperty("purity", purity);
        }
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "fuels"), json);
    }

    @Info(value = "Adds a fuel definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fuel definition applies to"),
            @Param(name = "temperature", value = "The temperature °C that the fuel burns at"),
            @Param(name = "duration", value = "The number of ticks the fuel burns for"),
            @Param(name = "purity", value = "The purity of the fuel, may be null to not specify a value"),
            @Param(name = "name", value = "The name of the fuel definition")
    })
    public void fuel(Ingredient ingredient, float temperature, int duration, @Nullable Float purity, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        if (purity != null) {
            json.addProperty("purity", purity);
        }
        addJson(DataUtils.dataID(name, "tfc", "fuels"), json);
    }

    @Info(value = "Adds a heat definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the heat definition applies to"),
            @Param(name = "heatCapacity", value = "Specifies how fast the ingredient heats up relative to others. Measured in Energy / °C"),
            @Param(name = "forgingTemperature", value = "Specifies the temperature °C required to work the ingredient. May be null to allow working at any temperature"),
            @Param(name = "weldingTemperature", value = "Specifies the temperature °C required to weld the ingredient. May be null to allow welding at any temperature")
    })
    public void itemHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature) {
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_heats"), DataUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @Info(value = "Adds a heat definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the heat definition applies to"),
            @Param(name = "heatCapacity", value = "Specifies how fast the ingredient heats up relative to others. Measured in Energy / °C"),
            @Param(name = "forgingTemperature", value = "Specifies the temperature °C required to work the ingredient. May be null to allow working at any temperature"),
            @Param(name = "weldingTemperature", value = "Specifies the temperature °C required to weld the ingredient. May be null to allow welding at any temperature"),
            @Param(name = "name", value = "The name of the heat definition")
    })
    public void itemHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature, ResourceLocation name) {
        addJson(DataUtils.dataID(name, "tfc", "item_heats"), DataUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @Info(value = "Adds an item size definition tot he specified ingredient", params ={
            @Param(name = "ingredient", value = "The ingredient this item size definition applies to"),
            @Param(name = "size", value = "Sets the size of the definition, may be 'tiny', 'very_small', 'small', 'normal', 'large', 'very_large', 'huge', or null to not specify a size"),
            @Param(name = "weight", value = "Sets the weight of the definition, may be 'very_light', 'light', 'medium', 'heavy', 'very_heavy', or null to not specify a weight")
    })
    public void itemSize(Ingredient ingredient, @Nullable Size size, @Nullable Weight weight) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleItemSize(json, size, weight);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "item_sizes"), json);
    }

    @Info(value = "Adds an item size definition to the specified ingredient", params ={
            @Param(name = "ingredient", value = "The ingredient this item size definition applies to"),
            @Param(name = "size", value = "Sets the size of the definition, may be 'tiny', 'very_small', 'small', 'normal', 'large', 'very_large', 'huge', or null to default to 'medium'"),
            @Param(name = "weight", value = "Sets the weight of the definition, may be 'very_light', 'light', 'medium', 'heavy', 'very_heavy', or null to default to 'medium'"),
            @Param(name = "name", value = "The name of the item size definition")
    })
    public void itemSize(Ingredient ingredient, @Nullable Size size, @Nullable Weight weight, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleItemSize(json, size, weight);
        addJson(DataUtils.dataID(name, "tfc", "item_sizes"), json);
    }

    @Info(value = "Defines a knapping type", params = {
            @Param(name = "ingredient", value = "The ingredient of the knapping type's item stack ingredient"),
            @Param(name = "ingredientCount", value = "The count of the knapping type's item stack ingredient"),
            @Param(name = "amountToConsume", value = "The amount of items that get used by the recipe"),
            @Param(name = "clickSound", value = "The registry name of a sound that plays when knapping occurs"),
            @Param(name = "consumeAfterComplete", value = "If items should be consumed as soon as a square is clicked, or when the result is removed from the slot"),
            @Param(name = "useDisabledTexture", value = "If true, a clicked slot will show a different texture rather than nothing at all"),
            @Param(name = "spawnsParticles", value = "if true, the screen will show particles when knapping"),
            @Param(name = "jeiIconItem", value = "An item stack, used as the category icon in the auto-generated jei category"),
            @Param(name = "name", value = "The name of the knapping type")
    })
    public void knappingType(Ingredient ingredient, int ingredientCount, int amountToConsume, ResourceLocation clickSound, boolean consumeAfterComplete, boolean useDisabledTexture, boolean spawnsParticles, ItemStack jeiIconItem, ResourceLocation name) {
        final JsonObject json = DataUtils.knappingType(ingredient, ingredientCount, amountToConsume, clickSound, consumeAfterComplete, useDisabledTexture, spawnsParticles, jeiIconItem);
        addJson(DataUtils.dataID(name, "tfc", "knapping_types"), json);
    }

    @Info(value = "Defines a lamp fuel", params = {
            @Param(name = "fluidIngredient", value = "The fluid ingredient which determines which fluids the the lamp fuel applies to"),
            @Param(name = "blockIngredient", value = "The block ingredient which determines what (lamp) blocks are valid for this fuel"),
            @Param(name = "burnRate", value = "How fast the lamp consumes fuel, in ticks per mB")
    })
    public void lampFuel(FluidIngredient fluidIngredient, BlockIngredient blockIngredient, int burnRate) {
        final JsonObject json = new JsonObject();
        json.add("fluid", fluidIngredient.toJson());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataIDFromObject(fluidIngredient, "tfc", "lamp_fuels"), json);
    }

    @Info(value = "Defines a lamp fuel", params = {
            @Param(name = "fluidIngredient", value = "The fluid ingredient which determines which fluids the the lamp fuel applies to"),
            @Param(name = "blockIngredient", value = "The block ingredient which determines what (lamp) blocks are valid for this fuel"),
            @Param(name = "burnRate", value = "How fast the lamp consumes fuel, in ticks per mB"),
            @Param(name = "name", value = "The name of the lamp fuel")
    })
    public void lampFuel(FluidIngredient fluidIngredient, BlockIngredient blockIngredient, int burnRate, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("fluid", fluidIngredient.toJson());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(DataUtils.dataID(name, "tfc", "lamp_fuels"), json);
    }

    @Info(value = "Defines a metal", params = {
            @Param(name = "fluid", value = "The registry name of the fluid which corresponds to the metal"),
            @Param(name = "meltTemperature", value = "The melting temperature °C of the metal"),
            @Param(name = "heatCapacity", value = "Specifies how fast the metal heats up relative to others. Measured in Energy / (mB x °C)"),
            @Param(name = "ingot", value = "The ingredient which specifies the metal's ingots, may be null"),
            @Param(name = "doubleIngot", value = "The ingredient which specifies the metal's double ingots, may be null"),
            @Param(name = "sheet", value = "The ingredient which specifies the metal's sheets, may be null"),
            @Param(name = "tier", value = "The tier of the metal")
    })
    public void metal(String fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier) {
        final JsonObject json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(DataUtils.dataIDFromObject(fluid, "tfc", "metals"), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    @Info(value = "Defines a metal", params = {
            @Param(name = "fluid", value = "The registry name of the fluid which corresponds to the metal"),
            @Param(name = "meltTemperature", value = "The melting temperature °C of the metal"),
            @Param(name = "heatCapacity", value = "Specifies how fast the metal heats up relative to others. Measured in Energy / (mB x °C)"),
            @Param(name = "ingot", value = "The ingredient which specifies the metal's ingots, may be null"),
            @Param(name = "doubleIngot", value = "The ingredient which specifies the metal's double ingots, may be null"),
            @Param(name = "sheet", value = "The ingredient which specifies the metal's sheets, may be null"),
            @Param(name = "tier", value = "The tier of the metal"),
            @Param(name = "name", value = "The name of the metal")
    })
    public void metal(String fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier, ResourceLocation name) {
        final JsonObject json = DataUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(DataUtils.dataID(name, "tfc", "metals"), json);
    }

    @Info(value = "Defines a support definition", params = {
            @Param(name = "blockIngredient", value = "The block ingredient that defines what blocks the definition applies to"),
            @Param(name = "up", value = "The number of blocks upwards the block supports"),
            @Param(name = "down", value = "The number og blocks downwards the block supports"),
            @Param(name = "horizontal", value = "The number of blocks horizontally the block supports")
    })
    public void support(BlockIngredient blockIngredient, int up, int down, int horizontal) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "supports"), json);
    }

    @Info(value = "Defines a support definition", params = {
            @Param(name = "blockIngredient", value = "The block ingredient that defines what blocks the definition applies to"),
            @Param(name = "up", value = "The number of blocks upwards the block supports"),
            @Param(name = "down", value = "The number og blocks downwards the block supports"),
            @Param(name = "horizontal", value = "The number of blocks horizontally the block supports"),
            @Param(name = "name", value = "The name of the support definition")
    })
    public void support(BlockIngredient blockIngredient, int up, int down, int horizontal, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(DataUtils.dataID(name, "tfc", "supports"), json);
    }

    @Info(value = "Adds a sluicing definition to the ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the sluicing definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped for this item")
    })
    public void sluicing(Ingredient ingredient, String lootTable) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataIDFromObject(ingredient, "tfc", "sluicing"), json);
    }

    @Info(value = "Adds a sluicing definition to the ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the sluicing definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped for this item"),
            @Param(name = "name", value = "The name of the sluicing definition")
    })
    public void sluicing(Ingredient ingredient, String lootTable, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(DataUtils.dataID(name, "tfc", "sluicing"), json);
    }

    @Info(value = "Adds a panning definition to the block ingredient", params = {
            @Param(name = "blockIngredient", value = "The block ingredient the definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped by the ingredient"),
            @Param(name = "models", value = "A list of model locations to be iterated through as panning progresses")
    })
    @Generics(value = String.class)
    public void panning(BlockIngredient blockIngredient, String lootTable, List<String> models) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        final JsonArray array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "tfc", "panning"), json);
    }

    @Info(value = "Adds a panning definition to the block ingredient", params = {
            @Param(name = "blockIngredient", value = "The block ingredient the definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped by the ingredient"),
            @Param(name = "models", value = "A list of model locations to be iterated through as panning progresses"),
            @Param(name = "name", value = "The name of the panning definition")
    })
    @Generics(value = String.class)
    public void panning(BlockIngredient blockIngredient, String lootTable, List<String> models, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        final JsonArray array = new JsonArray();
        models.forEach(array::add);
        json.add("model_stages", array);
        addJson(DataUtils.dataID(name, "tfc", "panning"), json);
    }

    @Info(value = "Specifies the fauna data of the given name", params = {
            @Param(name = "climate", value = "The fauna's climate requirements"),
            @Param(name = "fauna", value = "Additional fauna properties for the given fauna"),
            @Param(name = "name", value = "The name of the fauna definition")
    })
    @Generics(value = {PlacedFeatureProperties.Climate.class, BuildFaunaData.class})
    public void fauna(Consumer<PlacedFeatureProperties.Climate> climate, Consumer<BuildFaunaData> fauna, ResourceLocation name) {
        var climateObj = new PlacedFeatureProperties.Climate();
        climate.accept(climateObj);
        var faunaObj = new BuildFaunaData(climateObj);
        fauna.accept(faunaObj);
        addJson(DataUtils.dataID(name, "tfc", "fauna"), faunaObj.toJson());
    }

    @Info(value = "Specifies the climate range data of the given name", params = {
            @Param(name = "climateRange", value = "Climate range properties for the given climate range"),
            @Param(name = "name", value = "The name of the climate range")
    })
    @Generics(value = BuildClimateRangeData.class)
    public void climateRange(Consumer<BuildClimateRangeData> climateRange, ResourceLocation name) {
        var climateRageObj = new BuildClimateRangeData();
        climateRange.accept(climateRageObj);
        addJson(DataUtils.dataID(name, "tfc", "climate_ranges"), climateRageObj.toJson());
    }

    /*
     * FirmaLife and Beneath aren't out for 1.20 yet, they may potentially change how they do things
    public void greenhouse(BlockIngredient blockIngredient, int tier) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataIDFromObject(blockIngredient, "firmalife", "greenhouse"), json);
    }

    public void greenhouse(BlockIngredient blockIngredient, int tier, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataID(name, "firmalife", "greenhouse"), json);
    }

    public void plantable(Ingredient ingredient, Consumer<BuildPlantableData> plantableData) {
        var data = new BuildPlantableData(ingredient);
        plantableData.accept(data);
        addJson(DataUtils.dataIDFromObject(ingredient, "firmalife", "plantable"), data.toJson());
    }

    public void plantable(Ingredient ingredient, Consumer<BuildPlantableData> plantableData, ResourceLocation name) {
        var data = new BuildPlantableData(ingredient);
        plantableData.accept(data);
        addJson(DataUtils.dataID(name, "firmalife", "plantable"), data.toJson());
    }

    // Why the hell not
    public void netherFertilizer(Ingredient ingredient, String values) { // "d=0.2, f=0.5"
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataIDFromObject(ingredient, "beneath", "nether_fertilizers"), json);
    }

    public void netherFertilizer(Ingredient ingredient, String values, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        DataUtils.handleNetherFertilizers(values, json);
        addJson(DataUtils.dataID(name, "beneath", "nether_fertilizers"), json);
    }
    */
}
