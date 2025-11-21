package io.github.notenoughmail.kubejstfc.implementation.extensions;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.kubejs.util.WithCodec;
import dev.latvian.mods.rhino.Context;
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

@ReturnsSelf(ItemStackProvider.class)
@RemapPrefixForJS(KubeJSTFC.MIXIN_PREFIX)
public interface ISPExtension extends WithCodec {

    @Override
    default Codec<?> getCodec(Context cx) {
        return ItemStackProvider.CODEC;
    }

    @Info("The modifiers that will be applied to the stack, may be immutable")
    default List<ItemStackModifier> modifiers() {
        throw new AssertionError("Not injected");
    }
    @Info("The base stack of the ISP, may be empty")
    default ItemStack stack() {
        throw new AssertionError("Not injected");
    }

    private ItemStackProvider kubejs_tfc$self() {
        return Cast.to(this);
    }

    @Info("Copies this ItemStackModifier with a mutable modifier list")
    @ReturnsSelf(copy = true)
    default ItemStackProvider kubejs_tfc$copy() {
        return new ItemStackProvider(stack().copy(), new ArrayList<>(modifiers()));
    }

    @Info("Copies this ItemStackModifier with an immutable modifier list")
    @ReturnsSelf(copy = true)
    default ItemStackProvider kubejs_tfc$copyImmutable() {
        return new ItemStackProvider(stack().copy(), List.copyOf(modifiers()));
    }

    @Info("Adds the provided modifier")
    default ItemStackProvider kubejs_tfc$addModifier(ItemStackModifier modifier) {
        modifiers().add(modifier);
        return kubejs_tfc$self();
    }

    @Info("Adds the provided json-represented modifier")
    default ItemStackProvider kubejs_tfc$addJsonModifier(JsonObject json) {
        return kubejs_tfc$addModifier(ItemStackModifier.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst());
    }

    @Info("Adds a modifier of the given type")
    default ItemStackProvider kubejs_tfc$addSimpleModifier(KubeResourceLocation type) {
        return kubejs_tfc$addJsonModifier(Assistant.json(j -> j.addProperty("type", type.toString())));
    }

    @Info("Adds a 1tfc:copy_input` modifier")
    default ItemStackProvider kubejs_tfc$copyInputStack() {
        return kubejs_tfc$addModifier(CopyInputModifier.INSTANCE);
    }

    @Info("Adds a `tfc:copy_food` modifier")
    default ItemStackProvider kubejs_tfc$copyFood() {
        return kubejs_tfc$addModifier(CopyFoodModifier.INSTANCE);
    }

    @Info("Adds a `tfc:copy_oldest_food` modifier")
    default ItemStackProvider kubejs_tfc$copyOldestFood() {
        return kubejs_tfc$addModifier(CopyOldestFoodModifier.INSTANCE);
    }

    @Info("Adds a `tfc:copy_heat` modifier")
    default ItemStackProvider kubejs_tfc$copyheat() {
        return kubejs_tfc$addModifier(CopyHeatModifier.INSTANCE);
    }

    @Info("Adds a `tfc:copy_forging_bonus` modifier")
    default ItemStackProvider kubejs_tfc$copyForgingBonus() {
        return kubejs_tfc$addModifier(CopyForgingBonusModifier.INSTANCE);
    }

    @Info("Adds a `tfc:reset_food` modifier")
    default ItemStackProvider kubejs_tfc$resetFood() {
        return kubejs_tfc$addModifier(ResetFoodModifier.INSTANCE);
    }

    @Info("Adds a `tfc:empty_bowl` modifier")
    default ItemStackProvider kubejs_tfc$emptyBowl() {
        return kubejs_tfc$addModifier(EmptyBowlModifier.INSTANCE);
    }

    @Info("Adds a `tfc:add_bait_to_rod` modifier")
    default ItemStackProvider kubejs_tfc$addBaitToRod() {
        return kubejs_tfc$addModifier(AddBaitToRodModifier.INSTANCE);
    }

    @Info("Adds a `tfc:add_glass` modifier")
    default ItemStackProvider kubejs_tfc$addGlass() {
        return kubejs_tfc$addModifier(AddGlassModifier.INSTANCE);
    }

    @Info("Adds a `tfc:add_powder` modifier")
    default ItemStackProvider kubejs_tfc$addPowder() {
        return kubejs_tfc$addModifier(AddPowderModifier.INSTANCE);
    }

    @Info("Adds a `tfc:crafting_remainder` modifier")
    default ItemStackProvider kubejs_tfc$craftingRemainder() {
        return kubejs_tfc$addModifier(CraftingRemainderModifier.INSTANCE);
    }

    @Info("Adds a `tfc:damage_crafting_remainder` modifier")
    default ItemStackProvider kubejs_tfc$damageCraftingRemainder() {
        return kubejs_tfc$addModifier(DamageCraftingRemainderModifier.INSTANCE);
    }

    @Info("Adds a `tfc:add_trait` modifier")
    default ItemStackProvider kubejs_tfc$addTrait(Holder<FoodTrait> trait) {
        return kubejs_tfc$addModifier(new AddTraitModifier(trait));
    }

    @Info("Adds a `tfc:remove_trait` modifier")
    default ItemStackProvider kubejs_tfc$removeTrait(Holder<FoodTrait> trait) {
        return kubejs_tfc$addModifier(new RemoveTraitModifier(trait));
    }

    @Info("Adds a `tfc:add_heat` modifier")
    default ItemStackProvider kubejs_tfc$addHeat(float temperature) {
        return kubejs_tfc$addModifier(new AddHeatModifier(temperature));
    }

    @Info("Adds a `tfc:dye_leather` modifier")
    default ItemStackProvider kubejs_tfc$dyeLeather(DyeColor color) {
        return kubejs_tfc$addModifier(new DyeLeatherModifier(color));
    }

    @Info("Adds a `tfc:remove_dye` modifier")
    default ItemStackProvider kubejs_tfc$removeDye() {
        return kubejs_tfc$addModifier(RemoveDyeModifier.INSTANCE);
    }

    @Info("Adds a `tfc:meal` modifier")
    default ItemStackProvider kubejs_tfc$meal(FoodData food, MealModifier.MealPortion... portions) {
        return kubejs_tfc$addModifier(new MealModifier(food, List.of(portions)));
    }

    @Info("Adds a `tfc:extra_products` modifier")
    default ItemStackProvider kubejs_tfc$extraProduct(ItemStack stack) {
        return kubejs_tfc$addModifier(new ExtraProductModifier(stack));
    }

    @Info("Adds a `tfc:chance` modifier")
    default ItemStackProvider kubejs_tfc$chance(float chance) {
        return kubejs_tfc$addModifier(new ChanceModifier(chance));
    }
}
