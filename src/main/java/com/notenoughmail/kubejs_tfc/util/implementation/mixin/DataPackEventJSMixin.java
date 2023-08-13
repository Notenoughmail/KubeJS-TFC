package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildDrinkableData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildPlantableData;
import com.notenoughmail.kubejs_tfc.util.implementation.worldgen.*;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.data.DataPackEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@Mixin(value = DataPackEventJS.class, remap = false)
public abstract class DataPackEventJSMixin {

    private static final String notANumber = "[^0-9.-]";
    private static final String splitters = "[,;:]";

    @Shadow(remap = false)
    public abstract void addJson(ResourceLocation resourceLocation, JsonElement json);

    @RemapForJS("addTFCItemDamageResistance")
    @Unique
    public void kubeJS_TFC$addTFCItemDamageResistance(IngredientJS ingredient, String values) { // "p=20, c=50"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(3, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.matches("p(?>iercing)?.+")) {
                json.addProperty("piercing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("s(?>lashing)?.+")) {
                json.addProperty("slashing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("c(?>rushing)?.+")) {
                json.addProperty("crushing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(p|s|c)=\\d+/");
            }
        }
        addJson(kubeJS_TFC$dataIDTFC("item_damage_resistances/" + kubeJS_TFC$ingredientToName(ingredientJS)), json);
    }

    @RemapForJS("addTFCEntityDamageResistance")
    @Unique
    public void kubeJS_TFC$addTFCEntityDamageResistance(String entityTag, String values) { // "p=20, c=50"
        var json = new JsonObject();
        json.addProperty("entity", entityTag);
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(3, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.matches("p(?>iercing)?.+")) {
                json.addProperty("piercing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("s(?>lashing)?.+")) {
                json.addProperty("slashing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.matches("c(?>rushing)?.+")) {
                json.addProperty("crushing", (int) Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(p|s|c)=\\d+/");
            }
        }
        addJson(kubeJS_TFC$dataIDTFC("entity_damage_resistances/" + kubeJS_TFC$ingredientToName(entityTag)), json);
    }

    @RemapForJS("addTFCDrinkable")
    @Unique
    public void kubeJS_TFC$addTFCDrinkable(FluidStackIngredientJS fluidIngredient, Consumer<BuildDrinkableData> drinkableData) {
        var data = new BuildDrinkableData(fluidIngredient);
        drinkableData.accept(data);
        addJson(kubeJS_TFC$dataIDTFC("drinkables/" + kubeJS_TFC$ingredientToName(fluidIngredient)), data.toJson());
    }

    @RemapForJS("addTFCFertilizer")
    @Unique
    public void kubeJS_TFC$addTFCFertilizer(IngredientJS ingredient, String values) { // "n=0.2, p=0.2, k=0.2"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(3, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.charAt(0) == 'n' || value.matches("nitrogen.+")) {
                json.addProperty("nitrogen",  Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'p' || value.matches("phosphorus.+")) {
                json.addProperty("phosphorus", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'k' || value.matches("potassium.+")) {
                json.addProperty("potassium", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(n|p|k)=\\d*.\\d+/");
            }
        }
        addJson(kubeJS_TFC$dataIDTFC("fertilizers/" + kubeJS_TFC$ingredientToName(ingredientJS)), json);
    }

    @RemapForJS("addTFCFoodItem")
    @Unique
    public void kubeJS_TFC$addTFCFoodItem(IngredientJS ingredient, Consumer<BuildFoodItemData> foodItemData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildFoodItemData(ingredientJS);
        foodItemData.accept(data);
        addJson(kubeJS_TFC$dataIDTFC("food_items/" + kubeJS_TFC$ingredientToName(ingredientJS)), data.toJson());
    }

    @RemapForJS("addTFCFuel")
    @Unique
    public void kubeJS_TFC$addTFCFuel(IngredientJS ingredient, float temperature, int duration) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("temperature", temperature);
        json.addProperty("duration", duration);
        addJson(kubeJS_TFC$dataIDTFC("fuels/" + kubeJS_TFC$ingredientToName(ingredientJS)), json);
    }

    @RemapForJS("addTFCHeat")
    @Unique
    public void kubeJS_TFC$addTFCHeat(IngredientJS ingredient, float... values) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        if (values.length < 1) {
            ConsoleJS.SERVER.error("Heat data for " + ingredientJS.toString() + " does not define a heat capacity!");
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
        addJson(kubeJS_TFC$dataIDTFC("item_heats/" + kubeJS_TFC$ingredientToName(ingredientJS)), json);
    }

    @RemapForJS("addTFCItemSize")
    @Unique
    public void kubeJS_TFC$addTFCItemSize(IngredientJS ingredient, String values) { // "s=tiny, weight=medium"
        var ingredientjS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientjS.toJson());
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(2, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.matches("s(?>ize)?=.+")) {
                var constant = value.split("=");
                if (!constant[1].matches("(?>tiny|(?>very_)?(?>small|large)|normal|huge)")) {
                    ConsoleJS.SERVER.error("Size value cannot be '" + constant[1] + "', must be tiny, very_small, small, normal, large, very_large, or huge!");
                } else {
                    json.addProperty("size", constant[1]);
                }
            } else if (value.matches("w(?>eight)?=.+")) {
                var constant = value.split("=");
                if (!constant[1].matches("(?>(?>very_)?(?>light|heavy)|medium)")) {
                    ConsoleJS.SERVER.error("Weight value cannot be '" + constant[1] + "', it must be very_light, light, medium, heavy, or very_heavy!");
                } else {
                    json.addProperty("weight", constant[1]);
                }
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! The value should match /(s=(tiny|very_small|small|normal|large|very_large|huge)|w=(very_light|light|medium|heavy|very_heavy))/");
            }
        }
        addJson(kubeJS_TFC$dataIDTFC("item_sizes/" + kubeJS_TFC$ingredientToName(ingredientjS)), json);
    }

