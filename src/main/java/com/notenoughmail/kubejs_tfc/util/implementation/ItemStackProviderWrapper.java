package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildFoodItemData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.BuildPortionData;
import com.notenoughmail.kubejs_tfc.util.implementation.data.ModifyCondition;

import java.util.function.Consumer;

public class ItemStackProviderWrapper {

    public static ItemStackProviderJS of(Object o) {
        return ItemStackProviderJS.of(o);
    }

    public static ItemStackProviderJS of(Object o, Object b) {
        return ItemStackProviderJS.of(o, b);
    }

    // Needed/Recommended in order to use convenience methods
    public static ItemStackProviderJS empty() {
        return new ItemStackProviderJS(null, new JsonArray());
    }

    public static ItemStackProviderJS addHeat(ItemStackProviderJS js, int i) {
        return js.addHeat(i);
    }

    public static ItemStackProviderJS addTrait(ItemStackProviderJS js, String s) {
        return js.addTrait(s);
    }

    public static ItemStackProviderJS removeTrait(ItemStackProviderJS js, String s) {
        return js.removeTrait(s);
    }

    public static ItemStackProviderJS simpleModifier(ItemStackProviderJS js, String s) {
        return js.simpleModifier(s);
    }

    public static ItemStackProviderJS jsonModifier(ItemStackProviderJS js, Object o) {
        return js.jsonModifier(o);
    }

    public static ItemStackProviderJS copyFood(ItemStackProviderJS js) {
        return js.copyFood();
    }

    public static ItemStackProviderJS copyForgingBonus(ItemStackProviderJS js) {
        return js.copyForgingBonus();
    }

    public static ItemStackProviderJS copyHeat(ItemStackProviderJS js) {
        return js.copyHeat();
    }

    public static ItemStackProviderJS copyInput(ItemStackProviderJS js) {
        return js.copyInput();
    }

    public static ItemStackProviderJS emptyBowl(ItemStackProviderJS js) {
        return js.emptyBowl();
    }

    public static ItemStackProviderJS resetFood(ItemStackProviderJS js) {
        return js.resetFood();
    }

    public static ItemStackProviderJS copyOldestFood(ItemStackProviderJS js) {
        return js.copyOldestFood();
    }

    public static ItemStackProviderJS dyeLeather(ItemStackProviderJS js, String s) {
        return js.dyeLeather(s);
    }

    public static ItemStackProviderJS burrito(ItemStackProviderJS js) {
        return js.burrito();
    }

    public static ItemStackProviderJS pie(ItemStackProviderJS js) {
        return js.pie();
    }

    public static ItemStackProviderJS pizza(ItemStackProviderJS js) {
        return js.pizza();
    }

    public static ItemStackProviderJS copyDynamicFood(ItemStackProviderJS js) {
        return js.copyDynamicFood();
    }

    public static ItemStackProviderJS emptyPan(ItemStackProviderJS js) {
        return js.emptyPan();
    }

    public static ItemStackProviderJS conditional(ItemStackProviderJS js, Consumer<ModifyCondition> condition, Consumer<ItemStackProviderJS> modifiers, Consumer<ItemStackProviderJS> elseModifiers) {
        return js.conditional(condition, modifiers, elseModifiers);
    }

    public static ItemStackProviderJS conditional(ItemStackProviderJS js, Consumer<ModifyCondition> condition, Consumer<ItemStackProviderJS> modifiers) {
        return js.conditional(condition, modifiers, null);
    }

    public static ItemStackProviderJS setFoodData(ItemStackProviderJS js, Consumer<BuildFoodItemData> foodData) {
        return js.setFoodData(foodData);
    }

    // These modifiers are only usable in crafting recipes which support item stack providers

    public static ItemStackProviderJS addBait(ItemStackProviderJS js) {
        return js.addBait();
    }

    public static ItemStackProviderJS sandwich(ItemStackProviderJS js) {
        return js.sandwich();
    }

    @SafeVarargs
    public static ItemStackProviderJS meal(ItemStackProviderJS js, Consumer<BuildFoodItemData> food, Consumer<BuildPortionData>... portions) {
        return js.meal(food, portions);
    }
}
