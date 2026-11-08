package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// We want the builder to be final, but the id needs to be user-definable
public class DelayedBuilder<T extends BuilderBase<?>> extends Actionable<T> implements Supplier<T> {

    public static <T extends BuilderBase<?>> DelayedBuilder.NullCapable<T> startingNull(
            Function<ResourceLocation, @Nullable T> constructor,
            Supplier<ResourceLocation> fallbackId
    ) {
        final NullCapable<T> nullable = new NullCapable<>(constructor, fallbackId);
        nullable.markNull();
        return nullable;
    }

    private final Function<ResourceLocation, T> constructor;
    private final Supplier<ResourceLocation> fallbackId;

    public DelayedBuilder(Function<ResourceLocation, T> constructor, Supplier<ResourceLocation> fallbackId) {
        this.constructor = constructor;
        this.fallbackId = fallbackId;
    }

    public ResourceLocation fallbackId() {
        return fallbackId.get();
    }

    public T get(ResourceLocation id) {
        if (actor == null) {
            accept(constructor.apply(id));
        }
        return actor;
    }

    public void accept(@Nullable KubeResourceLocation id, Consumer<T> action) {
        action.accept(get(id == null ? fallbackId() : id.wrapped()));
    }

    @Override
    public T get() {
        return get(fallbackId());
    }

    public static class NullCapable<T extends BuilderBase<?>> extends DelayedBuilder<@Nullable T> {

        private boolean markedNull;
        @Nullable
        private Consumer<T> onMarkedNull;

        public NullCapable(Function<ResourceLocation, @Nullable T> constructor, Supplier<ResourceLocation> fallbackId) {
            super(constructor, fallbackId);
        }

        public void onMarkedNull(Consumer<T> action) {
            if (onMarkedNull == null) {
                onMarkedNull = action;
            } else {
                onMarkedNull = onMarkedNull.andThen(action);
            }
        }

        public void markNull() {
            markedNull = true;
            if (actor != null && onMarkedNull != null) {
                onMarkedNull.accept(actor);
            }
        }

        public void unmarkNull() {
            markedNull = false;
        }

        public boolean isNull() {
            return markedNull;
        }

        @Override
        @Nullable
        public T get(ResourceLocation id) {
            if (markedNull) return null;
            return super.get(id);
        }

        @Override
        @Nullable
        public T get() {
            return super.get();
        }

        public void ifNotMarkedNull(Consumer<T> action) {
            if (!markedNull) {
                action.accept(get());
            }
        }

        @Override
        public void accept(@Nullable KubeResourceLocation id, @Nullable Consumer<T> action) {
            if (action == null) {
                markNull();
            } else {
                super.accept(id, action);
            }
        }
    }
}
