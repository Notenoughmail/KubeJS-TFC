package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.util.PhysicalDamage;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.*;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class KubeTFCDataEvent extends KubeDataEvent {

    public KubeTFCDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    public void entityDamageResistance(TagKey<EntityType<?>> entity, PhysicalDamage resistance, @Nullable KubeResourceLocation id) {
        add(new EntityDamageResistance(entity ,resistance), EntityDamageResistance.CODEC, id, r -> r.entity().location().toString().replace(':', '/'), "tfc/entity_damage_resistance");
    }

    public void entityDamageResistance(TagKey<EntityType<?>> entity, PhysicalDamage resistance) {
        entityDamageResistance(entity, resistance, null);
    }

    public void itemDamageResistance(Ingredient ingredient, PhysicalDamage resistance, @Nullable KubeResourceLocation id) {
        add(new ItemDamageResistance(ingredient, resistance), ItemDamageResistance.CODEC, id, "tfc/item_damage_resistance");
    }

    public void itemDamageResistance(Ingredient ingredient, PhysicalDamage resistance) {
        itemDamageResistance(ingredient, resistance, null);
    }

    public void drinkable(Drinkable drinkable, @Nullable KubeResourceLocation id) {
        add(drinkable, Drinkable.CODEC, id, "tfc/drinkable");
    }

    public void drinkable(Drinkable drinkable) {
        drinkable(drinkable, null);
    }

    public void fertilizer(Fertilizer fertilizer, @Nullable KubeResourceLocation id) {
        add(fertilizer, Fertilizer.CODEC, id, "tfc/fertilizer");
    }

    public void fertilizer(Fertilizer fertilizer) {
        fertilizer(fertilizer, null);
    }

    public void fuel(Fuel fuel, @Nullable KubeResourceLocation id) {
        add(fuel, Fuel.CODEC, id, "tfc/fuel");
    }

    public void fuel(Fuel fuel) {
        fuel(fuel, null);
    }

    public void fluidHeat(FluidHeat fluidHeat, @Nullable KubeResourceLocation id) {
        add(fluidHeat, FluidHeat.CODEC, id, f -> f.fluid().toString().replace(":", "/"), "tfc/fluid_heat");
    }

    public void fluidHeat(FluidHeat fluidHeat) {
        fluidHeat(fluidHeat, null);
    }

    public void knappingType(KnappingType knappingType, KubeResourceLocation id) {
        add(knappingType, KnappingType.CODEC, id, "tfc/knapping_type");
    }

    public void support(Support support, @Nullable KubeResourceLocation id) {
        add(support, Support.CODEC, id, "tfc/support");
    }

    public void support(Support support) {
        support(support, null);
    }

    public void itemSize(ItemSizeDefinition itemSize, @Nullable KubeResourceLocation id) {
        add(itemSize, ItemSizeDefinition.CODEC, id, "tfc/item_size");
    }

    public void itemSize(ItemSizeDefinition itemSize) {
        itemSize(itemSize, null);
    }

    public void fauna(Consumer<Fauna.Builder> builder, KubeResourceLocation id) {
        add(Util.make(new Fauna.Builder(), builder).build(), Fauna.CODEC, id, "tfc/fauna");
    }

    public void climateRange(ClimateRange range, KubeResourceLocation id) {
        add(range, ClimateRange.CODEC, id, "tfc/climate_range");
    }

    public void lampFuel(LampFuel lampFuel, @Nullable KubeResourceLocation id) {
        add(lampFuel, LampFuel.CODEC, id, "tfc/lamp_fuel");
    }

    public void lampFuel(LampFuel lampFuel) {
        lampFuel(lampFuel, null);
    }

    public void deposit(Ingredient ingredient, ResourceKey<LootTable> lootTable, List<ResourceLocation> modelStages, @Nullable KubeResourceLocation id) {
        add(new Deposit(ingredient, lootTable, modelStages), Deposit.CODEC, id, "tfc/deposit");
    }

    public void deposit(Ingredient ingredient, ResourceKey<LootTable> lootTable, List<ResourceLocation> modelStages) {
        deposit(ingredient, lootTable, modelStages, null);
    }

    public void heat(HeatDefinition heat, @Nullable KubeResourceLocation id) {
        add(heat, HeatDefinition.CODEC, id, "tfc/item_heat");
    }

    public void heat(HeatDefinition heat) {
        heat(heat, null);
    }

    public void food(FoodDefinition food, @Nullable KubeResourceLocation id) {
        add(food, FoodDefinition.CODEC, id, "tfc/food");
    }

    public void food(FoodDefinition food) {
        food(food, null);
    }
}
