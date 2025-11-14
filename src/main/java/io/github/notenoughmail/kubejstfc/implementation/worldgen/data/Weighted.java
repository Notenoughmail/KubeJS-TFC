package io.github.notenoughmail.kubejstfc.implementation.worldgen.data;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaArray;
import dev.latvian.mods.rhino.NativeJavaList;
import dev.latvian.mods.rhino.NativeJavaMap;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.dries007.tfc.util.collections.IWeighted;
import net.minecraft.Util;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.util.*;

public record Weighted<T>(T value, int weight) {

    public static final TypeInfo TYPE = TypeInfo.of(Weighted.class);

    private static final Set<Class<?>> CLASSES = Set.of(
            NativeJavaMap.class,
            NativeJavaArray.class,
            NativeJavaList.class,
            Map.class,
            Collection.class
    );

    // TODO: 2.0.0 | This may or may not actually work as I want it to
    public static Weighted<?> wrap(Context ctx, Object from, TypeInfo target) {
        return switch (from) {
            case null -> throw Context.reportRuntimeError("Can't interpret 'null' as a weighted object", ctx);
            case Weighted<?> w -> w;
            case Object o when CLASSES.stream().noneMatch(o.getClass()::isAssignableFrom) ->
                    new Weighted<>(ctx.jsToJava(o, target.param(0)), 1);
            default -> (Weighted<?>) ctx.jsToJava(from, TYPE);
        };
    }

    public static <T> SimpleWeightedRandomList<T> toVanilla(List<Weighted<T>> values) {
        return Util.make(SimpleWeightedRandomList.<T>builder(), b -> {
            for (Weighted<T> w : values) {
                b.add(w.value, w.weight);
            }
        }).build();
    }

    public static <T> IWeighted<T> toTFC(List<Weighted<T>> values) {
        return new net.dries007.tfc.util.collections.Weighted<>(
                values.stream()
                        .map(w -> Pair.of(w.value, (double) w.weight))
                        .toList()
        );
    }

    public static <K, V> Map<K, IWeighted<V>> toTFC(Map<K, List<Weighted<V>>> map) {
        final Map<K, IWeighted<V>> ret = new LinkedHashMap<>();
        map.forEach((k, l) -> ret.put(k, toTFC(l)));
        return ret;
    }
}

