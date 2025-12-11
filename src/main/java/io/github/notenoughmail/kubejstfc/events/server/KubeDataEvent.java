package io.github.notenoughmail.kubejstfc.events.server;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.function.Function;

public abstract class KubeDataEvent implements KubeEvent {

    protected final KubeResourceGenerator gen;

    protected KubeDataEvent(KubeResourceGenerator gen) {
        this.gen = gen;
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
        gen.json(id, codec.encodeStart(gen.getRegistries().json(), t).getOrThrow());
    }

    protected <T> void add(T t, Codec<T> codec, @Nullable KubeResourceLocation id, Function<T, String> func, String prefix) {
        add(id(id, t, func, prefix), t, codec);
    }

    protected <T> void add(T t, Codec<T> codec, @Nullable KubeResourceLocation id, String prefix) {
        add(t, codec, id, this::makePath, prefix);
    }
}