    @RemapForJS("addTFCLampFuel")
    @Unique
    public void kubeJS_TFC$addTFCLampFuel(FluidStackIngredientJS fluidIngredient, BlockIngredientJS blockIngredient, int burnRate) {
        var json = new JsonObject();
        json.add("fluid", fluidIngredient.toJsonNoAmount());
        json.add("valid_lamps", blockIngredient.toJson());
        json.addProperty("burn_rate", burnRate);
        addJson(kubeJS_TFC$dataIDTFC("lamp_fuels/" + kubeJS_TFC$ingredientToName(fluidIngredient)), json);
    }

    @RemapForJS("addTFCMetal")
    @Unique
    public void kubeJS_TFC$addTFCMetal(String fluid, float meltTemp, float heatCap, IngredientJS ingotIngredient, IngredientJS sheetIngredient) {
        kubeJS_TFC$addTFCMetal(fluid, meltTemp, heatCap, ingotIngredient, sheetIngredient, 0);
    }

    @RemapForJS("addTFCMetal")
    @Unique
    public void kubeJS_TFC$addTFCMetal(String fluid, float meltTemp, float heatCap, IngredientJS ingotIngredient, IngredientJS sheetIngredient, int tier) {
        var ingotIngredientJS = ingotIngredient.unwrapStackIngredient().get(0);
        var sheetIngredientJS = sheetIngredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.addProperty("tier", tier);
        json.addProperty("fluid", fluid);
        json.addProperty("melt_temperature", meltTemp);
        json.addProperty("specific_heat_capacity", heatCap);
        json.add("ingots", ingotIngredientJS.toJson());
        json.add("sheets", sheetIngredientJS.toJson());
        addJson(kubeJS_TFC$dataIDTFC("metals/" + kubeJS_TFC$ingredientToName(fluid)), json);
        // The name has potential to collide if the user defines multiple metals off of one fluid, but TFC states
        // "   Creating multiple metals that reference the same fluid is
        //     liable to cause undefined behavior and may introduce bugs   "
        // Thus pretend this is actually a safeguard against undefined behavior 👍
    }

