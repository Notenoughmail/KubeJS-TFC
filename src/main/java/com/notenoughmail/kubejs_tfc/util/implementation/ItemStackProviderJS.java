package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildPortionData;
import dev.latvian.mods.kubejs.core.ItemStackKJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record ItemStackProviderJS(ItemStack stack, JsonArray modifiers) {

    public static final ItemStackProviderJS EMPTY = new ItemStackProviderJS(ItemStack.EMPTY, new JsonArray(0));

    public static ItemStackProviderJS of(@Nullable Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o == null) {
            throw new RecipeExceptionJS("KubeJS TFC tried to build a null Item Stack Provider");
        } else if (o instanceof ItemStack item) {
            return new ItemStackProviderJS(item, new JsonArray());
        } else if (o instanceof ItemStackProviderJS js) {
            return js;
        } else if (o instanceof JsonArray json) {
            return new ItemStackProviderJS(ItemStack.EMPTY, json);
        } else if (o instanceof List<?> list) {
            return new ItemStackProviderJS(ItemStack.EMPTY, parseModifierList(list));
        }

        return new ItemStackProviderJS(ItemStackJS.of(o), new JsonArray());
    }

    public boolean isEmpty() {
        return stack.isEmpty() && modifiers.isEmpty();
    }

    public ItemStackProviderJS withCount(int count) {
        stack.setCount(count);
        return this;
    }

    public int getCount() {
        return stack.getCount();
    }

    public boolean isSimple() {
        return modifiers.isEmpty();
    }

    @Nullable
    public CompoundTag getTag() {
        return stack.getTag();
    }

    public void setTag(CompoundTag tag) {
        stack.setTag(tag);
    }

    public void mergeTag(CompoundTag tag) {
        stack.getOrCreateTag().merge(tag);
    }

    public static ItemStackProviderJS of(ItemStack stack, @Nullable Object b) {
        return new ItemStackProviderJS(stack, parseModifierList(ListJS.orEmpty(b)));
    }

    // TODO: Possibly rework how this handles stuff
    private static JsonArray parseModifierList(List<?> list) {
        var modifiers = new JsonArray();
        for (var element : list) {
            if (element instanceof CharSequence) {
                var obj = new JsonObject();
                obj.addProperty("type", element.toString());
                modifiers.add(obj);
            } else if (element instanceof JsonObject obj) {
                modifiers.add(obj);
            } else {
                modifiers.add(MapJS.json(element));
            }
        }
        return modifiers;
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

    public ItemStackProviderJS jsonModifier(JsonObject json) {
        modifiers.add(json);
        return this;
    }

    public ItemStackProviderJS trait(boolean isAddingTrait, String foodTrait) {
        final JsonObject obj = new JsonObject();
        final String type = isAddingTrait ? "tfc:add_trait" : "tfc:remove_trait";
        obj.addProperty("type", type);
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

    public JsonObject getJsonStack() {
        return IngredientHelpers.itemStackToJson(stack);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackProviderJS provider) {
            return provider.stack().equals(this.stack()) && provider.modifiers.equals(this.modifiers);
        } else if (obj instanceof ItemStack itemStack) {
            return this.modifiers.isEmpty() && itemStack.equals(this.stack);
        }
        return false;
    }

    @Generics(base = JsonObject.class, value = {}) // is this how you do it? Who knows
    public List<JsonObject> getModifiersOfType(String type) {
        final List<JsonObject> list = new ArrayList<>();
        for (JsonElement element : modifiers) {
            if (element.getAsJsonObject().has(type)) {
                list.add(element.getAsJsonObject());
            }
        }
        return list;
    }

    // This assumes if neither element is defined the json is an item stack
    public static ItemStackProviderJS fromJson(JsonObject json) {
        if (!json.has("stack") && !json.has("modifiers")) {
            return new ItemStackProviderJS(ItemStackJS.of(json), new JsonArray());
        }
        var stack = json.has("stack") ? ItemStackJS.of(json.get("stack")) : ItemStack.EMPTY;
        var modifiers = json.has("modifiers") ? json.get("modifiers").getAsJsonArray() : new JsonArray();
        return new ItemStackProviderJS(stack, modifiers);
    }

    public JsonObject toJson() {
        if (stack.isEmpty()) {
            var obj = new JsonObject();
            if (modifiers.isEmpty()) {
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("KubeJS TFC tried to build an empty item stack provider!");
                }
            } else {
                obj.add("modifiers", modifiers());
            }
            return obj;
        } else {
            if (modifiers.isEmpty()) {
                return getJsonStack();
            } else {
                var obj = new JsonObject();
                obj.add("stack", getJsonStack());
                obj.add("modifiers", modifiers());
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
        return "TFC.itemStackProvider.of(" + IngredientHelpers.stringifyItemStack(stack()) + ", " + modifiers() + ")";
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
