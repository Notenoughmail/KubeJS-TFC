package com.notenoughmail.kubejs_tfc.util.implementation.bindings;

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
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.chunkdata.RockData;
import net.dries007.tfc.world.noise.Metaballs2D;
import net.dries007.tfc.world.noise.Metaballs3D;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import net.dries007.tfc.world.noise.OpenSimplex3D;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum MiscBindings {
    INSTANCE;

    @Info(value = "A map associating the name of a rock to its `RegistryRock`")
    @Generics(value = {String.class, RegistryRock.class})
    public final Map<String, RegistryRock> rock = new HashMap<>(20);

    @Info(value = "A map associating the name of a wood to its `NamedRegistryWood`, includes AFC woods if it is present")
    @Generics(value = {String.class, NamedRegistryWood.class})
    public final Map<String, NamedRegistryWood> wood = new HashMap<>(19);

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
    public final Map<String, Heat> heatLevels = new HashMap<>(11);

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

    @Info(value = "Returns the first `Metal` whose ingots match the given stack")
    @Nullable
    public Metal getMetalFromIngot(ItemStack ingot) {
        return Metal.getFromIngot(ingot);
    }

    @Info(value = "Returns the first `Metal` whose sheets match the given stack")
    @Nullable
    public Metal getMetalFromSheet(ItemStack sheet) {
        return Metal.getFromSheet(sheet);
    }
}
