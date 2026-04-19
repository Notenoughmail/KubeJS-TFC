package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// There exists a number of cases where we want the builder to be final
// but would also like for the id to be editable
public class DelayedBuilder<T extends BuilderBase<?>> implements Supplier<T> {

    @Nullable
    private T builder;
    private final Function<ResourceLocation, T> constructor;
    private final Supplier<ResourceLocation> fallbackId;
    private Consumer<T> onConstruct;

    public DelayedBuilder(Function<ResourceLocation, T> constructor, Supplier<ResourceLocation> fallbackId) {
        this.constructor = constructor;
        this.fallbackId = Lazy.of(fallbackId);
    }

    public T get(ResourceLocation id) {
        if (builder == null) {
            builder = constructor.apply(id);
            if (onConstruct != null) onConstruct.accept(builder);
        }
        if (onConstruct != null) {
            onConstruct.accept(builder);
            onConstruct = null;
        }
        return builder;
    }

    public T get() {
        return get(fallbackId.get());
    }

    public ResourceLocation fallbackId() {
        return fallbackId.get();
    }

    public void onConstruct(Consumer<T> onConstruct) {
        this.onConstruct = onConstruct;
    }

    public static class NullCapable<T extends BuilderBase<?>> extends DelayedBuilder<@Nullable T> {

        private boolean markedNull;

        public NullCapable(Function<ResourceLocation, T> constructor, Supplier<ResourceLocation> fallbackId) {
            super(constructor, fallbackId);
            markedNull = false;
        }

        public void markNull() {
            markedNull = true;
        }

        @Nullable
        @Override
        public T get(ResourceLocation id) {
            if (markedNull) return null;
            return super.get(id);
        }

        @Nullable
        @Override
        public T get() {
            return super.get();
        }

        public void ifNotMarkedNull(Consumer<T> action) {
            if (!markedNull) {
                action.accept(get());
            }
        }
    }
}
