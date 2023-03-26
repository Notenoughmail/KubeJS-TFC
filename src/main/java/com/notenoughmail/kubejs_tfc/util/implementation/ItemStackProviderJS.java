package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.NativeArray;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackProviderJS {

    public static final ItemStackProviderJS EMPTY = new ItemStackProviderJS(null, new JsonArray());

    public static ItemStackProviderJS of(@Nullable Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o == null) {
            throw new RecipeExceptionJS("KubeJS TFC tried to build a null Item Stack Provider");
        } else if (o instanceof ItemStackJS item) {
            return new ItemStackProviderJS(item.toResultJson().getAsJsonObject(), new JsonArray());
        } else if (o instanceof ItemStackProviderJS js) {
            return js;
        } else if (o instanceof JsonArray json) {
            return new ItemStackProviderJS(new JsonObject(), json);
        } else if (o instanceof JsonObject json) {
            return new ItemStackProviderJS(json, new JsonArray());
        } else if (o instanceof CharSequence || o instanceof ResourceLocation) {
            return new ItemStackProviderJS(ItemStackJS.of(o).toResultJson().getAsJsonObject(), new JsonArray());
        } else if (o instanceof List<?> list) {
            return new ItemStackProviderJS(new JsonObject(), parseModifierList(list));
        }

        return EMPTY;
    }

    public static ItemStackProviderJS of(@Nullable Object o, @Nullable Object b) {
        return new ItemStackProviderJS(of(o).getStack(), of(b instanceof List<?> ? b : ListJS.orSelf(b)).getModifiers());
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
            }
        }
        return modifiers;
    }

    private final JsonObject stack;
    private final JsonArray modifiers;

    ItemStackProviderJS(JsonObject stack, JsonArray modifiers) {
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

    public JsonObject getStack() {
        return stack;
    }

    public JsonArray getModifiers() {
        return modifiers;
    }

    // This assumes if neither element is defined the json is an item stack
    public static ItemStackProviderJS fromJson(JsonObject json) {
        if (!json.has("stack") && !json.has("modifiers")) {
            return new ItemStackProviderJS(json, new JsonArray());
        }
        var stack = json.has("stack") ? json.get("stack").getAsJsonObject() : null;
        var modifiers = json.has("modifiers") ? json.get("modifiers").getAsJsonArray() : new JsonArray();
        return new ItemStackProviderJS(stack, modifiers);
    }

    public JsonObject toJson() {
        if (stack == null) {
            if (modifiers == null || modifiers.isEmpty()) {
                var obj = new JsonObject();
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("KubeJS TFC tried to build an empty item stack provider!");
                }
                return obj;
            } else {
                var obj = new JsonObject();
                obj.add("modifiers", modifiers);
                return obj;
            }
        } else {
            if (modifiers == null || modifiers.isEmpty()) {
                return stack;
            } else {
                var obj = new JsonObject();
                obj.add("stack", stack);
                obj.add("modifiers", modifiers);
                return obj;
            }
        }
    }

    @Override
    public String toString() {
        return "ItemStackProvider.of(" + getStack() + ", " + getModifiers() + ")";
    }

    // static methods wooooo 🙃

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

    public ItemStackProviderJS addBait() {
        return this.simpleModifier("tfc:add_bait_to_rod");
    }

    public ItemStackProviderJS sandwich() {
        return this.simpleModifier("tfc:sandwich");
    }
}
