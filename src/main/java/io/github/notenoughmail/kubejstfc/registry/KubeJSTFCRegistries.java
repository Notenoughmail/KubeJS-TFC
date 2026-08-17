package io.github.notenoughmail.kubejstfc.registry;

import com.mojang.serialization.MapCodec;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.util.commands.BooleanTypeInfo;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.Range;
import io.github.notenoughmail.kubejstfc.util.commands.TreeSolver;
import io.github.notenoughmail.kubejstfc.worldgen.generator.RockSurfaceRuleSource;
import io.github.notenoughmail.kubejstfc.worldgen.generator.WrappedChunkGenerator;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.util.AlloyRange;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.*;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class KubeJSTFCRegistries {

    public static void init(IEventBus modBus) {
        COMMAND_ARGS.register(modBus);
        CHUNK_GENERATOR.register(modBus);
        SURFACE_RULE_SOURCE.register(modBus);
        DATA_TYPE.register(modBus);
    }

    public static final ResourceKey<? extends Registry<DataType<?>>> DATA_TYPE_KEY = ResourceKey.createRegistryKey(KubeJSTFC.id("data_type"));
    public static final Registry<DataType<?>> DATA_TYPES = new RegistryBuilder<>(DATA_TYPE_KEY).create();

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGS = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, KubeJSTFC.ID);
    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = DeferredRegister.create(Registries.CHUNK_GENERATOR, KubeJSTFC.ID);
    private static final DeferredRegister<MapCodec<? extends SurfaceRules.RuleSource>> SURFACE_RULE_SOURCE = DeferredRegister.create(Registries.MATERIAL_RULE, KubeJSTFC.ID);
    private static final DeferredRegister<DataType<?>> DATA_TYPE = DeferredRegister.create(DATA_TYPE_KEY, TerraFirmaCraft.MOD_ID);

    static {
        COMMAND_ARGS.register("range", () -> ArgumentTypeInfos.registerByClass(
                Range.RangeArgumentType.class,
                SingletonArgumentInfo.contextFree(Range::arg)
        ));
        COMMAND_ARGS.register("tree_solver", () -> ArgumentTypeInfos.registerByClass(
                TreeSolver.ArgType.class,
                BooleanTypeInfo.of(TreeSolver.ArgType::log, TreeSolver.ArgType::new)
        ));
        COMMAND_ARGS.register("data_type", () -> ArgumentTypeInfos.registerByClass(
                DataType.Argument.class,
                BooleanTypeInfo.of(DataType.Argument::all, DataType.Argument::new)
        ));

        CHUNK_GENERATOR.register("wrapped", () -> WrappedChunkGenerator.CODEC);

        SURFACE_RULE_SOURCE.register("rock", RockSurfaceRuleSource.CODEC::codec);

        DATA_TYPE.register(
                "climate_range",
                () -> DataTypes.unsearchableManager(
                        ClimateRange.MANAGER,
                        DataTypes.CLIMATE_RANGE
                )
        );
        DATA_TYPE.register(
                "entity_damage_resistance",
                () -> DataTypes.registry(
                        EntityDamageResistance.MANAGER,
                        BuiltInRegistries.ENTITY_TYPE,
                        DataTypes.ENTITY_DAMAGE_RESISTANCE,
                        (resistance, type) -> Helpers.isEntity(type, resistance.entity()),
                        () -> EntityDamageResistance.MANAGER.getValues().stream()
                                .map(EntityDamageResistance::entity)
                                .map(BuiltInRegistries.ENTITY_TYPE::getTag)
                                .flatMap(Optional::stream)
                                .flatMap(HolderSet.Named::stream)
                                .<EntityType<?>>map(Holder::value)
                                .distinct()
                )
        );
        DATA_TYPE.register(
                "item_damage_resistance",
                () -> DataTypes.cachedItemRegistry(
                        ItemDamageResistance.MANAGER,
                        DataTypes.ITEM_DAMAGE_RESISTANCE,
                        ItemDamageResistance.CACHE
                )
        );
        DATA_TYPE.register(
                "fertilizer",
                () -> DataTypes.cachedItemRegistry(
                        Fertilizer.MANAGER,
                        DataTypes.FERTILIZER,
                        (fertilizer, item) -> fertilizer.ingredient().kjs$testItem(item),
                        Fertilizer.CACHE
                )
        );
        DATA_TYPE.register(
                "fuel",
                () -> DataTypes.cachedItemRegistry(
                        Fuel.MANAGER,
                        DataTypes.FUEL,
                        Fuel.CACHE
                )
        );
        DATA_TYPE.register(
                "fluid_heat",
                () -> DataTypes.registry(
                        FluidHeat.MANAGER,
                        BuiltInRegistries.FLUID,
                        DataTypes.FLUID_HEAT,
                        (fluidHeat, fluid) -> fluid == fluidHeat.fluid(),
                        () -> FluidHeat.BY_FLUID.keySet().stream()
                )
        );
        DATA_TYPE.register(
                "item_size",
                () -> DataTypes.cachedItemRegistry(
                        ItemSizeManager.MANAGER,
                        DataTypes.ITEM_SIZE,
                        (size, item) -> size.ingredient().kjs$testItem(item),
                        ItemSizeManager.CACHE
                )
        );
        DATA_TYPE.register(
                "heat",
                () -> DataTypes.cachedItemRegistry(
                        HeatCapability.MANAGER,
                        DataTypes.HEAT,
                        HeatCapability.CACHE
                )
        );
        DATA_TYPE.register(
                "drinkable",
                () -> DataTypes.cachedRegistry(
                        Drinkable.MANAGER,
                        BuiltInRegistries.FLUID,
                        DataTypes.DRINKABLE,
                        (drinkable, fluid) -> drinkable.ingredient().test(new FluidStack(fluid, 1000)),
                        Drinkable.CACHE
                )
        );
        DATA_TYPE.register(
                "knapping_type",
                () -> DataTypes.registry(
                        KnappingType.MANAGER,
                        BuiltInRegistries.ITEM,
                        DataTypes.KNAPPING_TYPE,
                        (knappingType, item) -> knappingType.matches(item.getDefaultInstance().kjs$withCount(99)),
                        () -> KnappingType.MANAGER.getValues().stream()
                                .map(KnappingType::inputItem)
                                .map(SizedIngredient::ingredient)
                                .map(Ingredient::getItems)
                                .flatMap(Arrays::stream)
                                .map(ItemStack::getItem)
                                .distinct()
                )
        );
        DATA_TYPE.register(
                "support",
                () -> DataTypes.cachedRegistry(
                        Support.MANAGER,
                        BuiltInRegistries.BLOCK,
                        DataTypes.SUPPORT,
                        (support, block) -> support.ingredient().test(block),
                        Support.CACHE
                )
        );
        DATA_TYPE.register(
                "fauna",
                () -> DataTypes.unsearchableManager(
                        Fauna.MANAGER,
                        DataTypes.FAUNA
                )
        );
        DATA_TYPE.register(
                "lamp_fuel",
                () -> DataTypes.cachedRegistry(
                        LampFuel.MANAGER,
                        BuiltInRegistries.FLUID,
                        DataTypes.LAMP_FUEL,
                        (lampFuel, fluid) -> lampFuel.fluid().test(new FluidStack(fluid, 1000)),
                        LampFuel.CACHE
                )
        );
        DATA_TYPE.register(
                "deposit",
                () -> DataTypes.cachedItemRegistry(
                        Deposit.MANAGER,
                        DataTypes.DEPOSIT,
                        Deposit.CACHE
                )
        );
        DATA_TYPE.register(
                "food",
                () -> DataTypes.cachedItemRegistry(
                        FoodCapability.MANAGER,
                        DataTypes.FOOD,
                        FoodCapability.CACHE
                )
        );
        DATA_TYPE.register(
                "recipe/collapse",
                () -> DataTypes.forCachedBlockRecipe(
                        CollapseRecipe.CACHE,
                        DataTypes.BLOCK_RECIPE.cast(),
                        TFCRecipeTypes.COLLAPSE
                )
        );
        DATA_TYPE.register(
                "recipe/landslide",
                () -> DataTypes.forCachedBlockRecipe(
                        LandslideRecipe.CACHE,
                        DataTypes.BLOCK_RECIPE.cast(),
                        TFCRecipeTypes.LANDSLIDE
                )
        );
        DATA_TYPE.register(
                "recipe/chisel",
                () -> DataTypes.forCachedBlockRecipe(
                        ChiselRecipe.CACHE,
                        DataTypes.CHISEL,
                        TFCRecipeTypes.CHISEL
                )
        );
        DATA_TYPE.register(
                "recipe/scraping",
                () -> DataTypes.forCachedItemRecipe(
                        ScrapingRecipe.CACHE,
                        DataTypes.SCRAPING,
                        TFCRecipeTypes.SCRAPING
                )
        );
        DATA_TYPE.register(
                "recipe/casting",
                () -> DataTypes.forCachedItemRecipe(
                        CastingRecipe.CACHE,
                        DataTypes.CASTING,
                        TFCRecipeTypes.CASTING
                )
        );
        DATA_TYPE.register(
                "recipe/heating",
                () -> DataTypes.forCachedItemRecipe(
                        HeatingRecipe.CACHE,
                        DataTypes.HEATING,
                        TFCRecipeTypes.HEATING
                )
        );
        DATA_TYPE.register(
                "recipe/loom",
                () -> DataTypes.forCachedItemRecipe(
                        LoomRecipe.CACHE,
                        DataTypes.LOOM,
                        TFCRecipeTypes.LOOM
                )
        );
        DATA_TYPE.register(
                "recipe/quern",
                () -> DataTypes.forCachedItemRecipe(
                        QuernRecipe.CACHE,
                        DataTypes.BASIC_ITEM.cast(),
                        TFCRecipeTypes.QUERN
                )
        );
        DATA_TYPE.register(
                "recipe/welding",
                () -> DataTypes.forUncachedRecipe(
                        DataTypes.WELDING,
                        TFCRecipeTypes.WELDING,
                        BuiltInRegistries.ITEM,
                        (w, i) -> {
                            final ItemStack s = i.getDefaultInstance();
                            return w.getFirstInput().test(s) || w.getSecondInput().test(s);
                        },
                        w -> Stream.concat(
                                w.getFirstInput().kjs$getItemStream(),
                                w.getSecondInput().kjs$getItemStream()
                        ).distinct(),
                        null
                )
        );
        DATA_TYPE.register(
                "recipe/anvil",
                () -> DataTypes.forUncachedRecipe(
                        DataTypes.ANVIL,
                        TFCRecipeTypes.ANVIL,
                        BuiltInRegistries.ITEM,
                        (a, i) -> a.getInput().test(i.getDefaultInstance()),
                        a -> a.getInput().kjs$getItemStream().distinct(),
                        null
                )
        );
        DATA_TYPE.register(
                "recipe/sewing",
                () -> DataTypes.forUncachedRecipe(
                        DataTypes.SEWING,
                        TFCRecipeTypes.SEWING
                )
        );
        DATA_TYPE.register(
                "recipe/alloy",
                () -> DataTypes.forUncachedRecipe(
                        DataTypes.ALLOY,
                        TFCRecipeTypes.ALLOY,
                        BuiltInRegistries.FLUID,
                        (a, f) -> a.contents().stream().anyMatch(r -> r.fluid() == f),
                        a -> a.contents().stream().map(AlloyRange::fluid),
                        null
                )
        );
        DATA_TYPE.register(
                "recipe/instant_fluid_barrel",
                () -> DataTypes.forUncachedRecipe(
                        DataTypes.INSTANT_FLUID_BARREL,
                        TFCRecipeTypes.BARREL_INSTANT_FLUID,
                        BuiltInRegistries.FLUID,
                        (i, f) -> {
                            final FluidStack s = new FluidStack(f, 1000);
                            return i.getInputFluid().ingredient().test(s) || i.getAddedFluid().ingredient().test(s);
                        },
                        i -> Stream.concat(
                                Arrays.stream(i.getInputFluid().ingredient().getStacks()),
                                Arrays.stream(i.getAddedFluid().ingredient().getStacks())
                        ).map(FluidStack::getFluid).distinct(),
                        null
                )
        );
        DATA_TYPE.register(
                "recipe/instant_barrel",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.BASE_BARREL.cast(),
                        TFCRecipeTypes.BARREL_INSTANT,
                        DataTypes.Search.sizedItem(BarrelRecipe::getInputItem),
                        DataTypes.Search.sizedFluid(BarrelRecipe::getInputFluid)
                )
        );
        DATA_TYPE.register(
                "recipe/sealed_barrel",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.SEALED_BARREL,
                        TFCRecipeTypes.BARREL_SEALED,
                        DataTypes.Search.sizedItem(BarrelRecipe::getInputItem),
                        DataTypes.Search.sizedFluid(BarrelRecipe::getInputFluid)
                )
        );
        DATA_TYPE.register(
                "recipe/bloomery",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.BLOOMERY,
                        TFCRecipeTypes.BLOOMERY,
                        DataTypes.Search.sizedItem(BloomeryRecipe::getCatalyst),
                        DataTypes.Search.sizedFluid(BloomeryRecipe::getInputFluid)
                )
        );
        DATA_TYPE.register(
                "recipe/blast_furnace",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.BLAST_FURNACE,
                        TFCRecipeTypes.BLAST_FURNACE,
                        DataTypes.Search.item(BlastFurnaceRecipe::catalyst),
                        DataTypes.Search.sizedFluid(BlastFurnaceRecipe::inputFluid)
                )
        );
        DATA_TYPE.register(
                "recipe/glassworking",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.GLASSWORKING,
                        TFCRecipeTypes.GLASSWORKING,
                        DataTypes.Search.item(GlassworkingRecipe::batchItem),
                        new DataTypes.Search<>(
                                GlassOperation.REGISTRY,
                                (g, o) -> g.operations().contains(o),
                                g -> g.operations().stream()
                        )
                )
        );
        DATA_TYPE.register(
                "recipe/jam_pot",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.JAM_POT,
                        TFCRecipeSerializers.POT_JAM,
                        TFCRecipeTypes.POT,
                        DataTypes.Search.multiItem(j -> j.getItemIngredients().stream()),
                        DataTypes.Search.sizedFluid(JamPotRecipe::getFluidIngredient)
                )
        );
        DATA_TYPE.register(
                "recipe/pot",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.SIMPLE_POT,
                        TFCRecipeSerializers.POT_SIMPLE,
                        TFCRecipeTypes.POT,
                        DataTypes.Search.multiItem(j -> j.getItemIngredients().stream()),
                        DataTypes.Search.sizedFluid(SimplePotRecipe::getFluidIngredient)
                )
        );
        DATA_TYPE.register(
                "recipe/soup_pot",
                () -> DataTypes.forUncachedMultiLookupRecipe(
                        DataTypes.POT.cast(),
                        TFCRecipeSerializers.POT_SOUP,
                        TFCRecipeTypes.POT,
                        DataTypes.Search.multiItem(j -> j.getItemIngredients().stream()),
                        DataTypes.Search.sizedFluid(SoupPotRecipe::getFluidIngredient)
                )
        );
    }
}
