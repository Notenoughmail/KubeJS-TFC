package io.github.notenoughmail.kubejstfc.util;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.event.EventExit;
import dev.latvian.mods.kubejs.event.IEventHandler;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.*;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.extensions.MutableLevelTier;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.Nutrient;
import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Assistant {

    Direction[] COMPASS_DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    static MutableLevelTier levelTier(MutableToolTier tier) {
        return Cast.to(tier);
    }

    static void singleTag(BuilderBase<?> builder, TagKey<?> tag) {
        singleTag(builder, tag.location());
    }

    static void singleTag(BuilderBase<?> builder, ResourceLocation tag) {
        builder.defaultTags.add(tag);
    }

    static void toolItemAttributes(HandheldItemBuilder builder) {
        builder.itemAttributeModifiers = ToolItem.productAttributes(builder.toolTier, builder.attackDamageBaseline, builder.speedBaseline);
    }

    @Nullable
    static <T> Supplier<T> holderAsSupplier(@Nullable Holder<T> holder) {
        return mapNull(holder, h -> h::value);
    }

    @Nullable
    static <R, T> R mapNull(@Nullable T t, Function<@NotNull T, R> map) {
        return t == null ? null : map.apply(t);
    }

    static Supplier<@Nullable ParticleOptions> getParticleOptions(@Nullable Holder<ParticleType<?>> holder) {
        return holder == null ? () -> null : Suppliers.memoize(() -> {
            final ParticleType<?> type = holder.value();
            if (type instanceof ParticleOptions options) {
                return options;
            }
            ConsoleJS.SERVER.error("'%s' is not a ParticleOptions".formatted(holder));
            return null;
        });
    }

    static Supplier<@Nullable ParticleOptions> wrapParticleOptionsSafely(Supplier<ParticleOptions> particle) {
        return Suppliers.memoize(() -> {
            try {
                return particle.get();
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Could not parse particle options", e);
                return null;
            }
        });
    }

    static <T extends Comparable<T>> boolean haveSamePropertyValue(BlockState s1, BlockState s2, Property<T> p) {
        return s1.getValue(p).compareTo(s2.getValue(p)) == 0;
    }

    static <T extends KubeEvent> IEventHandler handleKube(KubeHandler<T> handler) {
        return e -> {
            handler.handle((T) e);
            return handler;
        };
    }

    static JsonObject json(Consumer<JsonObject> builder) {
        return Util.make(new JsonObject(), builder);
    }

    static <B extends BlockBuilder> void addBlock(AdditionalObjectRegistry registry, Supplier<@Nullable B> builder) {
        addBlock(registry, builder.get());
    }

    static void addBlock(AdditionalObjectRegistry registry, @Nullable BlockBuilder builder) {
        addBlock(registry, builder, true);
    }

    static void addBlock(AdditionalObjectRegistry registry, @Nullable BlockBuilder builder, boolean nest) {
        if (builder != null) {
            registry.add(Registries.BLOCK, builder);
            if (nest) builder.createAdditionalObjects(registry);
        }
    }

    static void addItem(AdditionalObjectRegistry registry, @Nullable ItemBuilder builder) {
        if (builder != null) {
            registry.add(Registries.ITEM, builder);
        }
    }

    static <T> T applyIf(T t, boolean condition, Consumer<T> apply) {
        if (condition) {
            apply.accept(t);
        }
        return t;
    }

    @Nullable
    static <T> T orElse(@NotNull T t, Predicate<@NotNull T> filter, @Nullable T fallback) {
        return filter.test(t) ? fallback : t;
    }

    static <T> void iterate(Iterable<T> i, Consumer<T> action, Consumer<T> onAllButLast) {
        final Iterator<T> iter = i.iterator();
        while (iter.hasNext()) {
            final T t = iter.next();
            action.accept(t);
            if (iter.hasNext()) {
                onAllButLast.accept(t);
            }
        }
    }

    static Map<String, Object> foodDataAsMap(FoodData food) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("hunger", food.hunger());
        map.put("water", food.water());
        map.put("saturation", food.saturation());
        map.put("intoxication", food.intoxication());
        for (Nutrient n : Nutrient.VALUES)
            map.put(n.getSerializedName(), food.nutrient(n));
        map.put("decayModifier", food.decayModifier());
        return map;
    }

    static <T> T getPrivateField(Object object, String fieldName, Class<T> fieldType) {
        try {
            return Cast.to(Hidden.PRIVATE_FIELDS
                    .computeIfAbsent(object.getClass(), c -> new HashMap<>())
                    .computeIfAbsent(fieldName, n -> {
                        Field field = getField(object, fieldName, fieldType);
                        if (field == null) {
                            throw new IllegalArgumentException("Field (%s) of type (%s) could not be found in %s or any of its superclasses".formatted(fieldName, fieldType.getSimpleName(), object));
                        }
                        return field;
                    }).get(object));
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while trying to get value of private field", e);
        }
    }

    @Nullable
    private static <T> Field getField(Object object, String fieldName, Class<T> fieldType) {
        Class<?> clazz = object.getClass();
        Field field = null;
        while (field == null && clazz != Object.class) {
            try {
                final Field f = clazz.getDeclaredField(fieldName);
                if (f.getType() == fieldType) {
                    f.setAccessible(true);
                    field = f;
                }
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return field;
    }

    static <P, R> Stream<R> forAllMethods(Class<?> clazz, P parameter, Class<R> returnType) {
        final Class<P> parameterType = Cast.to(parameter.getClass());
        final Stream.Builder<R> builder = Stream.builder();
        for (Method m : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getReturnType() == returnType && m.getParameterCount() == 1 && m.getParameterTypes()[0] == parameterType) {
                try {
                    m.setAccessible(true);
                    builder.accept(Cast.to(m.invoke(null, parameter)));
                } catch (Throwable t) {
                    KubeJSTFC.LOGGER.warn("Error while trying to invoke %s, skipping".formatted(m), t);
                }
            }
        }
        return builder.build();
    }

    static <T> T tryOrElse(Supplier<T> source, T fallback, Consumer<Throwable> onFail) {
        try {
            return source.get();
        } catch (Throwable t) {
            onFail.accept(t);
            return fallback;
        }
    }

    @FunctionalInterface
    interface KubeHandler<T extends KubeEvent> {
        void handle(T t) throws EventExit;
    }

    static <K, V> Supplier<Map<K, V>> unmodifiable(Supplier<Map<K, V>> constructor, Consumer<Map<K, V>> builder) {
        return Suppliers.memoize(() -> Collections.unmodifiableMap(Util.make(constructor.get(), builder)));
    }

    Supplier<Map<ResourceLocation, RegistryWood>> WOODS = unmodifiable(HashMap::new, Hidden.WOODS);
    Supplier<Map<ResourceLocation, RegistryMetal>> METALS = unmodifiable(HashMap::new, Hidden.METALS);
    Supplier<Map<ResourceLocation, RegistryRock>> ROCKS = unmodifiable(HashMap::new, Hidden.ROCKS);

    static void registerWoods(MapBuilder<RegistryWood> reg) {
        Hidden.WOODS.queue(reg);
    }

    static void registerMetals(MapBuilder<RegistryMetal> reg) {
        Hidden.METALS.queue(reg);
    }

    static void registerRocks(MapBuilder<RegistryRock> reg) {
        Hidden.ROCKS.queue(reg);
    }

    static <T> T notNull(@Nullable T t, String name, Context ctx) {
        if (t == null) {
            throw new KubeRuntimeException("'%s' must be defined".formatted(name)).source(SourceLine.of(ctx));
        }
        return t;
    }

    static <T> T notNull(@Nullable T t, String name, SourceLine source) {
        if (t == null) {
            throw new KubeRuntimeException("'%s' must be defined!".formatted(name)).source(source);
        }
        return t;
    }

    static boolean canBeHandledByDefaultRecordWrapper(Object o) {
        return switch (o) {
            case Map<?, ?> $ -> true;
            case NativeJavaObject $ -> true;
            case NativeMap $ -> true;
            case NativeArray $ -> true; // NativeJavaList inherits from NativeJavaObject, thus not here
            case Callable $ -> true; // I guess I'll allow it...
            default -> false;
        };
    }

    class Hidden {
        static final Map<Class<?>, Map<String, Field>> PRIVATE_FIELDS = new IdentityHashMap<>();
        static final Actionable<Map<ResourceLocation, RegistryWood>> WOODS = new Actionable<>();
        static final Actionable<Map<ResourceLocation, RegistryMetal>> METALS = new Actionable<>();
        static final Actionable<Map<ResourceLocation, RegistryRock>> ROCKS = new Actionable<>();
    }

    @FunctionalInterface
    interface MapBuilder<T> extends Consumer<Map<ResourceLocation, T>> {}
}
