package io.github.notenoughmail.kubejstfc.implementation.bindings;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.dries007.tfc.util.data.*;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistryWood;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public enum DataBindings {
    INSTANCE;

    @Info("Utilities for using and interacting with TFC's food system")
    public final FoodBindings food = FoodBindings.INSTANCE;

    @Info("Utilities for using and interacting with TFC's support and collapse systems")
    public final SupportBindings support = SupportBindings.INSTANCE;

    @Info("A map connecting Heats to their name")
    public Map<String, Heat> getHeatLevels() {
        return HEAT_LEVELS.get();
    }
    private static final Supplier<Map<String, Heat>> HEAT_LEVELS = Suppliers.memoize(() ->
            Util.make(new ImmutableMap.Builder<String, Heat>(), m -> {
                for (Heat h : Heat.values()) m.put(h.name(), h);
            }).build());

    @Info("A map connecting a wood to its pseudo official id")
    public Map<ResourceLocation, RegistryWood> getWoods() {
        return WOOD.get();
    }
    private static final Supplier<Map<ResourceLocation, RegistryWood>> WOOD = Suppliers.memoize(KubeJSTFC::getWoods);

    @Info("A map connecting a metal to its pseudo official id")
    public Map<ResourceLocation, RegistryMetal> getMetals() {
        return METALS.get();
    }
    private static final Supplier<Map<ResourceLocation, RegistryMetal>> METALS = Suppliers.memoize(KubeJSTFC::getMetals);

    @Info("A map connecting a rock to its pseudo official id")
    public Map<ResourceLocation, RegistryRock> getRocks() {
        return ROCKS.get();
    }
    private static final Supplier<Map<ResourceLocation, RegistryRock>> ROCKS = Suppliers.memoize(KubeJSTFC::getRocks);

    @Info("Gets TFC's nutrition and other attached data associated with the player")
    public IPlayerInfo getPlayerInfo(Player player) {
        return IPlayerInfo.get(player);
    }

    @Info("Gets TFC's ChunkData at the given position")
    public ChunkData getChunkData(LevelReader level, BlockPos pos) {
        return ChunkData.get(level, pos);
    }

    @Info("Gets TFC's ChunkData fro the given chunk")
    public ChunkData getChunkData(ChunkAccess chunk) {
        return ChunkData.get(chunk);
    }

    @Info("Gets the heat capability of the stack if present")
    @Nullable
    public IHeat getHeat(ItemStack stack) {
        return HeatCapability.get(stack);
    }

    @Info("If the stack has a heat capability")
    public boolean hasHeat(ItemStack stack) {
        return HeatCapability.has(stack);
    }

    @Info("Gets the heat level at the temperature (°C), or null if <= 0")
    @Nullable
    public Heat getHeatLevel(float temperature) {
        return Heat.getHeat(temperature);
    }

    @Info("Gets the size of the stack")
    public Size getSize(ItemStack stack) {
        return ItemSizeManager.get(stack).getSize(stack);
    }

    @Info("Gets the weight of the stack")
    public Weight getWeight(ItemStack stack) {
        return ItemSizeManager.get(stack).getWeight(stack);
    }

    @Info("Gets the current hydration at the position")
    public int getFarmlandHydration(Level level, BlockPos pos) {
        return FarmlandBlock.getHydration(level, pos);
    }

    @Info("Gets the hydration at the position at the given calendar tick")
    public int getFarmlandHydration(Level level, BlockPos pos, long calendarTick) {
        return FarmlandBlock.getHydration(level, pos, calendarTick);
    }

    @Info("Gets the fluid heat of the fluid")
    @Nullable
    public FluidHeat getFluidHeat(Fluid fluid) {
        return FluidHeat.get(fluid);
    }

    @Info("Gets the lamp fuel that matches the given fluid and block")
    @Nullable
    public LampFuel getLampFuel(Fluid fluid, BlockState state) {
        return LampFuel.get(fluid, state);
    }

    @Info("Gets the drinkable definition for the fluid")
    @Nullable
    public Drinkable getDrinkable(Fluid fluid) {
        return Drinkable.get(fluid);
    }

    @Info("Gets the fertilizer of the stack")
    @Nullable
    public Fertilizer getFertilizer(ItemStack stack) {
        return Fertilizer.get(stack);
    }

    @Info("Gets the fuels of the stack")
    @Nullable
    public Fuel getFuel(ItemStack stack) {
        return Fuel.get(stack);
    }

    @Info("Gets the deposit of the stack")
    @Nullable
    public Deposit getDeposit(ItemStack stack) {
        return Deposit.get(stack);
    }
}
