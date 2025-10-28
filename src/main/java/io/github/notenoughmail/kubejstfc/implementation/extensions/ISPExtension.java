package io.github.notenoughmail.kubejstfc.implementation.extensions;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.recipes.outputs.*;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

// TODO: 2.0.0 | Document
@ReturnsSelf(ItemStackProvider.class)
@RemapPrefixForJS(KubeJSTFC.MIXIN_PREFIX)
public interface ISPExtension {

    List<ItemStackModifier> modifiers();

    private ItemStackProvider kubejs_tfc$self() {
        return Cast.to(this);
    }

    @ReturnsSelf(copy = true)
    default ItemStackProvider kubejs_tfc$copy() {
        return new ItemStackProvider(kubejs_tfc$self().stack().copy(), new ArrayList<>(modifiers()));
    }

    @ReturnsSelf(copy = true)
    default ItemStackProvider kubejs_tfc$copyImmutable() {
        return new ItemStackProvider(kubejs_tfc$self().stack().copy(), List.copyOf(modifiers()));
    }

    default ItemStackProvider kubejs_tfc$addModifier(ItemStackModifier modifier) {
        modifiers().add(modifier);
        return kubejs_tfc$self();
    }

    default ItemStackProvider kubejs_tfc$addJsonModifier(JsonObject json) {
        return kubejs_tfc$addModifier(ItemStackModifier.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst());
    }

    default ItemStackProvider kubejs_tfc$addSimpleModifier(String type) {
        return kubejs_tfc$addJsonModifier(Assistant.json(j -> j.addProperty("type", type)));
    }

    default ItemStackProvider kubejs_tfc$copyInputStack() {
        return kubejs_tfc$addModifier(CopyInputModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$copyFood() {
        return kubejs_tfc$addModifier(CopyFoodModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$copyOldestFood() {
        return kubejs_tfc$addModifier(CopyOldestFoodModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$copyheat() {
        return kubejs_tfc$addModifier(CopyHeatModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$copyForgingBonus() {
        return kubejs_tfc$addModifier(CopyForgingBonusModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$resetFood() {
        return kubejs_tfc$addModifier(ResetFoodModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$emptyBowl() {
        return kubejs_tfc$addModifier(EmptyBowlModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$addBaitToRod() {
        return kubejs_tfc$addModifier(AddBaitToRodModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$addGlass() {
        return kubejs_tfc$addModifier(AddGlassModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$addPowder() {
        return kubejs_tfc$addModifier(AddPowderModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$craftingRemainder() {
        return kubejs_tfc$addModifier(CraftingRemainderModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$damageCraftingRemainder() {
        return kubejs_tfc$addModifier(DamageCraftingRemainderModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$addTrait(Holder<FoodTrait> trait) {
        return kubejs_tfc$addModifier(new AddTraitModifier(trait));
    }

    default ItemStackProvider kubejs_tfc$removeTrait(Holder<FoodTrait> trait) {
        return kubejs_tfc$addModifier(new RemoveTraitModifier(trait));
    }

    default ItemStackProvider kubejs_tfc$addHeat(float temperature) {
        return kubejs_tfc$addModifier(new AddHeatModifier(temperature));
    }

    default ItemStackProvider kubejs_tfc$dyeLeather(DyeColor color) {
        return kubejs_tfc$addModifier(new DyeLeatherModifier(color));
    }

    default ItemStackProvider kubejs_tfc$removeDye() {
        return kubejs_tfc$addModifier(RemoveDyeModifier.INSTANCE);
    }

    default ItemStackProvider kubejs_tfc$meal(FoodData food, MealModifier.MealPortion... portions) {
        return kubejs_tfc$addModifier(new MealModifier(food, List.of(portions)));
    }

    default ItemStackProvider kubejs_tfc$extraProduct(ItemStack stack) {
        return kubejs_tfc$addModifier(new ExtraProductModifier(stack));
    }

    default ItemStackProvider kubejs_tfc$chance(float chance) {
        return kubejs_tfc$addModifier(new ChanceModifier(chance));
    }
}
