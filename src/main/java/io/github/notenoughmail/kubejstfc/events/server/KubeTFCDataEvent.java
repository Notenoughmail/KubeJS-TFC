package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.Context;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
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
        add(new EntityDamageResistance(entity, resistance), EntityDamageResistance.MANAGER, id, r -> r.entity().location().toString().replace(':', '/'));
    }

    public void entityDamageResistance(TagKey<EntityType<?>> entity, PhysicalDamage resistance) {
        entityDamageResistance(entity, resistance, null);
    }

    public void itemDamageResistance(Ingredient ingredient, PhysicalDamage resistance, @Nullable KubeResourceLocation id) {
        add(new ItemDamageResistance(ingredient, resistance), ItemDamageResistance.MANAGER, id);
    }

    public void itemDamageResistance(Ingredient ingredient, PhysicalDamage resistance) {
        itemDamageResistance(ingredient, resistance, null);
    }

    public void drinkable(Context ctx, Drinkable drinkable, @Nullable KubeResourceLocation id) {
        Assistant.notNull(drinkable.ingredient(), "drinkable.ingredient", ctx);
        add(drinkable, Drinkable.MANAGER, id);
    }

    public void drinkable(Context ctx, Drinkable drinkable) {
        drinkable(ctx, drinkable, null);
    }

    public void fertilizer(Context ctx, Fertilizer fertilizer, @Nullable KubeResourceLocation id) {
        Assistant.notNull(fertilizer.ingredient(), "fertilizer.ingredient", ctx);
        add(fertilizer, Fertilizer.MANAGER, id);
    }

    public void fertilizer(Context ctx, Fertilizer fertilizer) {
        fertilizer(ctx, fertilizer, null);
    }

    public void fuel(Context ctx, Fuel fuel, @Nullable KubeResourceLocation id) {
        Assistant.notNull(fuel.ingredient(), "fuel.ingredient", ctx);
        add(fuel, Fuel.MANAGER, id);
    }

    public void fuel(Context ctx, Fuel fuel) {
        fuel(ctx, fuel, null);
    }

    public void fluidHeat(Context ctx, FluidHeat fluidHeat, @Nullable KubeResourceLocation id) {
        Assistant.notNull(fluidHeat.fluid(), "fluidHeat.fluid", ctx);
        add(fluidHeat, FluidHeat.MANAGER, id, f -> f.fluid().toString().replace(":", "/"));
    }

    public void fluidHeat(Context ctx, FluidHeat fluidHeat) {
        fluidHeat(ctx, fluidHeat, null);
    }

    public void knappingType(Context ctx, KnappingType knappingType, KubeResourceLocation id) {
        Assistant.notNull(knappingType.inputItem(), "knappingType.inputItem", ctx);
        Assistant.notNull(knappingType.clickSound(), "knappingType.clickSound", ctx);
        Assistant.notNull(knappingType.icon(), "knappingType.icon", ctx);
        add(knappingType, KnappingType.MANAGER, id);
    }

    public void support(BlockIngredient ingredient, Support.SupportRange range, @Nullable KubeResourceLocation id) {
        add(new Support(ingredient, range.up(), range.down(), range.horizontal()), Support.MANAGER, id);
    }

    public void support(BlockIngredient ingredient, Support.SupportRange range) {
        support(ingredient, range, null);
    }

    public void itemSize(Context ctx, ItemSizeDefinition itemSize, @Nullable KubeResourceLocation id) {
        Assistant.notNull(itemSize.ingredient(), "itemSize.ingredient", ctx);
        add(itemSize, ItemSizeManager.MANAGER, id);
    }

    public void itemSize(Context ctx, ItemSizeDefinition itemSize) {
        itemSize(ctx, itemSize, null);
    }

    public void fauna(Consumer<Fauna.Builder> builder, KubeResourceLocation id) {
        add(Util.make(new Fauna.Builder(), builder).build(), Fauna.MANAGER, id);
    }

    public void climateRange(ClimateRange range, KubeResourceLocation id) {
        add(range, ClimateRange.MANAGER, id);
    }

    public void lampFuel(Context ctx, LampFuel lampFuel, @Nullable KubeResourceLocation id) {
        Assistant.notNull(lampFuel.fluid(), "lampFuel.fluid", ctx);
        Assistant.notNull(lampFuel.lamps(), "lampFuel.lamps", ctx);
        add(lampFuel, LampFuel.MANAGER, id);
    }

    public void lampFuel(Context ctx, LampFuel lampFuel) {
        lampFuel(ctx, lampFuel, null);
    }

    public void deposit(Ingredient ingredient, ResourceKey<LootTable> lootTable, List<ResourceLocation> modelStages, @Nullable KubeResourceLocation id) {
        add(new Deposit(ingredient, lootTable, modelStages), Deposit.MANAGER, id);
    }

    public void deposit(Ingredient ingredient, ResourceKey<LootTable> lootTable, List<ResourceLocation> modelStages) {
        deposit(ingredient, lootTable, modelStages, null);
    }

    public void heat(Context ctx, HeatDefinition heat, @Nullable KubeResourceLocation id) {
        Assistant.notNull(heat.ingredient(), "heat.ingredient", ctx);
        add(heat, HeatCapability.MANAGER, id);
    }

    public void heat(Context ctx, HeatDefinition heat) {
        heat(ctx, heat, null);
    }

    public void food(Context ctx, FoodDefinition food, @Nullable KubeResourceLocation id) {
        Assistant.notNull(food.ingredient(), "food.ingredient", ctx);
        add(food, FoodCapability.MANAGER, id);
    }

    public void food(Context ctx, FoodDefinition food) {
        food(ctx, food, null);
    }
}
