package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// We want the builder to be final, but the id needs to be user-definable
public class DelayedBuilder<T extends BuilderBase<?>> implements Supplier<T> {

    @Nullable
    protected T builder;
    private final Function<ResourceLocation, T> constructor;
    private final Supplier<ResourceLocation> fallbackId;
    @Nullable
    private Consumer<T> onConstruct;

    public DelayedBuilder(Function<ResourceLocation, T> constructor, Supplier<ResourceLocation> fallbackId) {
        this.constructor = constructor;
        this.fallbackId = fallbackId;
    }

    public void onConstruct(Consumer<T> onConstruct) {
        this.onConstruct = onConstruct;
    }

    public ResourceLocation fallbackId() {
        return fallbackId.get();
    }

    public T get(ResourceLocation id) {
        if (builder == null) {
            builder = constructor.apply(id);
        }
        if (onConstruct != null) {
            onConstruct.accept(builder);
            onConstruct = null;
        }
        return builder;
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
            onMarkedNull = action;
        }

        public void markNull() {
            markedNull = true;
            if (builder != null && onMarkedNull != null) {
                onMarkedNull.accept(builder);
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
