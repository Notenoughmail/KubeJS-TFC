package io.github.notenoughmail.kubejstfc.implementation;

import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Printer;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.notenoughmail.kubejstfc.util.Printer.*;

public class DataTypes {

    public static String nameOf(DataType<?> dataType) {
        return KubeJSTFCRegistries.DATA_TYPES.getResourceKey(dataType).orElseThrow().location().toString();
    }

    public static final Display<EntityDamageResistance> ENTITY_DAMAGE_RESISTANCE = (e, m) -> {
        append(m, "entityTag", clickableTag(e.entity()));
        append(m, "crushing", e.damages().crushing());
        append(m, "piercing", e.damages().piercing());
        append(m, "slashing", e.damages().slashing(), true);
    };

    public static final Display<ItemDamageResistance> ITEM_DAMAGE_RESISTANCE = (i, m) -> {
        append(m, "ingredient", i.ingredient());
        append(m, "crushing", i.damages().crushing());
        append(m, "piercing", i.damages().piercing());
        append(m, "slashing", i.damages().slashing(), true);
    };

    // Drinkable

    public static final Display<Fertilizer> FERTILIZER = (f, m) -> {
        append(m, "nitrogen", f.nitrogen());
        append(m, "phosphorus", f.phosphorus());
        append(m, "potassium", f.potassium());
        append(m, "ingredient", f.ingredient(), true);
    };

    public static final Display<Fuel> FUEL = (f, m) -> {
        append(m, "duration", f.duration());
        append(m, "temperature", f.temperature());
        append(m, "purity", f.purity());
        append(m, "ingredient", f.ingredient(), true);
    };

    public static final Display<FluidHeat> FLUID_HEAT = (f, m) -> {
        append(m, "fluid", f.fluid());
        append(m, "meltTemperature", f.meltTemperature());
        append(m, "specificHeatCapacity", f.specificHeatCapacity(), true);
    };

    // KnappingType

    // Support

    public static final Display<ItemSizeDefinition> ITEM_SIZE = (i, m) -> {
        append(m, "size", i.size());
        append(m, "weight", i.weight());
        append(m, "ingredient", i.ingredient(), true);
    };

    // Fauna

    public static final Display<ClimateRange> CLIMATE_RANGE = (c, m) -> {
        append(m, "minHydration", c.minHydration());
        append(m, "maxHydration", c.maxHydration());
        append(m, "hydrationWiggleRange", c.hydrationWiggleRange());
        append(m, "minTemperature", c.minTemperature());
        append(m, "maxTemperature", c.maxTemperature());
        append(m, "temperatureWiggleRange", c.hydrationWiggleRange(), true);
    };

    // LampFuel

    // Deposit

    public static final Display<HeatDefinition> HEAT = (h, m) -> {
        append(m, "heatCapacity", h.heatCapacity());
        append(m, "forgingTemperature", h.forgingTemperature());
        append(m, "weldingTemperature", h.weldingTemperature());
        append(m, "ingredient", h.ingredient(), true);
    };

    // Food

    public static <T> DataType<T> cachedItemRegistry(
            DataManager<T> manager,
            Display<T> display,
            BiPredicate<T, Item> valueTester,
            IndirectHashCollection<Item, T> cache
    ) {
        return cachedRegistry(manager, BuiltInRegistries.ITEM, display, valueTester, cache);
    }

    public static <T, R> DataType<T> cachedRegistry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            IndirectHashCollection<R, T> cache
    ) {
        return registry(manager, registry, display, valueTester, () -> cache.indirectResultMap.keySet().stream());
    }

    public static <T, R> DataType<T> registry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            Supplier<Stream<R>> searchable
    ) {
        return registry(manager, registry, display, valueTester, searchable, Printer::stringify);
    }

    public static <T, R> DataType<T> registry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            Supplier<Stream<R>> searchable,
            Function<R, String> stringifier
    ) {
        return new RegistryBackedDataManagerType.Impl<>(manager, registry, display, valueTester, () -> searchable.get().map(stringifier));
    }

    public static <T> DataType<T> unsearchableManager(DataManager<T> manager, Display<T> display) {
        return new DataManagerType.UnsearchableImpl<>(manager, display);
    }

    @FunctionalInterface
    public interface Display<T> extends BiConsumer<T, MutableComponent> {}

    public interface DataManagerType<T> extends DataType<T> {

        DataManager<T> manager();

        @Override
        @Nullable
        default T find(String str) {
            return manager().get(ResourceLocation.parse(str));
        }

        @Override
        default Set<String> names() {
            return manager().getElements().keySet()
                    .stream()
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        record UnsearchableImpl<T>(DataManager<T> manager, Display<T> display) implements DataManagerType<T> {

            @Override
            public void display(T value, MutableComponent text) {
                display.accept(value, text);
            }

            @Override
            public boolean canBeSearched() {
                return false;
            }

            @Override
            public Set<String> search(String str) {
                return Set.of();
            }

            @Override
            public Stream<String> searchSuggestions() {
                return Stream.empty();
            }
        }
    }

    public interface RegistryBackedDataManagerType<T, R> extends DataManagerType<T> {

        Registry<R> registry();

        boolean valueHas(T value, R regEntry);

        @Override
        default Set<String> search(String str) {
            final R regEntry = registry().get(ResourceLocation.parse(str));
            if (regEntry == null) return Set.of();

            return manager().getElements().entrySet().stream()
                    .filter(e -> valueHas(e.getValue(), regEntry))
                    .map(Map.Entry::getKey)
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @Override
        default boolean canBeSearched() {
            return true;
        }

        record Impl<T, R>(DataManager<T> manager, Registry<R> registry, Display<T> display, BiPredicate<T, R> valueHas, Supplier<Stream<String>> searchSuggest) implements RegistryBackedDataManagerType<T, R> {

            @Override
            public boolean valueHas(T value, R regEntry) {
                return valueHas.test(value, regEntry);
            }

            @Override
            public void display(T value, MutableComponent text) {
                display.accept(value, text);
            }

            @Override
            public Stream<String> searchSuggestions() {
                return searchSuggest.get();
            }
        }
    }
}
