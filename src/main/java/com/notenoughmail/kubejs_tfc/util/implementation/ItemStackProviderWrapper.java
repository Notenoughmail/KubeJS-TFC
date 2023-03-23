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

    // These modifiers are only usable in crafting recipes which support item stack providers

    public static ItemStackProviderJS addBait(ItemStackProviderJS js) {
        return js.addBait();
    }

    public static ItemStackProviderJS sandwich(ItemStackProviderJS js) {
        return js.sandwich();
    }
}
