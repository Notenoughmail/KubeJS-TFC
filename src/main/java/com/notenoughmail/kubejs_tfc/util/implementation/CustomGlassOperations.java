package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.Wrapper;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CustomGlassOperations {

    public static final LockableMap<String, Integer> SYNC_VALIDATION = new LockableMap<>();
    private static final LockableMap<GlassOperation, ExtraData> EXTRA_DATA = new LockableMap<>();

    public static void lock() {
        SYNC_VALIDATION.lock();
        EXTRA_DATA.lock();
    }

    public static void track(GlassOperation op, Supplier<@Nullable SoundEvent> sound, float minHeat, @Nullable StackSupplier stack) {
        EXTRA_DATA.put(op, new ExtraData(sound, minHeat, stack));
        SYNC_VALIDATION.put(op.name(), op.ordinal());
    }

    public static boolean isEmpty() {
        return SYNC_VALIDATION.isEmpty();
    }

    @Nullable
    public static SoundEvent getSound(GlassOperation op) {
        final ExtraData extraData = EXTRA_DATA.get(op);
        if (extraData != null) {
            return extraData.sound().get();
        }
        return null;
    }

    public static float getMinHeat(GlassOperation op) {
        final ExtraData extraData = EXTRA_DATA.get(op);
        if (extraData != null) {
            return extraData.minHeat() <= 0.0F ? Float.NEGATIVE_INFINITY : extraData.minHeat();
        }
        return Float.NEGATIVE_INFINITY;
    }

    public static void addDisplays(BiConsumer<GlassOperation, ItemStack> adder) {
        EXTRA_DATA.forEach((op, data) -> {
            if (data.stack() != null) {
                adder.accept(op, data.stack().get());
            }
        });
    }

    record ExtraData(Supplier<@Nullable SoundEvent> sound, float minHeat, @Nullable StackSupplier stack) {}

    @FunctionalInterface
    public interface StackSupplier {
        ItemStack get();

        static StackSupplier wrap(Context ctx, Object o) {
            if (o instanceof Wrapper w) {
                o = w.unwrap();
            }

            if (o instanceof CharSequence || o instanceof ResourceLocation) {
                Object finalO = o;
                return () -> ItemStackJS.of(finalO);
            } else if (o instanceof BaseFunction func) {
                return (StackSupplier) NativeJavaObject.createInterfaceAdapter(ctx, StackSupplier.class, func);
            }
            return null;
        }
    }

    public static class LockableMap<K, V> extends HashMap<K, V> {

        @Override
        public V put(K key, V value) {
            if (!locked) {
                return super.put(key, value);
            } else {
                throw new IllegalCallerException("Cannot add values while locked!");
            }
        }

        @Override
        public V remove(Object key) {
            if (!locked) {
                return super.remove(key);
            } else {
                throw new IllegalCallerException("Cannot remove values when locked");
            }
        }

        @Override
        public boolean remove(Object key, Object value) {
            if (!locked) {
                return super.remove(key, value);
            } else {
                throw new IllegalCallerException("Cannot remove values when locked!");
            }
        }

        private boolean locked = false;

        public void lock() {
            locked = true;
        }
    }
}
