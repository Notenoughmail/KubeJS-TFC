package com.notenoughmail.kubejs_tfc.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IDataConstructor;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildClimateRangeData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildDrinkableData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFaunaData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.PlacedFeatureProperties;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class TFCDataEventJS extends EventJS implements IDataConstructor {

    private final DataPackEventJS wrappedEvent;

    public TFCDataEventJS(DataPackEventJS wrapped) {
        wrappedEvent = wrapped;
    }

    @Override
    public void addJson(ResourceLocation id, JsonElement json) {
        KubeJSTFC.warningLog(id.toString());
        KubeJSTFC.infoLog(json.toString());
        wrappedEvent.addJson(id, json);
    }

    @Info(value = "Adds an item damage resistance to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient this resistance applies to"),
            @Param(name = "piercing", value = "The piercing value of this resistance, may be null to not specify a value"),
            @Param(name = "slashing", value = "The slashing value of this resistance, may be null to not specify a value"),
            @Param(name = "crushing", value = "the crushing value of this resistance, may be null to not specify a value")
    })
    public void itemDamageResistance(Ingredient ingredient, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "item_damage_resistances"), json);
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
        ResourceUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(ResourceUtils.dataID(name, "tfc", "item_damage_resistances"), json);
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
        ResourceUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(ResourceUtils.dataIDFromObject(entityTag, "tfc", "entity_damage_resistances"), json);
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
        ResourceUtils.handleResistances(json, piercing, slashing, crushing);
        addJson(ResourceUtils.dataID(name, "tfc", "entity_damage_resistances"), json);
    }

    @Info(value = "Defines that a fluid is directly drinkable", params = {
            @Param(name = "fluidIngredient", value = "The fluids this drinkable applies to"),
            @Param(name = "drinkableData", value = "The drinkable properties that are applied to the fluid ingredient")
    })
    @Generics(BuildDrinkableData.class)
    public void drinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(ResourceUtils.dataIDFromObject(fluidIngredient, "tfc", "drinkables"), data.toJson());
    }

    @Info(value = "Defines that a fluid is directly drinkable", params = {
            @Param(name = "fluidIngredient", value = "The fluids this drinkable applies to"),
            @Param(name = "drinkableData", value = "The drinkable properties that are applied to the fluid ingredient"),
            @Param(name = "name", value = "The name of the drinkable data")
    })
    @Generics(BuildDrinkableData.class)
    public void drinkable(FluidIngredient fluidIngredient, Consumer<BuildDrinkableData> drinkableData, ResourceLocation name) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(ResourceUtils.dataID(name, "tfc", "drinkables"), data.toJson());
    }

    @Info(value = "Adds a fertilizer definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fertilizer data applies to"),
            @Param(name = "nitrogen", value = "The nitrogen value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "phosphorous", value = "The phosphorous value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "potassium", value = "The potassium value of the fertilizer, amy be null to not define a value, defaults to 0")
    })
    public void fertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorus, @Nullable Number potassium) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleFertilizers(json, nitrogen, phosphorus, potassium);
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "fertilizers"), json);
    }

    @Info(value = "Adds a fertilizer definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the fertilizer data applies to"),
            @Param(name = "nitrogen", value = "The nitrogen value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "phosphorous", value = "The phosphorous value of the fertilizer, may be null to not define a value, defaults to 0"),
            @Param(name = "potassium", value = "The potassium value of the fertilizer, amy be null to not define a value, defaults to 0"),
            @Param(name = "name", value = "The name of the fertilizer data")
    })
    public void fertilizer(Ingredient ingredient, @Nullable Number nitrogen, @Nullable Number phosphorus, @Nullable Number potassium, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleFertilizers(json, nitrogen, phosphorus, potassium);
        addJson(ResourceUtils.dataID(name, "tfc", "fertilizers"), json);
    }

    @Info(value = "Adds a food definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the food definition applies to"),
            @Param(name = "foodItemData", value = "The food item properties that are applied to the ingredient")
    })
    @Generics(BuildFoodItemData.class)
    public void foodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "food_items"), data.toJson());
    }

    @Info(value = "Adds a food definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the food definition applies to"),
            @Param(name = "foodItemData", value = "The food item properties that are applied to the ingredient"),
            @Param(name = "name", value = "The name of the food item data")
    })
    @Generics(BuildFoodItemData.class)
    public void foodItem(Ingredient ingredient, Consumer<BuildFoodItemData> foodItemData, ResourceLocation name) {
        var data = new BuildFoodItemData(ingredient);
        foodItemData.accept(data);
        addJson(ResourceUtils.dataID(name, "tfc", "food_items"), data.toJson());
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
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "fuels"), json);
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
        addJson(ResourceUtils.dataID(name, "tfc", "fuels"), json);
    }

    @Info(value = "Adds a heat definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the heat definition applies to"),
            @Param(name = "heatCapacity", value = "Specifies how fast the ingredient heats up relative to others. Measured in Energy / °C"),
            @Param(name = "forgingTemperature", value = "Specifies the temperature °C required to work the ingredient. May be null to allow working at any temperature"),
            @Param(name = "weldingTemperature", value = "Specifies the temperature °C required to weld the ingredient. May be null to allow welding at any temperature")
    })
    public void itemHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature) {
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "item_heats"), ResourceUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @Info(value = "Adds a heat definition to the specified ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the heat definition applies to"),
            @Param(name = "heatCapacity", value = "Specifies how fast the ingredient heats up relative to others. Measured in Energy / °C"),
            @Param(name = "forgingTemperature", value = "Specifies the temperature °C required to work the ingredient. May be null to allow working at any temperature"),
            @Param(name = "weldingTemperature", value = "Specifies the temperature °C required to weld the ingredient. May be null to allow welding at any temperature"),
            @Param(name = "name", value = "The name of the heat definition")
    })
    public void itemHeat(Ingredient ingredient, float heatCapacity, @Nullable Float forgingTemperature, @Nullable Float weldingTemperature, ResourceLocation name) {
        addJson(ResourceUtils.dataID(name, "tfc", "item_heats"), ResourceUtils.buildHeat(ingredient, heatCapacity, forgingTemperature, weldingTemperature));
    }

    @Info(value = "Adds an item size definition tot he specified ingredient", params ={
            @Param(name = "ingredient", value = "The ingredient this item size definition applies to"),
            @Param(name = "size", value = "Sets the size of the definition, may be 'tiny', 'very_small', 'small', 'normal', 'large', 'very_large', 'huge', or null to not specify a size"),
            @Param(name = "weight", value = "Sets the weight of the definition, may be 'very_light', 'light', 'medium', 'heavy', 'very_heavy', or null to not specify a weight")
    })
    public void itemSize(Ingredient ingredient, @Nullable Size size, @Nullable Weight weight) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        ResourceUtils.handleItemSize(json, size, weight);
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "item_sizes"), json);
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
        ResourceUtils.handleItemSize(json, size, weight);
        addJson(ResourceUtils.dataID(name, "tfc", "item_sizes"), json);
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
        final JsonObject json = ResourceUtils.knappingType(ingredient, ingredientCount, amountToConsume, clickSound, consumeAfterComplete, useDisabledTexture, spawnsParticles, jeiIconItem);
        addJson(ResourceUtils.dataID(name, "tfc", "knapping_types"), json);
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
        addJson(ResourceUtils.dataIDFromObject(fluidIngredient, "tfc", "lamp_fuels"), json);
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
        addJson(ResourceUtils.dataID(name, "tfc", "lamp_fuels"), json);
    }

    @Info(value = "Defines a metal", params = {
            @Param(name = "fluid", value = "The fluid the metal is based on"),
            @Param(name = "meltTemperature", value = "The melting temperature °C of the metal"),
            @Param(name = "heatCapacity", value = "Specifies how fast the metal heats up relative to others. Measured in Energy / (mB x °C)"),
            @Param(name = "ingot", value = "The ingredient which specifies the metal's ingots, may be null"),
            @Param(name = "doubleIngot", value = "The ingredient which specifies the metal's double ingots, may be null"),
            @Param(name = "sheet", value = "The ingredient which specifies the metal's sheets, may be null"),
            @Param(name = "tier", value = "The tier of the metal")
    })
    public void metal(Fluid fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier) {
        final JsonObject json = ResourceUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(ResourceUtils.dataIDFromObject(fluid, "tfc", "metals"), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    @Info(value = "Defines a metal", params = {
            @Param(name = "fluid", value = "The fluid the metal is based on"),
            @Param(name = "meltTemperature", value = "The melting temperature °C of the metal"),
            @Param(name = "heatCapacity", value = "Specifies how fast the metal heats up relative to others. Measured in Energy / (mB x °C)"),
            @Param(name = "ingot", value = "The ingredient which specifies the metal's ingots, may be null"),
            @Param(name = "doubleIngot", value = "The ingredient which specifies the metal's double ingots, may be null"),
            @Param(name = "sheet", value = "The ingredient which specifies the metal's sheets, may be null"),
            @Param(name = "tier", value = "The tier of the metal"),
            @Param(name = "name", value = "The name of the metal")
    })
    public void metal(Fluid fluid, float meltTemperature, float heatCapacity, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier, ResourceLocation name) {
        final JsonObject json = ResourceUtils.makeMetal(fluid, meltTemperature, heatCapacity, ingot, doubleIngot, sheet, tier);
        addJson(ResourceUtils.dataID(name, "tfc", "metals"), json);
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
        addJson(ResourceUtils.dataIDFromObject(blockIngredient, "tfc", "supports"), json);
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
        addJson(ResourceUtils.dataID(name, "tfc", "supports"), json);
    }

    @Info(value = "Adds a sluicing definition to the ingredient", params = {
            @Param(name = "ingredient", value = "The ingredient the sluicing definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped for this item")
    })
    public void sluicing(Ingredient ingredient, String lootTable) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(ResourceUtils.dataIDFromObject(ingredient, "tfc", "sluicing"), json);
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
        addJson(ResourceUtils.dataID(name, "tfc", "sluicing"), json);
    }

    @Info(value = "Adds a panning definition to the block ingredient", params = {
            @Param(name = "blockIngredient", value = "The block ingredient the definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped by the ingredient"),
            @Param(name = "models", value = "A list of model locations to be iterated through as panning progresses")
    })
    public void panning(BlockIngredient blockIngredient, String lootTable, String[] models) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        final JsonArray array = new JsonArray();
        for (String s : models) array.add(s);
        json.add("model_stages", array);
        addJson(ResourceUtils.dataIDFromObject(blockIngredient, "tfc", "panning"), json);
    }

    @Info(value = "Adds a panning definition to the block ingredient", params = {
            @Param(name = "blockIngredient", value = "The block ingredient the definition applies to"),
            @Param(name = "lootTable", value = "The location of a loot table to be dropped by the ingredient"),
            @Param(name = "models", value = "A list of model locations to be iterated through as panning progresses"),
            @Param(name = "name", value = "The name of the panning definition")
    })
    public void panning(BlockIngredient blockIngredient, String lootTable, String[] models, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        final JsonArray array = new JsonArray();
        for (String s : models) array.add(s);
        json.add("model_stages", array);
        addJson(ResourceUtils.dataID(name, "tfc", "panning"), json);
    }

    @Info(value = "Specifies the fauna data of the given name", params = {
            @Param(name = "climate", value = "The fauna's climate requirements"),
            @Param(name = "fauna", value = "Additional fauna properties for the given fauna"),
            @Param(name = "name", value = "The name of the fauna definition")
    })
    @Generics({PlacedFeatureProperties.Climate.class, BuildFaunaData.class})
    public void fauna(Consumer<PlacedFeatureProperties.Climate> climate, Consumer<BuildFaunaData> fauna, ResourceLocation name) {
        var faunaObj = new BuildFaunaData(PlacedFeatureProperties.buildClimate(climate));
        fauna.accept(faunaObj);
        addJson(ResourceUtils.dataID(name, "tfc", "fauna"), faunaObj.toJson());
    }

    @Info(value = "Specifies the climate range data of the given name", params = {
            @Param(name = "climateRange", value = "Climate range properties for the given climate range"),
            @Param(name = "name", value = "The name of the climate range")
    })
    @Generics(BuildClimateRangeData.class)
    public void climateRange(Consumer<BuildClimateRangeData> climateRange, ResourceLocation name) {
        var climateRageObj = new BuildClimateRangeData();
        climateRange.accept(climateRageObj);
        addJson(ResourceUtils.dataID(name, "tfc", "climate_ranges"), climateRageObj.toJson());
    }
}
