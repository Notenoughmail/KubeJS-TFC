package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.NativeObject;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemStackProviderJS {

    public static final ItemStackProviderJS EMPTY = new ItemStackProviderJS(new JsonObject(), new JsonArray());

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

    public ItemStackProviderJS simpleProperty(String s) {
        var obj = new JsonObject();
        obj.addProperty("type", s);
        modifiers.add(obj);
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
        var stack = json.has("stack") ? json.get("stack").getAsJsonObject() : new JsonObject();
        var modifiers = json.has("modifiers") ? json.get("modifiers").getAsJsonArray() : new JsonArray();
        return new ItemStackProviderJS(stack, modifiers);
    }

    // Not wholly sure this is right
    public JsonObject toJson() {
        if (stack == null && (modifiers == null || modifiers.isEmpty())) {
            throw new RecipeExceptionJS("KubeJS TFC tried to build an empty item stack provider!");
        }
        if (!Objects.equals(stack, new JsonObject()) && (modifiers == null || modifiers.isEmpty())) {
            return stack;
        }
        var obj = new JsonObject();
        obj.add("modifiers", modifiers);
        if (Objects.equals(stack, new JsonObject())) {
            return obj;
        }
        obj.add("stack", stack);
        return obj;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("ItemStackProvider.of(");
        builder.append(getStack());
        builder.append(", ");
        builder.append(getModifiers());
        builder.append(")");
        return builder.toString();
    }
}
