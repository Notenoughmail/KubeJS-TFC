package io.github.notenoughmail.kubejstfc.worldgen.support;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.type.RecordTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.dries007.tfc.util.collections.IWeighted;
import net.minecraft.Util;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An 'abstracted' representation of a weighted value which can be converted to vanilla's {@link SimpleWeightedRandomList}
 * or TFC's {@link IWeighted}.
 * <p>
 * A single or null {@code Weighted} object outside a list is meaningless and should not occur
 */
public record Weighted<T>(T value, int weight) {

    private static final RecordTypeInfo TYPE = Cast.to(TypeInfo.of(Weighted.class));
    private static final TypeInfo CONSUMER_TYPE = TypeInfo.RAW_CONSUMER.withParams(TypeInfo.RAW_MAP.withParams(TypeInfo.STRING, TypeInfo.NONE));

    public static Weighted<?> wrap(Context ctx, Object from, TypeInfo target) {
        return switch (from) {
            case null -> throw Context.reportRuntimeError("Can't interpret 'null' as a weighted object", ctx);
            case Weighted<?> w -> w;
            case Map<?, ?> map -> c(ctx, map, target);
            case Iterable<?> itr -> c(ctx, itr, target);
            case Callable c -> c(ctx, c, target);
            default -> new Weighted<>(ctx.jsToJava(from, target.param(0)), 1);
        };
    }

    private static Weighted<?> c(Context ctx, Object from, TypeInfo target) {
        if (TYPE.asClass().isInstance(from)) {
            return Cast.to(from);
        } else if (from instanceof NativeArray || from instanceof NativeJavaList) {
            final Object[] arr = Cast.to(ctx.arrayOf(from, TypeInfo.NONE));
            return fromArr(arr, ctx, target);
        } else if (from instanceof Map<?,?> || from instanceof NativeJavaObject) {
            final Map<String, ?> map = Cast.to(ctx.mapOf(from, TypeInfo.STRING, TypeInfo.NONE));
            return fromMap(map, ctx, target);
        } else if (from instanceof Callable) {
            final Map<String, ?> map = new HashMap<>(2);
            final Consumer<Map<String, ?>> consumer = Cast.to(ctx.jsToJava(from, CONSUMER_TYPE));
            consumer.accept(map);
            return fromMap(map, ctx, target);
        } else {
            return Cast.to(ctx.reportConversionError(from, target));
        }
    }

    private static Weighted<?> fromArr(Object[] from, Context ctx, TypeInfo target) {
        final RecordTypeInfo.Data data = TYPE.getData();
        final Object[] args = new Object[2];
        args[1] = 1;
        final int len = Math.min(2, from.length);

        for (int i = 0 ; i < len ; i++) {
            args[i] = ctx.jsToJava(
                    from[i],
                    i == 0 ?
                            target.param(0) :
                            data.components()[i].type()
            );
        }

        return make(ctx, from, args, target);
    }

    private static Weighted<?> fromMap(Map<String, ?> from, Context ctx, TypeInfo target) {
        final RecordTypeInfo.Data data = TYPE.getData();
        final Object[] args = new Object[2];
        args[1] = 1;

        for (Map.Entry<String, ?> entry : from.entrySet()) {
            final RecordTypeInfo.Component c = data.componentMap().get(entry.getKey());

            if (c != null) {
                args[c.index()] = ctx.jsToJava(
                        entry.getValue(),
                        c.name().equals("value") ?
                                target.param(0) :
                                c.type()
                );
            }
        }

        return make(ctx, from, args, target);
    }

    private static Weighted<?> make(Context ctx, Object originalFrom, Object[] args, TypeInfo target) {
        final MethodHandle constructor = ctx.factory.getRecordConstructor(TYPE.asClass());

        if (constructor == null) {
            throw Context.reportRuntimeError("Unable to find Weighted<?> constructor", ctx);
        }

        try {
            return Cast.to(constructor.invokeWithArguments(args));
        } catch (RhinoException ex) {
            return Cast.to(ctx.reportConversionError(originalFrom, target));
        } catch (Throwable ex) {
            throw Context.throwAsScriptRuntimeEx(ex, ctx);
        }
    }

    public static <T> SimpleWeightedRandomList<T> toVanilla(List<Weighted<T>> values) {
        return switch (values.size()) {
            case 0 -> SimpleWeightedRandomList.empty();
            case 1 -> SimpleWeightedRandomList.single(values.getFirst().value);
            default -> Util.make(SimpleWeightedRandomList.<T>builder(), b -> {
                for (Weighted<T> w : values) {
                    b.add(w.value, w.weight);
                }
            }).build();
        };
    }

    public static <T> IWeighted<T> toTFC(List<Weighted<T>> values) {
        final List<Pair<T, Double>> list = values.stream()
                .map(w -> Pair.of(w.value, (double) w.weight))
                .toList();
        return switch (list.size()) {
            case 0 -> IWeighted.empty();
            case 1 -> IWeighted.singleton(list.getFirst().getFirst());
            default -> new net.dries007.tfc.util.collections.Weighted<>(list);
        };
    }

    public static <K, V> Map<K, IWeighted<V>> toTFC(Map<K, List<Weighted<V>>> map) {
        final Map<K, IWeighted<V>> ret = new LinkedHashMap<>();
        map.forEach((k, l) -> ret.put(k, toTFC(l)));
        return ret;
    }
}

