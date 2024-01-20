package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.recipe.ISupportProviderOutput;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildPortionData;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public record ItemStackProviderJS(ItemStack stack, JsonArray modifiers) implements OutputReplacement, ReplacementMatch {

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

    @Override
    public Object replaceOutput(RecipeJS recipe, ReplacementMatch match, OutputReplacement original) {
        if (recipe instanceof ISupportProviderOutput) {
            if (original instanceof ItemStackProviderJS originalProvider) {
                final ItemStackProviderJS replacement = new ItemStackProviderJS(stack.copy(), originalProvider.modifiers);
                replacement.withCount(originalProvider.getCount());
                return replacement;
            } else if (original instanceof OutputItem originalOutput) {
                final ItemStackProviderJS replacement = new ItemStackProviderJS(stack.copy(), new JsonArray());
                replacement.withCount(originalOutput.getCount());
                return replacement;
            }

            return new ItemStackProviderJS(stack.copy(), new JsonArray());
        } else {
            // Uh... how?
            final OutputItem replacement = OutputItem.of(stack.copy());
            if (original instanceof OutputItem o) {
                replacement.item.setCount(o.getCount());
            } else if (original instanceof ItemStackProviderJS provider) {
                replacement.item.setCount(provider.getCount());
            }
            return replacement;
        }
    }

    @Info(value = "Returns true if this ISP's stack is empty and the modifier list is empty")
    public boolean isEmpty() {
        return stack.isEmpty() && modifiers.isEmpty();
    }

    @Info(value = "Sets the ISP's count")
    public ItemStackProviderJS withCount(int count) {
        stack.setCount(count);
        return this;
    }

    @Info(value = "Returns the ISP's count, will return 0 if its item stack is empty")
    public int getCount() {
        return stack.getCount();
    }

    @Info(value = "Returns true if the modifier list is empty")
    public boolean isSimple() {
        return modifiers.isEmpty();
    }

    @Info(value = "Returns the item stack's CompoundTag, may be null")
    @Nullable
    public CompoundTag getTag() {
        return stack.getTag();
    }

    @Info(value = "Sets the item stack's CompoundTag")
    public ItemStackProviderJS setTag(CompoundTag tag) {
        stack.setTag(tag);
        return this;
    }

    @Info(value = "Merges the provided CompoundTag into item stack's CompundTag")
    public ItemStackProviderJS mergeTag(CompoundTag tag) {
        stack.getOrCreateTag().merge(tag);
        return this;
    }

    public static ItemStackProviderJS of(ItemStack stack, @Nullable Object b) {
        return new ItemStackProviderJS(stack, parseModifierList(ListJS.orEmpty(b)));
    }

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

    @Info(value = "Adds a simple modifier to the ISP with the type defined by the provided string")
    public ItemStackProviderJS simpleModifier(String s) {
        var obj = new JsonObject();
        obj.addProperty("type", s);
        modifiers.add(obj);
        return this;
    }

    @Info(value = "Adds the provided JsonObject to the modifier list")
    public ItemStackProviderJS jsonModifier(JsonObject json) {
        modifiers.add(json);
        return this;
    }

    public ItemStackProviderJS trait(boolean isAddingTrait, String foodTrait) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("type", isAddingTrait ? "tfc:add_trait" : "tfc:remove_trait");
        obj.addProperty("trait", foodTrait);
        modifiers.add(obj);
        return this;
    }

    @Info(value = "Adds the 'tfc:dye_leather', modifier to the ISP with the provided color")
    public ItemStackProviderJS dyeLeather(String color) {
        var obj = new JsonObject();
        obj.addProperty("type", "tfc:dye_leather");
        obj.addProperty("color", color); // Guessing as to the structure based on code as I don't know where this is actually used, and it's not in the docs
        modifiers.add(obj);
        return this;
    }

    @Info(value = "Returns the json representation of the ISP's item stack")
    public JsonObject getJsonStack() {
        return IngredientHelpers.itemStackToJson(stack);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackProviderJS provider) {
            return provider.stack().equals(this.stack()) && provider.modifiers.equals(this.modifiers);
        } else if (obj instanceof ItemStack itemStack) {
            return this.isSimple() && itemStack.equals(this.stack);
        } else if (obj instanceof ItemStackProvider provider) {
            return provider.equals(asCanonClass());
        }
        return false;
    }

    @Info(value = "Returns a list of JsonObjects consisting of the applied modifiers which match the requested type")
    @Generics(value = JsonObject.class)
    public List<JsonObject> getModifiersOfType(String type) {
        final List<JsonObject> list = new ArrayList<>();
        for (JsonElement element : modifiers) {
            if (Objects.equals(element.getAsJsonObject().get("type").getAsString(), type)) {
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

    @Info(value = "Returns the json representation of this ISP")
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

    @Info(value = "Returns an object of the canon ItemStackProvider class matching the ISP this ItemStackProviderJS represents")
    public ItemStackProvider asCanonClass() {
        return ItemStackProvider.fromJson(toJson());
    }

    @Info(value = "Returns true if the ISP depends on a recipe's input")
    public boolean dependsOnInput() {
        for (JsonElement element : modifiers) {
            if (ItemStackModifiers.fromJson(element).dependsOnInput()) {
                return true;
            }
        }
        return false;
    }

    @Info(value = "Returns a copy of the ISP")
    public ItemStackProviderJS copy() {
        return new ItemStackProviderJS(stack.copy(), modifiers.deepCopy());
    }

    @Override
    public String toString() {
        return "TFC.itemStackProvider." + (isEmpty() ? "empty()" : "of(" + IngredientHelpers.stringifyItemStack(stack()) + ", " + modifiers() + ")");
    }

    @Info(value = "Adds a 'tfc:add_heat' modifier to the ISP", params = @Param(name = "temperature", value = "The °C to add to the item"))
    public ItemStackProviderJS addHeat(float temperature) {
        var obj = new JsonObject();
        obj.addProperty("type", "tfc:add_heat");
        obj.addProperty("temperature", temperature);
        modifiers.add(obj);
        return this;
    }

    @Info(value = "Adds a 'tfc:add_powder' modifier to the ISP")
    public ItemStackProviderJS addPowder() {
        return simpleModifier("tfc:add_powder");
    }

    @Info(value = "Adds a 'tfc:add_trait' modifier to the ISP", params = @Param(name = "trait", value = "The food trait to be added"))
    public ItemStackProviderJS addTrait(String trait) {
        return this.trait(true, trait);
    }

    @Info(value = "Adds a 'tfc:remove_trait' modifier to the ISP", params = @Param(name = "trait", value = "The food trait to be removed"))
    public ItemStackProviderJS removeTrait(String trait) {
        return this.trait(false, trait);
    }

    @Info(value = "Adds a 'tfc:add_glass' modifier to the ISP, used as part of glassworking recipes")
    public ItemStackProviderJS addGlass() {
        return this.simpleModifier("tfc:add_glass");
    }

    @Info(value = "Adds a 'tfc:copy_food' modifier to the ISP")
    public ItemStackProviderJS copyFood() {
        return this.simpleModifier("tfc:copy_food");
    }

    @Info(value = "Adds a 'tfc:copy_forging_bonus' modifier to the ISP")
    public ItemStackProviderJS copyForgingBonus() {
        return this.simpleModifier("tfc:copy_forging_bonus");
    }

    @Info(value = "Adds a 'tfc:copy_heat' modifier to the ISP")
    public ItemStackProviderJS copyHeat() {
        return this.simpleModifier("tfc:copy_heat");
    }

    @Info(value = "Adds a 'tfc:copy_input' modifier to the ISP")
    public ItemStackProviderJS copyInput() {
        return this.simpleModifier("tfc:copy_input");
    }

    @Info(value = "Adds a 'tfc:empty_bowl' modifier to the ISP. This is supported by soup items")
    public ItemStackProviderJS emptyBowl() {
        return this.simpleModifier("tfc:empty_bowl");
    }

    @Info(value = "Adds a 'tfc:reset_food' modifier to the ISP")
    public ItemStackProviderJS resetFood() {
        return this.simpleModifier("tfc:reset_food");
    }

    @Info(value = "Adds a 'tfc:copy_oldest_food' modifier to the ISP")
    public ItemStackProviderJS copyOldestFood() {
        return this.simpleModifier("tfc:copy_oldest_food");
    }

    @Info(value = "Adds a 'tfc:add_bait_to_rod' modifier to the ISP")
    public ItemStackProviderJS addBait() {
        return this.simpleModifier("tfc:add_bait_to_rod");
    }

    @Info(value = "Adds a 'tfc:meal' modifier to the ISP", params = {
            @Param(name = "food", value = "The base food data values for the meal modifier"),
            @Param(name = "portions", value = "The portion data values for the meal modifier")
    })
    @Generics(value = {BuildFoodItemData.class, Consumer.class, BuildPortionData.class})
    public ItemStackProviderJS meal(Consumer<BuildFoodItemData> food, List<Consumer<BuildPortionData>> portions) {
        final JsonObject obj = mealBase(food);
        JsonArray portionArray = new JsonArray(portions.size());
        for (Consumer<BuildPortionData> portion : portions) {
            var portionData = new BuildPortionData();
            portion.accept(portionData);
            portionArray.add(portionData.toJson());
        }
        obj.add("portions", portionArray);
        modifiers.add(obj);
        return this;
    }

    @Info(value = "Adds a 'tfc:meal' modifier to the ISP", params = @Param(name = "food", value = "The base food data values for the meal modifier"))
    @Generics(value = BuildFoodItemData.class)
    public ItemStackProviderJS meal(Consumer<BuildFoodItemData> food) {
        modifiers.add(mealBase(food));
        return this;
    }

    private JsonObject mealBase(Consumer<BuildFoodItemData> food) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "tfc:meal");
        var foodData = new BuildFoodItemData(null);
        food.accept(foodData);
        obj.add("food", foodData.toJson());
        return obj;
    }
}
