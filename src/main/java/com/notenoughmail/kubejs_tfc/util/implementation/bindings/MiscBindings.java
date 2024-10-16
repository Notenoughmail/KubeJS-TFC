package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

import com.google.common.collect.ImmutableMap;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.dries007.tfc.common.recipes.CollapseRecipe;
import net.dries007.tfc.util.*;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.world.TFCChunkGenerator;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.noise.Metaballs2D;
import net.dries007.tfc.world.noise.Metaballs3D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.noise.OpenSimplex3D;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum MiscBindings {
    INSTANCE;

    @Info(value = "A map associating the name of a rock to its `RegistryRock`")
    @Generics(value = {String.class, RegistryRock.class})
    public Map<String, RegistryRock> getRock() { return rock.get(); }
    private static final Supplier<Map<String, RegistryRock>> rock = Lazy.of(KubeJSTFC::registerRocks);

    @Info(value = "A map associating the name of a wood to its `NamedRegistryWood`, includes AFC woods if it is present")
    @Generics(value = {String.class, NamedRegistryWood.class})
    public Map<String, NamedRegistryWood> getWood() { return wood.get(); }
    private static final Supplier<Map<String, NamedRegistryWood>> wood = Lazy.of(KubeJSTFC::registerWoods);

    @Nullable
    @Info(value = "Returns the stack's `IHeat` capability if present, else null")
    public IHeat getHeat(ItemStack stack) {
        return HeatCapability.get(stack);
    }

    @Info(value = "returns true if the stack does have an `IHeat` capability")
    public boolean hasHeat(ItemStack stack) {
        return HeatCapability.has(stack);
    }

    @Info(value = "Returns the `Heat` that describes the given temperature. Returns null for temperatures less than 1°C")
    @Nullable
    public Heat getHeatLevel(float temperature) {
        return Heat.getHeat(temperature);
    }

    @Info(value = "A map associating the name of a heat level to its Heat")
    @Generics(value = {String.class, Heat.class})
    public final Map<String, Heat> heatLevels = Util.make(new ImmutableMap.Builder<String, Heat>(), b -> {
        for (Heat heatLevel : Heat.values()) {
            b.put(heatLevel.name().toLowerCase(Locale.ROOT), heatLevel);
        }
    }).build();

    @Nullable
    @Info(value = "Returns the stack's `IFood` capability if present, else null")
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
    @Info(value = "Returns the `FoodTrait` with the given registry name if it exists, else null")
    public FoodTrait getFoodTrait(ResourceLocation id) {
        return FoodTrait.getTrait(id);
    }

    @Info(value = "Returns the registry name of the given food trait")
    public ResourceLocation getFoodTraitId(FoodTrait trait) {
        return FoodTrait.getId(trait);
    }

    @Info(value = "Applies the given food trait to the stack", params = {
            @Param(name = "stack", value = "The stack to add the trait to. **Important**: This stack *will* be modified"),
            @Param(name = "trait", value = "the id of the trait to be added")
    })
    public void applyFoodTrait(ItemStack stack, ResourceLocation trait) {
        final FoodTrait foodTrait = getFoodTrait(trait);
        if (foodTrait != null) {
            FoodCapability.applyTrait(stack, foodTrait);
        }
    }

    @Info(value = "Removes the given food trait to the stack", params = {
            @Param(name = "stack", value = "The stack to take the trait from. **Important**: This stack *will* be modified"),
            @Param(name = "trait", value = "the id of the trait to be removed")
    })
    public void removeFoodTrait(ItemStack stack, ResourceLocation trait) {
        final FoodTrait foodTrait = getFoodTrait(trait);
        if (foodTrait != null) {
            FoodCapability.removeTrait(stack, foodTrait);
        }
    }

    @Info(value = "Returns the `Size` value of the provided stack")
    public Size getSize(ItemStack stack) {
        return ItemSizeManager.get(stack).getSize(stack);
    }

    @Info(value = "Returns the `Weight` value of the provided stack")
    public Weight getWeight(ItemStack stack) {
        return ItemSizeManager.get(stack).getWeight(stack);
    }

    @Info(value = "Returns true if the given block can start a collapse", params = {
            @Param(name = "level", value = "The level to check in"),
            @Param(name = "pos", value = "The position to check at")
    })
    public boolean canStartCollapse(LevelAccessor level, BlockPos pos) {
        return CollapseRecipe.canStartCollapse(level, pos);
    }

    @Info(value = "Attempts to trigger a collapse, returns false if no collapse or a fake collapse occurred", params = {
            @Param(name = "level", value = "The level to attempt collapse in"),
            @Param(name = "pos", value = "The center position of the attempted collapse")
    })
    public boolean tryCollapse(Level level, BlockPos pos) {
        return CollapseRecipe.tryTriggerCollapse(level, pos);
    }

    @Info(value = "Forces a collapse to happen at a position, returns true if any blocks started collapsing", params = {
            @Param(name = "level", value = "The level to collapse in"),
            @Param(name = "pos", value = "The center position of the collapse")
    })
    public boolean forceCollapse(Level level, BlockPos pos) {
        return CollapseRecipe.startCollapse(level, pos);
    }

    @Info(value = "Finds and returns all positions in the given area that are unsupported", params = {
            @Param(name = "level", value = "The level to check in"),
            @Param(name = "from", value = "The minimum corner to check"),
            @Param(name = "to", value = "The maximum corner to check")
    })
    public Set<BlockPos> findUnsupportedPositions(BlockGetter level, BlockPos from, BlockPos to) {
        return Support.findUnsupportedPositions(level, from, to);
    }

    @Info(value = "Finds and returns all positions in the given area that are unsupported", params = {
            @Param(name = "level", value = "The level to check in"),
            @Param(name = "center", value = "The center position"),
            @Param(name = "horizontal", value = "The horizontal distance to check from the center"),
            @Param(name = "up", value = "The upwards distance to check from the center"),
            @Param(name = "down", value = "The downwards distance to check from the center")
    })
    public Set<BlockPos> findUnsupportedPositions(BlockGetter level, BlockPos center, int horizontal, int up, int down) {
        return Support.findUnsupportedPositions(level, center.offset(-horizontal, down, -horizontal), center.offset(horizontal, up, horizontal));
    }

    @Info(value = "Returns true if the position is supported")
    public boolean isSupported(BlockGetter level, BlockPos pos) {
        return Support.isSupported(level, pos);
    }

    @Info(value = "Returns an iterable of all positions that could possibly be supported around the min and max points")
    public Iterable<BlockPos> getMaximumSupportedAreaAround(BlockPos minPoint, BlockPos maxPoint) {
        return Support.getMaximumSupportedAreaAround(minPoint, maxPoint);
    }

    @Info(value = "Gets the `SupportRange` that is used as a maximum for checking if an area is supported")
    public Support.SupportRange getSupportCheckRange() {
        return Support.getSupportCheckRange();
    }

    @Info(value = "Gets the support from the block, or null if it is not a supporting block")
    @Nullable
    public Support getSupport(BlockState state) {
        return Support.get(state);
    }

    @Info(value = "Gets the support from the block, or null if it is not a supporting block")
    @Nullable
    public Support getSupport(BlockGetter level, BlockPos pos) {
        return Support.get(level.getBlockState(pos));
    }

    @Info(value = "Returns TFC's `ChunkData` object for the given level and position", params = {
            @Param(name = "level", value = "The level to get the data from"),
            @Param(name = "pos", value = "The position to get the data from")
    })
    public ChunkData getChunkData(LevelReader level, BlockPos pos) {
        return ChunkData.get(level, pos);
    }

    @Info(value = "Returns TFC's `RockData` object for the given level and position, may be null", params = {
            @Param(name = "level", value = "The level to get the data from"),
            @Param(name = "pos", value = "The position to get the data from")
    })
    @Nullable
    public RockData getRockData(LevelReader level, BlockPos pos) {
        final ChunkData data = getChunkData(level, pos);
        if (data.status() == ChunkData.Status.EMPTY || data.status() == ChunkData.Status.CLIENT) {
            return null; // #getRockData() throws when the status is CLIENT or EMPTY
        } else {
            return data.getRockData();
        }
    }

    @Info(value = "Returns TFC's `RockSettings` object for the given level and position, may be null", params = {
            @Param(name = "level", value = "The level to get the settings from"),
            @Param(name = "pos", value = "The position to get the settings from")
    })
    @Nullable
    public RockSettings getRockSettings(LevelReader level, BlockPos pos) {
        final RockData data = getRockData(level, pos);
        if (data != null) {
            return data.getRock(pos);
        }
        return null;
    }

    @Info(value = "Gets the `RockSettings` of the given block in the given level", params = {
            @Param(name = "level", value = "The level to check in"),
            @Param(name = "block", value = "the block to check")
    })
    @Nullable
    public static RockSettings getRockSettings(LevelAccessor level, Block block) {
        if (level instanceof ServerLevel serverLevel && serverLevel.getChunkSource().getGenerator() instanceof TFCChunkGenerator tfcGenerator) {
            return tfcGenerator.settings().rockLayerSettings().getRock(block);
        }
        return null;
    }

    @Info(value = "Returns the forest type at the given level and position", params = {
            @Param(name = "level", value = "The level to get the type from"),
            @Param(name = "pos", value = "The position to get the type from")
    })
    public ForestType getForestType(LevelReader level, BlockPos pos) {
        return getChunkData(level, pos).getForestType();
    }

    @Info(value = "Creates a new `OpenSimplex2D` noise, the implementation of 2D noise TFC uses for its worldgen")
    public OpenSimplex2D newOpenSimplex2D(long seed) {
        return new OpenSimplex2D(seed);
    }

    @Info(value = "Creates a new `OpenSimplex3D` noise, the implementation of 3D noise TFC uses for its worldgen")
    public OpenSimplex3D newOpenSimplex3D(long seed) {
        return new OpenSimplex3D(seed);
    }

    @Info(value = "Creates a new `Metaballs2D`, TFC's 2D implementation of Metaballs", params = {
            @Param(name = "random", value = "The random source used by the balls to create variance between instances"),
            @Param(name = "minBalls", value = "The minimum number of individual balls"),
            @Param(name = "maxBalls", value = "The maximum number of individual balls"),
            @Param(name = "minSize", value = "The minimum size of the Metaballs"),
            @Param(name = "maxSize", value = "The maximum size of the Metaballs"),
            @Param(name = "radius", value = "The maximum radius of an individual ball")
    })
    public Metaballs2D newMetaballs2D(RandomSource random, int minBalls, int maxBalls, double minSize, double maxSize, double radius) {
        return new Metaballs2D(random, minBalls, maxBalls, minSize, maxSize, radius);
    }

    @Info(value = "Creates a new `Metaballs3D`, TFC's 3D implementation of Metaballs", params = {
            @Param(name = "random", value = "The random source used by the balls to create variance between instances"),
            @Param(name = "minBalls", value = "The minimum number of individual balls"),
            @Param(name = "maxBalls", value = "The maximum number of individual balls"),
            @Param(name = "minSize", value = "The minimum size of the Metaballs"),
            @Param(name = "maxSize", value = "The maximum size of the Metaballs"),
            @Param(name = "radius", value = "The maximum radius of an individual ball")
    })
    public Metaballs3D newMetaballs3D(RandomSource random, int minBalls, int maxBalls, double minSize, double maxSize, double radius) {
        return new Metaballs3D(random, minBalls, maxBalls, minSize, maxSize, radius);
    }

    @Info(value = "Returns a number, in the range [0, 100], an expression of how hydrated the soil is", params = {
            @Param(name = "level", value = "The level to get the hydration from"),
            @Param(name = "pos", value = "THe position to get the hydration from")
    })
    public int getHydration(LevelAccessor level, BlockPos pos) {
        return FarmlandBlock.getHydration(level, pos);
    }

    @Info(value = "Returns the `Metal` associated with the given fluid, may be null")
    @Nullable
    public Metal getMetal(Fluid fluid) {
        return Metal.get(fluid);
    }

    @Info(value = "Returns the first `Metal` whose ingots match the given stack, may be null")
    @Nullable
    public Metal getMetalFromIngot(ItemStack ingot) {
        return Metal.getFromIngot(ingot);
    }

    @Info(value = "Returns the first `Metal` whose sheets match the given stack, may be null")
    @Nullable
    public Metal getMetalFromSheet(ItemStack sheet) {
        return Metal.getFromSheet(sheet);
    }

    @Info(value = "Returns the first `LampFuel` that matches the given fluid and state, may be null")
    @Nullable
    public LampFuel getLampFuel(Fluid fluid, BlockState state) {
        return LampFuel.get(fluid, state);
    }

    @Info(value = "Returns the first `Drinkable` that matches the given fluid, may be null")
    @Nullable
    public Drinkable getDrinkable(Fluid fluid) {
        return Drinkable.get(fluid);
    }

    @Info(value = "Returns the first `Fertilizer` that matches the given stack, may be null")
    @Nullable
    public Fertilizer getFertilizer(ItemStack stack) {
        return Fertilizer.get(stack);
    }

    @Info(value = "Returns the first `Fuel` that matches the given stack, may be null")
    @Nullable
    public Fuel getFuel(ItemStack stack) {
        return Fuel.get(stack);
    }

    @Info(value = "Returns the first `Pannable` that matches the given state, may be null")
    @Nullable
    public Pannable getPannable(BlockState state) {
        return Pannable.get(state);
    }

    @Info(value = "Returns the first `Sluiceable` that matches the given stack, may be null")
    @Nullable
    public Sluiceable getSluiceable(ItemStack stack) {
        return Sluiceable.get(stack);
    }
}
