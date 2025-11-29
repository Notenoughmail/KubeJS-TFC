package io.github.notenoughmail.kubejstfc.events.server;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public abstract class KubeDataEvent implements KubeEvent {

    private static final HolderOwner<?> UNIVERSAL_OWNER = new HolderOwner<>() {
        @Override
        public boolean canSerializeIn(HolderOwner<Object> owner) {
            return true;
        }
    };
    protected static <T> HolderOwner<T> universalOwner() {
        return Cast.to(UNIVERSAL_OWNER);
    }
    private static final HolderGetter<?> UNIVERSAL_GETTER = new HolderGetter<>() {
        @Override
        public Optional<Holder.Reference<Object>> get(ResourceKey<Object> resourceKey) {
            return Optional.empty();
        }

        @Override
        public Optional<HolderSet.Named<Object>> get(TagKey<Object> tagKey) {
            return Optional.empty();
        }
    };
    protected static <T> HolderGetter<T> universalGetter() {
        return Cast.to(UNIVERSAL_GETTER);
    }

    protected final KubeResourceGenerator gen;
    private final RegistryOps<JsonElement> fakeRegistryExtension;

    protected KubeDataEvent(KubeResourceGenerator gen) {
        this.gen = gen;
        final RegistryOps<JsonElement> realRegistryAccess = gen.getRegistries().json();
        fakeRegistryExtension = RegistryOps.create(realRegistryAccess, new RegistryOps.RegistryInfoLookup() {
            @Override
            public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
                return Optional.of(
                        realRegistryAccess.lookupProvider.lookup(registryKey)
                                .orElseGet(() -> new RegistryOps.RegistryInfo<>(
                                        universalOwner(),
                                        universalGetter(),
                                        Lifecycle.experimental()
                                ))
                );
            }
        });
    }

    protected String makePath(Object path) {
        String out;
        if (path instanceof CharSequence s) {
            out = s.toString();
        } else {
            try {
                final byte[] bytes = String.valueOf(path).getBytes(StandardCharsets.UTF_8);
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                out = new BigInteger(HexFormat.of().formatHex(digest.digest(bytes)), 16).toString(36);
            } catch (Exception e) {
                out =  Integer.toHexString(path.hashCode());
            }
        }
        out = out.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_", "")
                .replaceAll("_$", "");
        return out.length() > 64 ? out.substring(0, 64).replaceAll("_$", "") : out;
    }

    protected <T>ResourceLocation id(@Nullable KubeResourceLocation id, T t, Function<T, String> func, String prefix) {
        return (id == null ? KubeJS.id(func.apply(t)) : id.wrapped()).withPrefix(prefix + "/");
    }

    protected <T> void add(ResourceLocation id, T t, Codec<T> codec) {
        gen.json(id, codec.encodeStart(fakeRegistryExtension, t).getOrThrow());
    }

    protected <T> void add(T t, Codec<T> codec, @Nullable KubeResourceLocation id, Function<T, String> func, String prefix) {
        add(id(id, t, func, prefix), t, codec);
    }

    protected <T> void add(T t, Codec<T> codec, @Nullable KubeResourceLocation id, String prefix) {
        add(t, codec, id, this::makePath, prefix);
    }
}
