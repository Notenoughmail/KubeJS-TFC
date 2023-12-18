package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.common.capabilities.heat.*;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum MiscBindings {
    INSTANCE;

    @Info(value = "A map associating the name of a rock to its RegistryRock")
    @Generics(value = {String.class, RegistryRock.class})
    public final Map<String, RegistryRock> rock = new HashMap<>(20);
    @Info(value = "A map associating the name of a wood to its RegistryWood")
    @Generics(value = {String.class, RegistryWood.class})
    public final Map<String, RegistryWood> wood = new HashMap<>(19);

    @Nullable
    @Info(value = "Returns the stack's IHeat capability if present, else null")
    public IHeat getHeat(ItemStack stack) {
        return HeatCapability.get(stack);
    }

    @Info(value = "returns true if the stack does have an IHeat capability")
    public boolean hasHeat(ItemStack stack) {
        return HeatCapability.has(stack);
    }

    @Info(value = "A map associating the name of a heat level to its Heat")
    @Generics(value = {String.class, Heat.class})
    public final Map<String, Heat> heatLevels = new HashMap<>(11);

    @Nullable
    @Info(value = "Returns the stack's IFood capability if present, else null")
    public IFood getFood(ItemStack stack) {
        return FoodCapability.get(stack);
    }

    @Info(value = "Returns true if the stack does have an IFood capability")
    public boolean hasFood(ItemStack stack) {
        return FoodCapability.has(stack);
    }

    @Info(value = "Makes the provided stack rotten if possible and returns it")
    public ItemStack setRotten(ItemStack stack) {
        return FoodCapability.setRotten(stack);
    }

    @Info(value = "Sets the provided stack to never expires if possible")
    public void setNeverExpires(ItemStack stack) {
        FoodCapability.setNeverExpires(stack);
    }

    @Nullable
    @Info(value = "Returns the FoodTrait with the given registry name if it exists, else null")
    public FoodTrait getFoodTrait(ResourceLocation id) {
        return FoodTrait.getTrait(id);
    }

    @Info(value = "Returns the registry name of the given food trait")
    public ResourceLocation getFoodTraitId(FoodTrait trait) {
        return FoodTrait.getId(trait);
    }

    @Info(value = "Returns the Size value of the provided stack")
    public Size getSize(ItemStack stack) {
        return ItemSizeManager.get(stack).getSize(stack);
    }

    @Info(value = "Returns the Weight value of the provided stack")
    public Weight getWeight(ItemStack stack) {
        return ItemSizeManager.get(stack).getWeight(stack);
    }
}
