package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.notenoughmail.kubejstfc.util.Printer.Hidden.*;

public interface Printer {

    static void simpleAdd(MutableComponent txt, Object value) {
        txt.append(asComponent(value));
    }

    static void simpleAdd(MutableComponent txt, String val, ChatFormatting color) {
        txt.append(Component.literal(val).withStyle(color));
    }

    static void newLine(MutableComponent txt) {
        txt.append(CommonComponents.NEW_LINE);
    }

    static void descriptor(MutableComponent txt, String descriptor) {
        txt.append(Component.literal(descriptor).withStyle(ChatFormatting.RED))
                .append(Component.literal(": "));
    }

    static void append(MutableComponent txt, String description, Object value) {
        append(txt, description, value, false);
    }

    // TODO: 2.0.0 | Actually implement this
    static void append(MutableComponent txt, String description, Object value, boolean end) {

    }

    static MutableComponent clickableTag(TagKey<?> tag) {
        return Component.literal("#" + tag.location())
                .withStyle(s -> s
                        .withUnderlined(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("List %s entries".formatted(tag.location()))))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/forge tags %s get %s".formatted(tag.registry().location(), tag.location())))
                );
    }

    static MutableComponent asComponent(Object o) {
        if (o instanceof MutableComponent mut) {
            return mut;
        }
        return Component.literal(stringify(o)).withStyle(s -> s.withColor(getColor(o)));
    }

    static TextColor getColor(Object value) {
        return switch (value) {
            case null -> COLORS[0];
            case Number n -> COLORS[1];
            case Boolean b -> COLORS[2];
            case CharSequence c -> COLORS[3];
            case ResourceLocation r -> COLORS[3];
            case Enum<?> e -> COLORS[4];
            case MutableComponent mut -> mut.getStyle().getColor();
            default -> COLORS[5];
        };
    }

    // Whoa! The defaults for a lot of values have been made decent
    static String stringify(Object value) {
        return switch (value) {
            case MobEffect m -> getId(BuiltInRegistries.MOB_EFFECT, m);
            case Block b -> getId(BuiltInRegistries.BLOCK, b);
            case EntityType<?> e -> getId(BuiltInRegistries.ENTITY_TYPE, e);
            default -> String.valueOf(value);
        };
    }

    private static <T> String getId(Registry<T> registry, T object) {
        return registry.getResourceKey(object).orElseThrow().location().toString();
    }

    static <R extends Record> Map<String, Object> convertRecordToMap(R r_) {
        return RECORD_CONVERTERS.computeIfAbsent(r_.getClass(), c -> {
            final RecordComponent[] components = c.getRecordComponents();
            return (R r) -> {
                final Map<String, Object> map = HashMap.newHashMap(components.length);
                for (RecordComponent component : components) {
                    final String name = component.getName();
                    Object o;
                    try {
                        o = component.getAccessor().invoke(r);
                    } catch (Exception e) {
                        KubeJSTFC.LOGGER.error("Unable to access '%s' field of %s".formatted(name, component.getDeclaringRecord().getName()), e);
                        o = null;
                    }
                    map.put(name, o);
                }
                return map;
            };
        }).apply(Cast.to(r_));
    }

    class Hidden {
        static final TextColor[] COLORS = {
                TextColor.fromLegacyFormat(ChatFormatting.BLACK),
                TextColor.fromLegacyFormat(ChatFormatting.GREEN),
                TextColor.fromLegacyFormat(ChatFormatting.GOLD),
                TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE),
                TextColor.fromLegacyFormat(ChatFormatting.AQUA),
                TextColor.fromLegacyFormat(ChatFormatting.GRAY)
        };

        static final Map<Class<?>, Function<?, Map<String, Object>>> RECORD_CONVERTERS = new IdentityHashMap<>();
    }
}
