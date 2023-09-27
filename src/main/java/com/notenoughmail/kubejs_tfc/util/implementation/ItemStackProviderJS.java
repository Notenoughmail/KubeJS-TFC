package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildPortionData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.ModifyCondition;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ItemStackProviderJS {

    public static final ItemStackProviderJS EMPTY = new ItemStackProviderJS(ItemStackJS.EMPTY, null);

    public static ItemStackProviderJS of(@Nullable Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o == null) {
            throw new RecipeExceptionJS("KubeJS TFC tried to build a null Item Stack Provider");
        } else if (o instanceof ItemStackJS item) {
            return new ItemStackProviderJS(item, new JsonArray());
        } else if (o instanceof ItemStackProviderJS js) {
            return js;
        } else if (o instanceof JsonArray json) {
            return new ItemStackProviderJS(ItemStackJS.EMPTY, json);
        } else if (o instanceof CharSequence || o instanceof ResourceLocation || o instanceof JsonElement) {
            return new ItemStackProviderJS(ItemStackJS.of(o), new JsonArray());
        } else if (o instanceof List<?> list) {
            return new ItemStackProviderJS(ItemStackJS.EMPTY, parseModifierList(list));
        }

        return EMPTY;
    }

    // Helpful methods left over from when I initially panic-implemented IngredientJS, there's a reason I didn't do that to begin with
    public boolean test(ItemStackJS itemStackJS) {
        return stack.test(itemStackJS) && modifiers.isEmpty();
    }

    public boolean isEmpty() {
        return stack.isEmpty() && modifiers.isEmpty();
    }

    public ItemStackProviderJS withCount(int count) {
        stack.withCount(count);
        return this;
    }

    public ItemStackProviderJS x(int c) {
        stack.x(c);
        return this;
    }

    public int getCount() {
        return stack.getCount();
    }

    public boolean isSimple() {
        return modifiers.isEmpty();
    }

    public static ItemStackProviderJS of(@Nullable Object o, @Nullable Object b) {
        return new ItemStackProviderJS(ItemStackJS.of(o), parseModifierList(ListJS.orSelf(b)));
    }

    private static JsonArray parseModifierList(List<?> list) {
        var modifiers = new JsonArray();
        for (var element : list) {
            if (element instanceof NativeObject object) {
                modifiers.add(ListJS.orSelf(object).toJson().get(0).getAsJsonObject());
            } else if (element instanceof CharSequence) {
                var obj = new JsonObject();
                obj.addProperty("type", element.toString());
                modifiers.add(obj);
            } else if (element instanceof JsonObject obj) {
                modifiers.add(obj);
            } else if (element instanceof MapJS map) {
                modifiers.add(map.toJson());
            }
        }
        return modifiers;
    }

    private final ItemStackJS stack;
    private final JsonArray modifiers;

    @Deprecated
    public ItemStackProviderJS(JsonObject stack, JsonArray modifiers) {
        this.stack = ItemStackJS.of(stack);
        this.modifiers = modifiers;
    }

    public ItemStackProviderJS(ItemStackJS stack, JsonArray modifiers) {
        this.stack = stack;
        this.modifiers = modifiers;
    }

    public ItemStackProviderJS addHeat(int temperature) {
        var obj = new JsonObject();
        obj.addProperty("type", "tfc:add_heat");
        obj.addProperty("temperature", temperature);
        modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS simpleModifier(String s) {
        var obj = new JsonObject();
        obj.addProperty("type", s);
        modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS jsonModifier(Object o) {
        if (o instanceof JsonElement json) {
            if (json.isJsonArray()) {
                for (var element : json.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        modifiers.add(element.getAsJsonObject());
                    } else if (element.isJsonPrimitive()) {
                        var obj = new JsonObject();
                        obj.addProperty("type", element.getAsString());
                        modifiers.add(obj);
                    } else {
                        throw new RecipeExceptionJS("Provided element in json array should be a json object or json primitive!");
                    }
                }
            } else if (json.isJsonObject()) {
                modifiers.add(json.getAsJsonObject());
            } else {
                throw new RecipeExceptionJS("Provided json modifier must either be a json array or object!");
            }
        } else if (o instanceof NativeObject nativeObject) {
            modifiers.add(ListJS.orSelf(nativeObject).toJson().getAsJsonObject());
        } else if (o instanceof NativeArray nativeArray) {
            modifiers.addAll(parseModifierList(nativeArray));
        } else if (o instanceof MapJS map) {
            modifiers.add(map.toJson());
        } else {
            throw new RecipeExceptionJS("Provided json modifier failed to parse!");
        }
        return this;
    }

    public ItemStackProviderJS trait(boolean isAddingTrait, String foodTrait) {
        var obj = new JsonObject();
        if (isAddingTrait) {
            obj.addProperty("type", "tfc:add_trait");
        } else {
            obj.addProperty("type", "tfc:remove_trait");
        }
        obj.addProperty("trait", foodTrait);
        modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS dyeLeather(String color) {
        var obj = new JsonObject();
        obj.addProperty("type", "tfc:dye_leather");
        obj.addProperty("color", color); // Guessing as to the structure based on code as I don't know where this is actually used, and it's not in the docs
        modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS conditional(Consumer<ModifyCondition> conditional, Consumer<ItemStackProviderJS> modifiers, @Nullable Consumer<ItemStackProviderJS> elseModifiers) {
        if (!KubeJSTFC.channelCastingLoaded()) {
            return this;
        }

        var obj = new JsonObject();
        obj.addProperty("type", "tfcchannelcasting:conditional");

        var condition = new ModifyCondition();
        conditional.accept(condition);
        obj.add("condition", condition.toJson());

        var modifier = new ItemStackProviderJS(ItemStackJS.EMPTY, new JsonArray());
        modifiers.accept(modifier);
        obj.add("modifiers", modifier.getModifiers());

        if (elseModifiers != null) {
            var elseModifier = new ItemStackProviderJS(ItemStackJS.EMPTY, new JsonArray());
            elseModifiers.accept(elseModifier);
            obj.add("else_modifiers", elseModifier.getModifiers());
        }
        this.modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS setFoodData(Consumer<BuildFoodItemData> foodData) {
        if (!KubeJSTFC.channelCastingLoaded()) {
            return this;
        }

        var data = new BuildFoodItemData(null);
        foodData.accept(data);
        var obj = data.toJson();
        obj.addProperty("type", "tfcchannelcasting:set_food_data");
        modifiers.add(obj);
        return this;
    }

    public JsonObject getJsonStack() {
        return stack.toResultJson().getAsJsonObject();
    }

    public ItemStackJS getStackJS() {
        return stack;
    }

    /**
     * Tests if the provided item provider's stack equals this item provider's stack, logic is handled natively by KubeJS' own ItemStackJS
     * @param other The item provider to be tested
     * @return True if the item providers' stacks are equal
     */
    public boolean test(ItemStackProviderJS other) {
        return this.getStackJS().test(other.getStackJS()); // Kube can handle it
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemStackProviderJS provider && provider.getStackJS().equals(this.getStackJS()) && provider.modifiers.equals(this.modifiers);
    }

    public JsonArray getModifiers() {
        return modifiers;
    }

    // This assumes if neither element is defined the json is an item stack
    public static ItemStackProviderJS fromJson(JsonObject json) {
        if (!json.has("stack") && !json.has("modifiers")) {
            return new ItemStackProviderJS(ItemStackJS.of(json), new JsonArray());
        }
        var stack = json.has("stack") ? ItemStackJS.of(json.get("stack")) : ItemStackJS.EMPTY;
        var modifiers = json.has("modifiers") ? json.get("modifiers").getAsJsonArray() : new JsonArray();
        return new ItemStackProviderJS(stack, modifiers);
    }

    public JsonObject toJson() {
        if (stack.isEmpty()) {
            var obj = new JsonObject();
            if (modifiers == null || modifiers.isEmpty()) {
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("KubeJS TFC tried to build an empty item stack provider!");
                }
            } else {
                obj.add("modifiers", modifiers);
            }
            return obj;
        } else {
            if (modifiers == null || modifiers.isEmpty()) {
                return getJsonStack();
            } else {
                var obj = new JsonObject();
                obj.add("stack", getJsonStack());
                obj.add("modifiers", modifiers);
                return obj;
            }
        }
    }

    public ItemStackProvider asJavaObject() {
        return ItemStackProvider.fromJson(toJson());
    }

    public ItemStackProviderJS copy() {
        return new ItemStackProviderJS(stack.copy(), modifiers.deepCopy());
    }

    @Override
    public String toString() {
        return "ItemStackProvider.of(" + getStackJS() + ", " + getModifiers() + ")";
    }

    public ItemStackProviderJS addTrait(String s) {
        return this.trait(true, s);
    }

    public ItemStackProviderJS removeTrait(String s) {
        return this.trait(false, s);
    }

    public ItemStackProviderJS copyFood() {
        return this.simpleModifier("tfc:copy_food");
    }

    public ItemStackProviderJS copyForgingBonus() {
        return this.simpleModifier("tfc:copy_forging_bonus");
    }

    public ItemStackProviderJS copyHeat() {
        return this.simpleModifier("tfc:copy_heat");
    }

    public ItemStackProviderJS copyInput() {
        return this.simpleModifier("tfc:copy_input");
    }

    public ItemStackProviderJS emptyBowl() {
        return this.simpleModifier("tfc:empty_bowl");
    }

    public ItemStackProviderJS resetFood() {
        return this.simpleModifier("tfc:reset_food");
    }

    public ItemStackProviderJS copyOldestFood() {
        return this.simpleModifier("tfc:copy_oldest_food");
    }

    public ItemStackProviderJS burrito() {
        return KubeJSTFC.firmaLoaded() ? this.simpleModifier("firmalife:burrito") : this;
    }

    public ItemStackProviderJS pie() {
        return KubeJSTFC.firmaLoaded() ? this.simpleModifier("firmalife:pie") : this;
    }

    public ItemStackProviderJS pizza() {
        return KubeJSTFC.firmaLoaded() ? this.simpleModifier("firmalife:pizza") : this;
    }

    public ItemStackProviderJS copyDynamicFood() {
        return KubeJSTFC.firmaLoaded() ? this.simpleModifier("firmalife:copy_dynamic_food") : this; // Can't find usage, looking at code seems to be simple
    }

    public ItemStackProviderJS emptyPan(){
        return KubeJSTFC.firmaLoaded() ? this.simpleModifier("firmalife:empty_pan") : this; // Can't find usage, looking at code seems to be simple
    }

    public ItemStackProviderJS addBait() {
        return this.simpleModifier("tfc:add_bait_to_rod");
    }

    public ItemStackProviderJS sandwich() {
        return this.simpleModifier("tfc:sandwich");
    }

    @SafeVarargs
    public final ItemStackProviderJS meal(Consumer<BuildFoodItemData> food, @Nullable Consumer<BuildPortionData>... portions) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "tfc:meal");
        var foodData = new BuildFoodItemData(null);
        food.accept(foodData);
        obj.add("food", foodData.toJson());
        if (portions != null) {
            JsonArray portionArray = new JsonArray();
            for (Consumer<BuildPortionData> portion : portions) {
                var portionData = new BuildPortionData();
                assert portion != null;
                portion.accept(portionData);
                portionArray.add(portionData.toJson());
            }
            obj.add("portions", portionArray);
        }
        modifiers.add(obj);
        return this;
    }

    public ItemStackProviderJS meal(Consumer<BuildFoodItemData> food) {
        return meal(food, (Consumer<BuildPortionData>) null);
    }
}
