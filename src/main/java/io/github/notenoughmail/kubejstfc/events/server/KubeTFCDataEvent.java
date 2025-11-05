package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.*;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class KubeTFCDataEvent extends KubeDataEvent {

    public KubeTFCDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    public void entityDamageResistance(EntityDamageResistance resistance, @Nullable KubeResourceLocation id) {
        add(resistance, EntityDamageResistance.CODEC, id, r -> r.entity().location().toString().replace(':', '/'), "tfc/entity_damage_resistance");
    }

    public void entityDamageResistance(EntityDamageResistance resistance) {
        entityDamageResistance(resistance, null);
    }

    public void itemDamageResistance(ItemDamageResistance resistance, @Nullable KubeResourceLocation id) {
        add(resistance, ItemDamageResistance.CODEC, id, "tfc/item_damage_resistance");
    }

    public void itemDamageResistance(ItemDamageResistance resistance) {
        itemDamageResistance(resistance, null);
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

    public void climateRange(Consumer<ClimateRange.Builder> builder, KubeResourceLocation id) {
        add(Util.make(new ClimateRange.Builder(), builder).build(), ClimateRange.CODEC, id, "tfc/climate_range");
    }

    public void lampFuel(LampFuel lampFuel, @Nullable KubeResourceLocation id) {
        add(lampFuel, LampFuel.CODEC, id, "tfc/lamp_fuel");
    }

    public void lampFuel(LampFuel lampFuel) {
        lampFuel(lampFuel, null);
    }

    public void deposit(Deposit deposit, @Nullable KubeResourceLocation id) {
        add(deposit, Deposit.CODEC, id, "tfc/deposit");
    }

    public void deposit(Deposit deposit) {
        deposit(deposit, null);
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
