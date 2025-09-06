package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.recipe.ISupportProviderOutput;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
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
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
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
        } else if (o instanceof JsonObject json) {
            return fromJson(json);
        } else if (o instanceof ItemStackProvider isp) {
            final JsonArray array = new JsonArray(isp.modifiers().length);
            for (ItemStackModifier mod : isp.modifiers()) {
                array.add(KubeJSTFC.convertISM(mod));
            }
            return new ItemStackProviderJS(isp.stack().get(), array);
        }

        return new ItemStackProviderJS(ItemStackJS.of(o), new JsonArray());
    }

    public static ItemStackProvider ofCanon(@Nullable Object o) {
        if (o instanceof ItemStackProvider isp) {
            return isp; // Skip the serialize & deserialize trip when canonizing
        }
        return of(o).asCanonClass();
    }

    @Override
    public Object replaceOutput(RecipeJS recipe, ReplacementMatch match, OutputReplacement original) {
        if (original instanceof ItemStackProviderJS originalProvider) {
            return withCount(originalProvider.getCount());
        } else if (original instanceof OutputItem originalOutput) {
            return withCount(originalOutput.getCount());
        }

        return this;
    }

    @Info("Returns true if this ISP's stack is empty and the modifier list is empty")
    public boolean isEmpty() {
        return stack.isEmpty() && modifiers.isEmpty();
    }

    @Info("Sets the ISP's count")
    public ItemStackProviderJS withCount(int count) {
        stack.setCount(count);
        return this;
    }

    @Info("Returns the ISP's count, will return 0 if its item stack is empty")
    public int getCount() {
        return stack.getCount();
    }

    @Info("Returns true if the modifier list is empty")
    public boolean isSimple() {
        return modifiers.isEmpty();
    }

    @Info("Returns the item stack's `CompoundTag`, may be null")
    @Nullable
    public CompoundTag getTag() {
        return stack.getTag();
    }

    @Info("Sets the item stack's `CompoundTag`")
    public ItemStackProviderJS setTag(CompoundTag tag) {
        stack.setTag(tag);
        return this;
    }

    @Info("Merges the provided CompoundTag into item stack's CompoundTag")
    public ItemStackProviderJS mergeTag(CompoundTag tag) {
        stack.getOrCreateTag().merge(tag);
        return this;
    }

    @Info("Returns the ISP as an `ItemStack` with all of its modifiers applied, will error if any of the modifiers are dependent on the input stack")
    public ItemStack toStack() {
        final ItemStackProvider provider = asCanonClass();
        if (provider.dependsOnInput()) {
            throw new IllegalArgumentException("Tried to convert an ISP into a regular ItemStack while it was dependent on an input stack!");
        }
        return provider.getEmptyStack();
    }

    @Info("Returns the ISP as an `ItemStack` with all of its modifier applied, requires an input stack for any modifiers that require inputs")
    public ItemStack toStack(ItemStack input) {
        return asCanonClass().getSingleStack(input);
    }

    public static ItemStackProviderJS of(ItemStack stack, @Nullable Object b) {
        return new ItemStackProviderJS(stack, parseModifierList(ListJS.orEmpty(b)));
    }

    private static JsonArray parseModifierList(List<?> list) {
        var modifiers = new JsonArray();
        for (var element : list) {
            if (element instanceof CharSequence) {
                modifiers.add(ResourceUtils.buildJson(j -> j.addProperty("type", element.toString())));
            } else if (element instanceof JsonObject obj) {
                modifiers.add(obj);
            } else if (element instanceof JsonPrimitive prim) {
                modifiers.add(ResourceUtils.buildJson(j -> j.add("type", prim)));
            } else {
                final JsonObject obj = MapJS.json(element);
                if (obj != null) {
                    modifiers.add(obj);
                } else {
                    ConsoleJS.SERVER.error("Could not parse object [%s] into json modifier".formatted(element));
                }
            }
        }
        return modifiers;
    }

    @Info("Adds a simple modifier to the ISP with the type defined by the provided string")
    public ItemStackProviderJS simpleModifier(String s) {
        return jsonModifier(s, j -> {});
    }

    @Info("Adds the provided JsonObject to the modifier list")
    public ItemStackProviderJS jsonModifier(JsonObject json) {
        modifiers.add(json);
        return this;
    }

    public ItemStackProviderJS jsonModifier(String type, Consumer<JsonObject> json) {
        final JsonObject obj = ResourceUtils.buildJson(json);
        obj.addProperty("type", type);
        return jsonModifier(obj);
    }

    public ItemStackProviderJS trait(boolean isAddingTrait, String foodTrait) {
        return jsonModifier(isAddingTrait ? "tfc:add_trait" : "tfc:remove_trait", j -> j.addProperty("trait", foodTrait));
    }

    @Info("Adds a 'tfc:dye_leather' modifier to the ISP with the provided color")
    public ItemStackProviderJS dyeLeather(DyeColor color) {
        return jsonModifier("tfc:dye_leather", j -> j.addProperty("color", color.getSerializedName()));
    }

    @Info("Returns the json representation of the ISP's item stack")
    public JsonObject getJsonStack() {
        return IngredientHelpers.itemStackToJson(stack);
    }

    @Override
    public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ItemStackProviderJS provider) {
            return provider.stack().equals(this.stack()) && provider.modifiers.equals(this.modifiers);
        }
        return false;
    }

    @Info("Returns a list of JsonObjects consisting of the applied modifiers which match the requested type")
    @Generics(JsonObject.class)
    public List<JsonObject> getModifiersOfType(String type) {
        return modifiers.asList().stream()
                .map(JsonElement::getAsJsonObject)
                .filter(obj -> Objects.equals(obj.get("type").getAsString(), type))
                .toList();
    }

    // This assumes if neither element is defined the json is an item stack
    public static ItemStackProviderJS fromJson(JsonObject json) {
        if (!json.has("stack") && !json.has("modifiers")) {
            return new ItemStackProviderJS(ItemStackJS.of(json), new JsonArray());
        }
        var stack = json.has("stack") ? ItemStackJS.of(json.get("stack")) : ItemStack.EMPTY;
        var modifiers = json.has("modifiers") ? parseModifierList(json.get("modifiers").getAsJsonArray().asList()) : new JsonArray();
        return new ItemStackProviderJS(stack, modifiers);
    }

    @Info("Returns the json representation of this ISP")
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

    @Info("Returns an object of the canon ItemStackProvider class matching the ISP this ItemStackProviderJS represents")
    public ItemStackProvider asCanonClass() {
        return ItemStackProvider.fromJson(toJson());
    }

    @Info("Returns true if the ISP depends on a recipe's input")
    public boolean dependsOnInput() {
        for (JsonElement element : modifiers) {
            if (ItemStackModifiers.fromJson(element).dependsOnInput()) {
                return true;
            }
        }
        return false;
    }

    @Info("Returns a copy of the ISP")
    public ItemStackProviderJS copy() {
        return new ItemStackProviderJS(stack.copy(), modifiers.deepCopy());
    }

    @Override
    public String toString() {
        return "TFC.itemStackProvider." + (isEmpty() ? "empty()" : "of(" + IngredientHelpers.stringifyItemStack(stack()) + ", " + modifiers() + ")");
    }

    @Info(value = "Adds a 'tfc:add_heat' modifier to the ISP", params = @Param(name = "temperature", value = "The °C to add to the item"))
    public ItemStackProviderJS addHeat(float temperature) {
        return jsonModifier("tfc:add_heat", j -> j.addProperty("temperature", temperature));
    }

    @Info("Adds a 'tfc:add_powder' modifier to the ISP")
    public ItemStackProviderJS addPowder() {
        return simpleModifier("tfc:add_powder");
    }

    @Info(value = "Adds a 'tfc:add_trait' modifier to the ISP", params = @Param(name = "trait", value = "The food trait to be added"))
    public ItemStackProviderJS addTrait(String trait) {
        return trait(true, trait);
    }

    @Info(value = "Adds a 'tfc:remove_trait' modifier to the ISP", params = @Param(name = "trait", value = "The food trait to be removed"))
    public ItemStackProviderJS removeTrait(String trait) {
        return trait(false, trait);
    }

    @Info("Adds a 'tfc:add_glass' modifier to the ISP, used as part of glassworking recipes")
    public ItemStackProviderJS addGlass() {
        return simpleModifier("tfc:add_glass");
    }

    @Info("Adds a 'tfc:copy_food' modifier to the ISP")
    public ItemStackProviderJS copyFood() {
        return simpleModifier("tfc:copy_food");
    }

    @Info("Adds a 'tfc:copy_forging_bonus' modifier to the ISP")
    public ItemStackProviderJS copyForgingBonus() {
        return simpleModifier("tfc:copy_forging_bonus");
    }

    @Info("Adds a 'tfc:copy_heat' modifier to the ISP")
    public ItemStackProviderJS copyHeat() {
        return simpleModifier("tfc:copy_heat");
    }

    @Info("Adds a 'tfc:copy_input' modifier to the ISP")
    public ItemStackProviderJS copyInput() {
        return simpleModifier("tfc:copy_input");
    }

    @Info("Adds a 'tfc:empty_bowl' modifier to the ISP. This is supported by soup items")
    public ItemStackProviderJS emptyBowl() {
        return simpleModifier("tfc:empty_bowl");
    }

    @Info("Adds a 'tfc:reset_food' modifier to the ISP")
    public ItemStackProviderJS resetFood() {
        return simpleModifier("tfc:reset_food");
    }

    @Info("Adds a 'tfc:copy_oldest_food' modifier to the ISP")
    public ItemStackProviderJS copyOldestFood() {
        return simpleModifier("tfc:copy_oldest_food");
    }

    @Info("Adds a 'tfc:add_bait_to_rod' modifier to the ISP")
    public ItemStackProviderJS addBait() {
        return simpleModifier("tfc:add_bait_to_rod");
    }

    @Info(value = "Adds a 'tfc:meal' modifier to the ISP", params = {
            @Param(name = "food", value = "The base food data values for the meal modifier"),
            @Param(name = "portions", value = "The portion data values for the meal modifier")
    })
    @Generics({BuildFoodItemData.class, BuildPortionData.class})
    public ItemStackProviderJS meal(Consumer<BuildFoodItemData> food, Consumer<BuildPortionData>[] portions) {
        final JsonObject obj = mealBase(food);
        JsonArray portionArray = new JsonArray(portions.length);
        for (Consumer<BuildPortionData> portion : portions) {
            final BuildPortionData portionData = new BuildPortionData();
            portion.accept(portionData);
            portionArray.add(portionData.toJson());
        }
        obj.add("portions", portionArray);
        return jsonModifier(obj);
    }

    @Info(value = "Adds a 'tfc:meal' modifier to the ISP", params = @Param(name = "food", value = "The base food data values for the meal modifier"))
    @Generics(BuildFoodItemData.class)
    public ItemStackProviderJS meal(Consumer<BuildFoodItemData> food) {
        return jsonModifier(mealBase(food));
    }

    private JsonObject mealBase(Consumer<BuildFoodItemData> food) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "tfc:meal");
        obj.add("food", BuildFoodItemData.create(null, food));
        return obj;
    }
}
