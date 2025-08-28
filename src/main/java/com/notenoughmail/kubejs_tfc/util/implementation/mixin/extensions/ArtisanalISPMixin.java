package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IArtisanalISPExtensions;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import net.mrhitech.artisanal.Artisanal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@IfPresent(Artisanal.MOD_ID)
@Mixin(value = ItemStackProviderJS.class, remap = false)
public abstract class ArtisanalISPMixin implements IArtisanalISPExtensions {

    @Shadow(remap = false)
    public abstract ItemStackProviderJS simpleModifier(String s);

    @Shadow(remap = false)
    public abstract ItemStackProviderJS jsonModifier(String type, Consumer<JsonObject> json);

    @Override
    public ItemStackProviderJS kubejs_tfc$CapHeat(float max) {
        return jsonModifier("artisanal:cap_heat", j -> j.addProperty("max_temp", max));
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$CopyDynamicFood() {
        return simpleModifier("artisanal:copy_dynamic_food");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$CopyDynamicFoodNeverExpires() {
        return simpleModifier("artisanal:copy_dynamic_food_never_expires");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$EmptyBowl() {
        return simpleModifier("artisanal:empty_bowl");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$ExtractCannedFood() {
        return simpleModifier("artisanal:extract_canned_food");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$HomogenousIngredients() {
        return simpleModifier("artisanal:homogenous_ingredients");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$InheritDecay(float decayModifier) {
        return jsonModifier("artisanal:inherit_decay", j -> j.addProperty("decay_multiplier", decayModifier));
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$ModifyFluid(FluidStackJS fluid) {
        return jsonModifier("artisanal:modify_fluid", j -> j.add("fluid", fluid.toJson()));
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$OnlyIfGenericAnimalFat() {
        return simpleModifier("artisanal:only_if_generic_animal_fat");
    }

    @Override
    public ItemStackProviderJS kubejs_tfc$RemoveButter() {
        return simpleModifier("artisanal:remove_butter");
    }
}
