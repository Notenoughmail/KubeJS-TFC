package io.github.notenoughmail.kubejstfc.implementation.worldgen.data;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.RecordTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.dries007.tfc.util.collections.IWeighted;
import net.minecraft.Util;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An 'abstracted' representation of a weighted value which can be converted to vanilla's {@link SimpleWeightedRandomList}
 * or TFC's {@link IWeighted}.
 * <p>
 * A single or null {@code Weighted} object is meaningless and should not occur
 */
public record Weighted<T>(T value, int weight) {

    public static final RecordTypeInfo TYPE = Cast.to(TypeInfo.of(Weighted.class));

    // TODO: 2.0.0 | Test to make sure the T param is properly wrapped
    public static Weighted<?> wrap(Context ctx, Object from, TypeInfo target) {
        return switch (from) {
            case null -> throw Context.reportRuntimeError("Can't interpret 'null' as a weighted object", ctx);
            case Weighted<?> w -> w;
            case Map<?, ?> map -> c(ctx, map, target);
            case Iterable<?> itr -> c(ctx, itr, target);
            case BaseFunction func -> c(ctx, func, target);
            default -> new Weighted<>(ctx.jsToJava(from, target.param(0)), 1);
        };
    }

    private static Weighted<?> c(Context ctx, Object from, TypeInfo target) {
        return Cast.to(TYPE.wrap(ctx, from, target));
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

