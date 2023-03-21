package com.notenoughmail.kubejs_tfc.util.implementation;

public class ItemStackProviderWrapper {

    public static ItemStackProviderJS of(Object o) {
        return ItemStackProviderJS.of(o);
    }

    public static ItemStackProviderJS of(Object o, Object b) {
        return ItemStackProviderJS.of(o, b);
    }

    // Needed/Recommended in order to use convenience methods
    public static ItemStackProviderJS empty() {
        return ItemStackProviderJS.EMPTY;
    }

    public static ItemStackProviderJS addHeat(ItemStackProviderJS js, int i) {
        return js.addHeat(i);
    }

    public static ItemStackProviderJS addTrait(ItemStackProviderJS js, String s) {
        return js.trait(true, s);
    }

    public static ItemStackProviderJS removeTrait(ItemStackProviderJS js, String s) {
        return js.trait(false, s);
    }
    
    public static ItemStackProviderJS simpleModifier(ItemStackProviderJS js, String s) {
        return js.simpleProperty(s);
    }

    public static ItemStackProviderJS copyFood(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:copy_food");
    }

    public static ItemStackProviderJS copyForgingBonus(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:copy_forging_bonus");
    }

    public static ItemStackProviderJS copyHeat(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:copy_heat");
    }

    public static ItemStackProviderJS copyInput(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:copy_input");
    }

    public static ItemStackProviderJS emptyBowl(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:empty_bowl");
    }

    public static ItemStackProviderJS resetFood(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:reset_food");
    }

    // These modifiers are only usable in crafting recipes which support item stack providers

    public static ItemStackProviderJS addBait(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:add_bait_to_rod");
    }

    public static ItemStackProviderJS sandwich(ItemStackProviderJS js) {
        return js.simpleProperty("tfc:sandwich");
    }
}
