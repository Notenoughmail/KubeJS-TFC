package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.RemapForJS;

public interface IArtisanalISPExtensions {

    @Info(value = "Adds a 'artisanal:cap_heat' modifier to the stack", params = @Param(name = "max", value = "The max heat the stack may be"))
    @RemapForJS("artisanalCapHeat")
    ItemStackProviderJS kubejs_tfc$CapHeat(float max);

    @Info("Adds a 'artisanal:copy_dynamic_food' modifier to the stack")
    @RemapForJS("artisanalCopyDynamicFood")
    ItemStackProviderJS kubejs_tfc$CopyDynamicFood();

    @Info("Adds a 'artisanal:copy_dynamic_food_never_expires' modifier to the stack")
    @RemapForJS("artisanalCopyDynamicFoodNeverExpires")
    ItemStackProviderJS kubejs_tfc$CopyDynamicFoodNeverExpires();

    @Info("Adds a 'artisanal:empty_bowl' modifier to the stack")
    @RemapForJS("artisanalEmptyBowl")
    ItemStackProviderJS kubejs_tfc$EmptyBowl();

    @Info("Adds a 'artisanal:extract_canned_food' modifier to the stack")
    @RemapForJS("artisanalExtractCannedFood")
    ItemStackProviderJS kubejs_tfc$ExtractCannedFood();

    @Info("Adds a 'artisanal:homogeneous_ingredients' modifier to the stack")
    @RemapForJS("artisanalHomogenousIngredients")
    ItemStackProviderJS kubejs_tfc$HomogenousIngredients();

    @Info(value = "Adds a 'artisanal:inherit_decay' modifier to the stack", params = @Param(name = "decayModifier", value = "A multiplier on the decay to apply when the decay in inherited"))
    @RemapForJS("artisanalInheritDecay")
    ItemStackProviderJS kubejs_tfc$InheritDecay(float decayModifier);

    @Info(value = "Adds a 'artisanal:modify_fluid' modifier to the stack", params = @Param(name = "fluid", value = "The fluid to insert into the item"))
    @RemapForJS("artisanalModifyFluid")
    ItemStackProviderJS kubejs_tfc$ModifyFluid(FluidStackJS fluid);

    @Info("Adds a 'artisanal:only_if_generic_animal_fat' modifier to the stack")
    @RemapForJS("artisanalOnlyIfGenericAnimalFat")
    ItemStackProviderJS kubejs_tfc$OnlyIfGenericAnimalFat();

    @Info("Adds a 'artisanal:remove_butter' modifier to the the stack")
    @RemapForJS("artisanalRemoveButter")
    ItemStackProviderJS kubejs_tfc$RemoveButter();
}