    @RemapForJS("addTFCSupport")
    @Unique
    public void kubeJS_TFC$addTFCSupport(BlockIngredientJS blockIngredient, int up, int down, int horizontal) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("support_up", up);
        json.addProperty("support_down", down);
        json.addProperty("support_horizontal", horizontal);
        addJson(kubeJS_TFC$dataIDTFC("supports/" + kubeJS_TFC$ingredientToName(blockIngredient)), json);
    }

    @RemapForJS("addTFCSluicing")
    @Unique
    public void kubeJS_TFC$addTFCSluicing(IngredientJS ingredient, String lootTable) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        json.addProperty("loot_table", lootTable);
        addJson(kubeJS_TFC$dataIDTFC("sluicing/" + kubeJS_TFC$ingredientToName(ingredient)), json);
    }

    @RemapForJS("addTFCPanning")
    @Unique
    public void kubeJS_TFC$addTFCPanning(BlockIngredientJS blockIngredient, String lootTable, String... models) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("loot_table", lootTable);
        var array = new JsonArray();
        for (String model : models) {
            array.add(model);
        }
        json.add("model_stages", array);
        addJson(kubeJS_TFC$dataIDTFC("panning/" + kubeJS_TFC$ingredientToName(blockIngredient)), json);
    }

    @RemapForJS("addFLGreenhouse")
    @Unique
    public void kubeJS_TFC$addFLGreenhouse(BlockIngredientJS blockIngredient, int tier) {
        var json = new JsonObject();
        json.add("ingredient", blockIngredient.toJson());
        json.addProperty("tier", tier);
        addJson(kubeJS_TFC$dataIDFL("greenhouse/" + kubeJS_TFC$ingredientToName(blockIngredient)), json);
    }

    @RemapForJS("addFLPlantable")
    @Unique
    public void kubeJS_TFC$addFLPlantable(IngredientJS ingredient, Consumer<BuildPlantableData> plantableData) {
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var data = new BuildPlantableData(ingredientJS);
        plantableData.accept(data);
        addJson(kubeJS_TFC$dataIDFL("plantable/" + kubeJS_TFC$ingredientToName(ingredientJS)), data.toJson());
    }

    // Why the hell not
    @RemapForJS("addBeneathFertilizer")
    @Unique
    public void kubeJS_TFC$addBeneathFertilizer(IngredientJS ingredient, String values) { // "d=0.2, f=0.5"
        var ingredientJS = ingredient.unwrapStackIngredient().get(0);
        var json = new JsonObject();
        json.add("ingredient", ingredientJS.toJson());
        var splitValues = values.replace(" ", "").toLowerCase(Locale.ROOT).split(splitters);
        for (int i = 0 ; i < Math.min(5, splitValues.length) ; i++) {
            var value = splitValues[i];
            if (value.charAt(0) == 'd' || value.matches("death.+")) {
                json.addProperty("death", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 't' || value.matches("destruction.+")) {
                json.addProperty("destruction", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'f' || value.matches("flame.+")) {
                json.addProperty("flame", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 'c' || value.matches("decay.+")) {
                json.addProperty("decay", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else if (value.charAt(0) == 's' || value.matches("sorrow.+")) {
                json.addProperty("sorrow", Float.parseFloat(value.replaceAll(notANumber, "")));
            } else {
                ConsoleJS.SERVER.error("Value '" + value + "' in values '" + values + "' is not valid! the value should match /(d|t|f|c|s)=\\d*.\\d+/");
            }
        }
        addJson(kubeJS_TFC$dataIDBN("nether_fertilizers/" + kubeJS_TFC$ingredientToName(ingredientJS)), json);
    }

    @Unique
    private ResourceLocation kubeJS_TFC$dataIDTFC(String location) {
        return new ResourceLocation("kubejs_tfc", "tfc/" + location);
    }

    @Unique
    private ResourceLocation kubeJS_TFC$dataIDFL(String location) {
        return new ResourceLocation("kubejs_tfc", "firmalife/" + location);
    }

    @Unique
    private ResourceLocation kubeJS_TFC$dataIDBN(String location) {
        return new ResourceLocation("kubejs_tfc", "beneath/" + location);
    }

    @Unique
    private String kubeJS_TFC$ingredientToName(Object in) {
        String out = in.toString().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_") // Make the string not explode when parsed (yes I know there are a few more allowed chars, but it doesn't matter)
                .replaceAll("_+", "_")        // Remove duplicate underscores
                .replaceAll("^_", "")         // Remove leading underscores
                .replaceAll("_$", "");        // Remove trailing underscores
        return out.length() > 64 ? out.substring(0, 64) : out;// Limit length to 64 chars
    }

    //================================WORLDGEN================================ (misery)

    @RemapForJS("buildTFCGeode")
    @Unique
    public void kubeJS_TFC$buildTFCGeode(String name, Consumer<BuildGeodeProperties> geode, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildGeodeProperties();
        geode.accept(properties);
        addJson(kubeJS_TFC$configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(kubeJS_TFC$placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCBoulder")
    @Unique
    public void kubeJS_TFC$buildTFCBoulder(String name, Consumer<BuildBoulderProperties> boulder, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildBoulderProperties();
        boulder.accept(properties);
        addJson(kubeJS_TFC$configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(kubeJS_TFC$placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCThinSpike")
    @Unique
    public void kubeJS_TFC$buildTFCThinSpike(String name, Consumer<BuildThinSpikeProperties> spike, Consumer<PlacedFeatureProperties> placement) {
        var properties = new BuildThinSpikeProperties();
        spike.accept(properties);
        addJson(kubeJS_TFC$configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(kubeJS_TFC$placedFeatureName(name), place.toJson());
    }

    @RemapForJS("buildTFCVein")
    @Unique
    public void kubeJS_TFC$buildTFCVein(String name, Consumer<BuildVeinProperties> vein) {
        var properties = new BuildVeinProperties(name);
        vein.accept(properties);
        addJson(kubeJS_TFC$configuredFeatureName(name), properties.toJson());
        var place = new PlacedFeatureProperties(name);
        addJson(kubeJS_TFC$placedFeatureName(name), place.toJson());
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
        addJson(kubeJS_TFC$configuredFeatureName(name), json);
        var place = new PlacedFeatureProperties(name);
        placement.accept(place);
        addJson(kubeJS_TFC$placedFeatureName(name), place.toJson());
    }

    @Unique
    private ResourceLocation kubeJS_TFC$configuredFeatureName(String path) {
        return new ResourceLocation("kubejs_tfc", "worldgen/configured_feature/" + path);
    }

    @Unique
    private ResourceLocation kubeJS_TFC$placedFeatureName(String path) {
        return new ResourceLocation("kubejs_tfc", "worldgen/placed_feature/" + path);
    }
}
