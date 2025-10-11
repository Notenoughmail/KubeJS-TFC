package io.github.notenoughmail.kubejstfc.registry;

import com.mojang.serialization.MapCodec;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.RockSurfaceRuleSource;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.Range;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.commands.TreeSolver;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.entities.Fauna;
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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Arrays;
import java.util.Optional;

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
                TreeSolver.TYPE_INFO
        ));
        COMMAND_ARGS.register("data_type", () -> ArgumentTypeInfos.registerByClass(
                DataType.Argument.class,
                DataType.TYPE_INFO
        ));

        SURFACE_RULE_SOURCE.register("rock", RockSurfaceRuleSource.CODEC::codec);

        DATA_TYPE.register(
                "climate_range",
                () -> DataTypes.unsearchableManager(ClimateRange.MANAGER, DataTypes.CLIMATE_RANGE)
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
                        (resistance, item) -> resistance.matches(item.getDefaultInstance()),
                        ItemDamageResistance.CACHE
                )
        );
        DATA_TYPE.register(
                "fertilizer",
                () -> DataTypes.cachedItemRegistry(
                        Fertilizer.MANAGER,
                        DataTypes.FERTILIZER,
                        (fertilizer, item) -> fertilizer.ingredient().test(item.getDefaultInstance()),
                        Fertilizer.CACHE
                )
        );
        DATA_TYPE.register(
                "fuel",
                () -> DataTypes.cachedItemRegistry(
                        Fuel.MANAGER,
                        DataTypes.FUEL,
                        (fuel, item) -> fuel.matches(item.getDefaultInstance()),
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
                        (size, item) -> size.ingredient().test(item.getDefaultInstance()),
                        ItemSizeManager.CACHE
                )
        );
        DATA_TYPE.register(
                "heat",
                () -> DataTypes.cachedItemRegistry(
                        HeatCapability.MANAGER,
                        DataTypes.HEAT,
                        (heat, item) -> heat.matches(item.getDefaultInstance()),
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
                        (knappingType, item) -> knappingType.matches(item.getDefaultInstance()),
                        () -> KnappingType.MANAGER.getValues().stream()
                                .map(KnappingType::inputItem)
                                .map(SizedIngredient::ingredient)
                                .flatMap(i -> Arrays.stream(i.getItems()))
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
                        (deposit, item) -> deposit.matches(item.getDefaultInstance()),
                        Deposit.CACHE
                )
        );
        DATA_TYPE.register(
                "food",
                () -> DataTypes.cachedItemRegistry(
                        FoodCapability.MANAGER,
                        DataTypes.FOOD,
                        (food, item) -> food.matches(item.getDefaultInstance()),
                        FoodCapability.CACHE
                )
        );
    }
}
